package kr.re.keti.sc.dataservicebroker.notification.sender.mqtt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.re.keti.sc.dataservicebroker.common.exception.NotificationException;
import kr.re.keti.sc.dataservicebroker.notification.sender.NotificationSender;
import kr.re.keti.sc.dataservicebroker.notification.vo.NotificationVO;
import kr.re.keti.sc.dataservicebroker.subscription.vo.SubscriptionVO.NotificationParams.KeyValuePair;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MqttSender implements NotificationSender {

	@Autowired
	private ObjectMapper objectMapper;
	
	private static int DEFAULT_QOS = 0;

	private Map<String, MqttClient> mqttClientMap = new ConcurrentHashMap<>();
	private Object lockObject = new Object();
	
	@Override
	public boolean sendNotification(String subscriptionId, String endpointUri, List<KeyValuePair> notifierInfo,
			List<KeyValuePair> receiverInfo, String entityId, NotificationVO notificationVO) throws NotificationException {

		try {
			MqttConnectInfo mqttConnectInfo = uriToConnectInfo(endpointUri);

			MqttClient mqttClient = null;
			synchronized(lockObject) {
				mqttClient = mqttClientMap.get(subscriptionId);
				if(mqttClient == null) {
					mqttClient = new MqttClient(mqttConnectInfo.getBrokerHost(), subscriptionId, new MemoryPersistence());
				}
	
				if(!mqttClient.isConnected()) {
					
					MqttConnectOptions connOpts = new MqttConnectOptions();
					connOpts.setCleanSession(true);
					connOpts.setUserName(mqttConnectInfo.getUserName());
					if(mqttConnectInfo.getPassword() != null) {
						connOpts.setPassword(mqttConnectInfo.getPassword().toCharArray());
					}

					log.info("Connect mqtt session for notification. endpointUri={}, subscriptionId={}", endpointUri, subscriptionId);
					mqttClient.connect(connOpts);
				}
				mqttClientMap.put(subscriptionId, mqttClient);
			}

			MqttNotificationMessage mqttNotificationMessage = new MqttNotificationMessage();
			mqttNotificationMessage.setBody(notificationVO);

			Map<String, String> matadataMap = new HashMap<>();
			matadataMap.put(MqttMetadataKeys.CONTENT_TYPE.getCode(), MediaType.APPLICATION_JSON_VALUE); // TODO : accept 값 기반 가변
			matadataMap.put(MqttMetadataKeys.LINK.getCode(), objectMapper.writeValueAsString(notificationVO.getData().get(0).getContext()));
			mqttNotificationMessage.setMetadata(matadataMap);

			int qos = DEFAULT_QOS;
			if(notifierInfo != null) {
				for(KeyValuePair keyValuePair : notifierInfo) {
					if(NotifierInfoKeys.MQTT_QOS.getCode().equals(keyValuePair.getKey())) {
						try {
							qos = Integer.parseInt(keyValuePair.getValue());
						} catch (NumberFormatException e) {
							log.warn("MqttSender notifierInfo parse error. qos=" + keyValuePair.getValue(), e);
						}
						break;
					}
				}
			}

			String sendMessage = objectMapper.writeValueAsString(mqttNotificationMessage);

			log.debug("publish mqtt message. endpointUri={}, subscriptionId={}, message={}", endpointUri, subscriptionId, sendMessage);
		
			mqttClient.publish(mqttConnectInfo.getTopic(), sendMessage.getBytes(), qos, false);
			return true;

		} catch (MqttException e) {
			log.error("MqttSender sendNotification error. subscriptionId={}, endpointUri={}", subscriptionId, endpointUri, e);
		} catch (Exception e) {
			log.error("MqttSender sendNotification error. subscriptionId={}, endpointUri={}", subscriptionId, endpointUri, e);
		}
		return false;
	}
	
	
	private MqttConnectInfo uriToConnectInfo(String endpointUri) {
		
		// endpointUri : mqtt[s]://[<username>][:<password>]@<host>[:<port>]/<topic>[/<subtopic>]
		
		MqttConnectInfo mqttConnectInfo = new MqttConnectInfo();
		
		if(endpointUri.startsWith("mqtts")) {
			mqttConnectInfo.setEnableTls(true);
		}

		endpointUri = endpointUri.split("\\://")[1];

		if(endpointUri.contains("@")) {
			String[] uriArr = endpointUri.split("\\@", 2);
			String authInfo = uriArr[0];
			
			if(authInfo.contains(":")) {
				String[] authInfoArr = authInfo.split("\\:");
				mqttConnectInfo.setUserName(authInfoArr[0]);
				mqttConnectInfo.setPassword(authInfoArr[1]);
			} else {
				mqttConnectInfo.setUserName(authInfo);
			}
			endpointUri = uriArr[1];
		}
		
		String[] uriArr = endpointUri.split("\\/", 2);
		mqttConnectInfo.setBrokerHost("tcp://" + uriArr[0]);
		mqttConnectInfo.setTopic(uriArr[1]);
		return mqttConnectInfo;
	}
	
	@Data
	public static class MqttNotificationMessage {
		private NotificationVO body;
		private Map<String, String> metadata;
	}

	@Data
	public static class MqttConnectInfo {
		private String userName;
		private String password;
		private boolean enableTls;
		private String brokerHost;
		private String topic;
	}
	
	public static enum NotifierInfoKeys {
		MQTT_VERSION("MQTT-Version"),
		MQTT_QOS("MQTT-QoS");

		private String code;
		NotifierInfoKeys(String code) { this.code = code; }
		public String getCode() { return this.code; }
	}

	public static enum MqttMetadataKeys {
		CONTENT_TYPE("Content-Type"),
		LINK("Link");

		private String code;
		MqttMetadataKeys(String code) { this.code = code; }
		public String getCode() { return this.code; }
	}
}
