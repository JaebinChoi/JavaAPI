package mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface ReceiveEventListener {
	public void recvMsg(String topic, MqttMessage msg);

}