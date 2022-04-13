package kr.re.keti.sc.dataservicebroker.notification.sender;

import java.util.List;

import kr.re.keti.sc.dataservicebroker.common.exception.NotificationException;
import kr.re.keti.sc.dataservicebroker.notification.vo.NotificationVO;
import kr.re.keti.sc.dataservicebroker.subscription.vo.SubscriptionVO.NotificationParams.KeyValuePair;

public interface NotificationSender {

	public boolean sendNotification(String subscriptionId, 
									String endpointUri, 
									List<KeyValuePair> notifierInfo,
									List<KeyValuePair> receiverInfo,
									String entityId, 
									NotificationVO notificationVO) throws NotificationException;
}
