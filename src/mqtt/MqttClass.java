package mqtt;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.*;

public class MqttClass implements MqttCallback {
	private MqttClient client = null;
	private ReceiveEventListener listener = null;

	public MqttClass() { 			// mqttClass 클래스 호출 시
		new Thread(task).start(); 	// task 쓰레드 실행
	}

	// Thread
	Runnable task = new Runnable() {
		@Override
		public void run() {
			try {
				String clientId = UUID.randomUUID().toString(); // 임의 아이디 설정
				
				// new MqttClient()
				// 서버와 연결
				// client = new MqttClient("tcp://IPv4 Address", clientId);
				client = new MqttClient("tcp://192.168.0.15", clientId);
				MqttConnectOptions connopt = new MqttConnectOptions(); 
				connopt.setCleanSession(true); 	// Mqtt 연결  초기화
				client.connect(connopt); 		// Mqtt 연결
				client.subscribe("dht11"); 		// 수신자 등록
				
				/*
				 * Callback Interface를 상속했기 때문에 자기 자신을 호출
				 * 발행자(publisher)로부터 연결이 끊기거나, 전송이 완료되거나, 메시지가 도착하거나, 메시지를 보냈을 경우
				 * connectionLost(), deliveryComplete(), messageArrived(), sendMessage() 메소드 호출
				 */
				client.setCallback(MqttClass.this); 
				
				// GUI 실행할 때 MqttClass를 넘겨줌(MqttClass의 메소드들을 GUI에서 사용하기 위함)
				new IoTFrame(MqttClass.this);
			} catch (MqttException e) {
				System.out.println("ERR0" + e.getStackTrace());
			}
		}
	};

	public void sendMessage(String payload) {
		MqttMessage message = new MqttMessage();
		message.setPayload(payload.getBytes()); // 네트워크로 전송 시 String => Byte 변환 필요
		
		try { // MqttClient 연결되어 있으면 메시지 전송(publish)
			if (client.isConnected()) client.publish("led", message);
		} catch (MqttException e) {
			System.out.println("error1-" + e.getStackTrace()); // + e.getMessage());
		}
	}

	@Override
	public void connectionLost(Throwable arg0) {
		try {
			System.out.println("disconect");
			client.close();
		} catch (MqttException e) {
			System.out.println("error" + e.getMessage());
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	public void setMyEventListner(ReceiveEventListener listener) {
		this.listener = listener;
	}

	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		// System.out.println(topic+","+msg.toString());
		listener.recvMsg(topic, msg);
	}
}