package kr.re.keti.sc.dataservicebroker.datafederation.service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.re.keti.sc.dataservicebroker.common.code.DataServiceBrokerCode;
import kr.re.keti.sc.dataservicebroker.common.code.DataServiceBrokerCode.AttributeType;
import kr.re.keti.sc.dataservicebroker.csource.CsourceRegistrationManager;
import kr.re.keti.sc.dataservicebroker.csource.vo.CsourceRegistrationVO;
import kr.re.keti.sc.dataservicebroker.csource.vo.CsourceRegistrationVO.EntityInfo;
import kr.re.keti.sc.dataservicebroker.csource.vo.CsourceRegistrationVO.Information;
import kr.re.keti.sc.dataservicebroker.datamodel.dao.DataModelDAO;
import kr.re.keti.sc.dataservicebroker.datamodel.vo.Attribute;
import kr.re.keti.sc.dataservicebroker.datamodel.vo.DataModelBaseVO;
import kr.re.keti.sc.dataservicebroker.datamodel.vo.DataModelVO;
import kr.re.keti.sc.dataservicebroker.subscription.vo.SubscriptionVO;
import kr.re.keti.sc.dataservicebroker.subscription.vo.SubscriptionVO.NotificationParams;
import kr.re.keti.sc.dataservicebroker.subscription.vo.SubscriptionVO.NotificationParams.Endpoint;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataFederationService {

	@Autowired
	private CsourceRegistrationManager csourceRegistrationManager;
	@Autowired
	private DataModelDAO dataModelDAO;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${data-federation.standalone:true}")
	private Boolean federationStandalone;
	
    @Value("${data-federation.csource.id:#{null}}")
    private String federationCsourceId;
    @Value("${data-federation.csource.endpoint:#{null}}")
    private String federationCsourceEndpoint;
    
	@Value("${data-federation.subscription.id:#{null}}")
    private String federationCsourceSubscriptionId;
    @Value("${data-federation.subscription.endpoint:#{null}}")
    private String federationCsourceSubscriptionEndpoint;
    
    @Value("${data-federation.data-registry.base-uri:#{null}}")
    private String dataRegistryBaseUri;
    @Value("${data-federation.data-registry.sub-uri.subscription:#{null}}")
    private String dataRegistrySubscriptionUri;
    @Value("${data-federation.data-registry.sub-uri.csource:#{null}}")
    private String dataRegistryCsourceUri;
    

	@PostConstruct
	public void init() {

		// data-federation 연계 여부 체크
		if (enableFederation()) {
			try {
				// 1. data registry 로 csource 정보 등록
				registerCsource();

				// 2. data registry와 local 에 존재하는 csource 정보 조회하여 캐싱
				retrieveAndCachingCsource();

				// 3. data registry 에 csource 구독 정보 생성
				registerCsourceSubscription();

			} catch (Exception e) {
				log.error("DataFederationService initialize error", e);
			}
		}
	}

	public boolean enableFederation() {
		return !federationStandalone;
	}
	
	public String getFederationCsourceId() {
		return federationCsourceId;
	}

	private void retrieveAndCachingCsource() {
		
		// 1. data-registry 정보 조회 및 캐싱
		List<CsourceRegistrationVO> registryCsourceRegistrationVOs = getCsourceRegistrations();
		if (registryCsourceRegistrationVOs != null) {
			for (CsourceRegistrationVO csourceRegistrationVO : registryCsourceRegistrationVOs) {
				if(csourceRegistrationManager.getCsourceRegistrationCache(csourceRegistrationVO.getId()) == null) {
					csourceRegistrationManager.putCsourceRegistrationCache(csourceRegistrationVO);
				}
			}
		}
	}
	
	
    /**
     * Csource 전체 조회 요청
     * @return
     */
    public List<CsourceRegistrationVO> getCsourceRegistrations() {
    	
    	String requestUri = dataRegistryBaseUri + dataRegistryCsourceUri;
    	MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headerMap.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		RequestEntity<Void> requestEntity = new RequestEntity<>(headerMap, HttpMethod.GET, URI.create(requestUri));
		
		ResponseEntity<List<CsourceRegistrationVO>> response = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<CsourceRegistrationVO>>() {});
    	
    	return response.getBody();
    }

    /**
     * Csource 전체 등록 요청
     * @return
     */
	public ResponseEntity<Void> registerCsource() {

		// 1. csource 정보 생성
		CsourceRegistrationVO csourceRegistrationVO = createCsourceRegistrationInfo();
		
		log.info("Regist cSource to Data-Registry. csourceRegistrationVO={}", csourceRegistrationVO);

    	// 2. csource 업데이트 요청 (업데이트 시도 후 미 존재 시 생성 요청)
    	try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<CsourceRegistrationVO> entity = new HttpEntity<>(csourceRegistrationVO, headers);
            ResponseEntity<Void> result = restTemplate.exchange(dataRegistryBaseUri + dataRegistryCsourceUri + "/" + csourceRegistrationVO.getId(), 
                    HttpMethod.PATCH, entity, Void.class);
            return result;

    	} catch(HttpClientErrorException e) {
    		log.warn("HTTP CsourceRegistration Update Response code={}. Id={}",
					e.getRawStatusCode(), csourceRegistrationVO.getId(), e);
		} catch(Exception e) {
			log.warn("HTTP CsourceRegistration Update error. Id={}",
					csourceRegistrationVO.getId(), e);
		}
    	
    	// 3. 존재하지 않는 context source 인 경우 생성 요청
    	try{
    		return restTemplate.postForEntity(dataRegistryBaseUri + dataRegistryCsourceUri, csourceRegistrationVO, Void.class);
    	} catch(HttpClientErrorException e) {
    		log.warn("HTTP CsourceRegistration Create Response code={}. Id={}",
					e.getRawStatusCode(), csourceRegistrationVO.getId(), e);
		} catch(Exception e) {
			log.warn("HTTP CsourceRegistration Create error. Id={}",
					csourceRegistrationVO.getId(), e);
		}
    	
    	return null;
	}
	
	
    /**
     * Csource 구독 등록 요청
     * @return
     * @throws Exception
     */
    public ResponseEntity<Void> registerCsourceSubscription() {
        
        // 1. 구독 요청 객체 생성 
        SubscriptionVO subscriptionVO = new SubscriptionVO();
        subscriptionVO.setId(federationCsourceSubscriptionId);
        subscriptionVO.setType(DataServiceBrokerCode.JsonLdType.SUBSCRIPTION.getCode());
        subscriptionVO.setIsActive(true);
        NotificationParams notificationParams = new NotificationParams();
        Endpoint endpoint = new Endpoint();
        endpoint.setUri(federationCsourceSubscriptionEndpoint);
        endpoint.setAccept(MediaType.APPLICATION_JSON_VALUE);
        notificationParams.setEndpoint(endpoint);
        subscriptionVO.setNotification(notificationParams);
        
        // 2. 구독 생성 요청 (업데이트 시도 후 미 존재 시 생성 요청)
        // 2-1. 구독 업데이트 요청
        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<SubscriptionVO> entity = new HttpEntity<>(subscriptionVO, headers);
            ResponseEntity<Void> result = restTemplate.exchange(dataRegistryBaseUri + dataRegistrySubscriptionUri + "/" + subscriptionVO.getId(), 
                    HttpMethod.PATCH, entity, Void.class);
            return result;
        } catch(HttpClientErrorException e) {
            log.warn("HTTP CsourceRegistration Update Response code={}. Id={}",
                    e.getRawStatusCode(), subscriptionVO.getId(), e);
        } catch(Exception e) {
            log.warn("HTTP CsourceRegistration Update error. Id={}",
                    subscriptionVO.getId(), e);
        }
        
        try{
	        // 2-2. 존재하지 않는 구독 요청인 경우 생성 요청
	        return restTemplate.postForEntity(dataRegistryBaseUri + dataRegistrySubscriptionUri, subscriptionVO, Void.class);
	    } catch(HttpClientErrorException e) {
	        log.warn("HTTP CsourceRegistration Update Response code={}. Id={}",
	                e.getRawStatusCode(), subscriptionVO.getId(), e);
	    } catch(Exception e) {
	        log.warn("HTTP CsourceRegistration Update error. Id={}",
	                subscriptionVO.getId(), e);
	    }
        
        return null;
    }

    public CsourceRegistrationVO createCsourceRegistrationInfo() {
		CsourceRegistrationVO csourceRegistrationVO = new CsourceRegistrationVO();
		
		// 1. 전체 DataModel 정보 조회
    	List<DataModelBaseVO> dataModelBaseVOs = dataModelDAO.getDataModelBaseVOList();
    	if(dataModelBaseVOs == null || dataModelBaseVOs.isEmpty()) {
    		return null;
    	}
    	
    	// 2-1. CsourceRegistrationVO 기본 데이터 설정
    	csourceRegistrationVO.setId(federationCsourceId);
    	csourceRegistrationVO.setType(DataServiceBrokerCode.JsonLdType.CSOURCE_REGISTRATION.getCode());
    	csourceRegistrationVO.setEndpoint(federationCsourceEndpoint);
    	
    	// 2-2. 조회된 DataModel 기반 CsourceRegistrationVO(information) 데이터 생성
		List<Information> informations = new ArrayList<Information>();
    	for(DataModelBaseVO dataModelBaseVO : dataModelBaseVOs) {
			try {
				DataModelVO dataModel = objectMapper.readValue(dataModelBaseVO.getDataModel(), DataModelVO.class);
				Information information = dataModelToInformation(dataModel);
	    		informations.add(information);
			} catch (IOException e) {
				log.warn("createCsourceRegistrationInfo error. invalid Model. dataModel={}", dataModelBaseVO.getDataModel(), e);
			}
    	}
    	csourceRegistrationVO.setInformation(informations);
    	
    	return csourceRegistrationVO;
	}
	
	private Information dataModelToInformation(DataModelVO dataModel) {
		Information information = new Information();
		List<EntityInfo> entityInfos = new ArrayList<EntityInfo>();
		List<String> properties = new ArrayList<String>();
		List<String> relationships = new ArrayList<String>();
		EntityInfo entityInfo = new EntityInfo();
		
		// entities type 만 지원 (TODO: id 도 조회 하여 추가)
		entityInfo.setType(dataModel.getTypeUri());
		entityInfos.add(entityInfo);
		
		List<Attribute> attributes = dataModel.getAttributes();
		for(Attribute attribute : attributes) {
			if(AttributeType.PROPERTY.equals(attribute.getAttributeType())) {
				properties.add(attribute.getAttributeUri());
			} else if(AttributeType.RELATIONSHIP.equals(attribute.getAttributeType())) {
				relationships.add(attribute.getAttributeUri());
			} else {
				// do nothing.
			}
		}
		
		information.setEntities(entityInfos);
		information.setPropertyNames(properties);
		information.setRelationshipNames(relationships);
		
		return information;
	}
}
