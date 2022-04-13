package kr.re.keti.sc.dataservicebroker.notification.sender.http;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.re.keti.sc.dataservicebroker.common.exception.NotificationException;
import kr.re.keti.sc.dataservicebroker.notification.sender.NotificationSender;
import kr.re.keti.sc.dataservicebroker.notification.vo.NotificationVO;
import kr.re.keti.sc.dataservicebroker.subscription.vo.SubscriptionVO.NotificationParams.KeyValuePair;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpSender implements NotificationSender {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	

	@Override
	public boolean sendNotification(String subscriptionId, String endpointUri, List<KeyValuePair> notifierInfo,
			List<KeyValuePair> receiverInfo, String entityId, NotificationVO notificationVO) throws NotificationException {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	
			HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(notificationVO), headers);
	
			long startTime = System.currentTimeMillis();
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(endpointUri, entity, String.class);
			long elapsedTime = System.currentTimeMillis() - startTime;
	
			if(responseEntity == null) {
				log.warn("HTTP Notification Response is null. subsciprionId={}, entityId={}, endpointUri={}, elapsedTime={}ms",
						subscriptionId, entityId, endpointUri, elapsedTime);
			} else {
				log.info("HTTP Notification Response code={}, subsciprionId={}, entityId={}, elapsedTime={}ms", 
						responseEntity.getStatusCodeValue(), subscriptionId, entityId, elapsedTime);
			}
			
			if(responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
				return true;
			}

		} catch(HttpClientErrorException e) {
			log.warn("HTTP Notification Response code={}. subsciprionId={}, entityId={}, endpointUri={}",
					e.getRawStatusCode(), subscriptionId, entityId, endpointUri, e);
		} catch(Exception e) {
			log.warn("HTTP Notification error. subsciprionId={}, entityId={}, endpointUri={}",
					subscriptionId, entityId, endpointUri, e);
		}
		return false;
	}

}
