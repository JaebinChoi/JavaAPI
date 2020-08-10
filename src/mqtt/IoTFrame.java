package mqtt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class IoTFrame extends JFrame implements ActionListener, ReceiveEventListener {
	private static final long serialVersionUID = 1L;
	private JTextField tmp = new JTextField(10);
	private JTextField hum = new JTextField(10);
	private JButton ledOn = new JButton("LED_ON");
	private JButton ledOff = new JButton("LED_OFF");
	private JLabel msg = new JLabel("온습도 모니터링");
	private JTextArea out = new JTextArea(20, 40);
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel panel3 = new JPanel();
	private ScrollPane sp = new ScrollPane();
	
	private MqttClass mqtt = null;
	public IoTFrame(MqttClass mqtt) {
		this();
		this.mqtt = mqtt;
		this.mqtt.setMyEventListner(this);
	}

	public IoTFrame() {
		super("MQTT 사물인터넷 통신 프로젝트");
		setSize(400, 400);
		panel1.add(msg);
		panel1.add(ledOn);
		panel1.add(ledOff);
		panel2.add(tmp);
		panel2.add(hum);
		sp.add(out);
		
		add(BorderLayout.NORTH, panel1); // JFrame에 div
		add(BorderLayout.CENTER, sp); 	 // JFrame에 div
		add(BorderLayout.SOUTH, panel2);
		// add(BorderLayout.EAST, panel3);
		
		// Button Click Listener
		ledOn.addActionListener(this); 	// 통지할 수 있게 설정
		ledOff.addActionListener(this); // 통지할 수 있게 설정
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// ReceiveEventListener 인터페이스에서 상속받은 것
	@Override
	public void recvMsg(String topic, MqttMessage msg) {
		System.out.println(topic + "," + msg);
		
		String append = out.getText();
		out.setText(topic + "," + msg + "\n" + append);
		
		JSONObject obj = new JSONObject(new String(msg.getPayload()));
		tmp.setText("온도:" + obj.get("tmp").toString());
		hum.setText("습도:" + obj.get("hum").toString());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if (b.getText().equals("LED_ON")) mqtt.sendMessage("1");
		else if (b.getText().equals("LED_OFF")) mqtt.sendMessage("2");
	}
}