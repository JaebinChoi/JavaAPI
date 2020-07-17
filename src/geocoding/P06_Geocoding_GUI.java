package geocoding;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class P06_Geocoding_GUI {
	JTextField address;
	JLabel resAddress, resX, resY, jibunAddress;
	JLabel imageLabel;
	
	public void initGUI() {
		JFrame frame = new JFrame("Map View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frame.getContentPane();
		
		imageLabel = new JLabel("지도 보기");
		
		// ## 주소 검색 부분 ##
		// 컴포넌트 생성
		JLabel addressLbl = new JLabel("주소 입력");
		address = new JTextField(50);
		JButton btn = new JButton("클릭");
		
		// 판넬에 컴포넌트 부착
		JPanel pan_address = new JPanel();
		pan_address.add(addressLbl);
		pan_address.add(address);
		pan_address.add(btn);
		
		btn.addActionListener(new NaverMap(this));
		
		// ## 결과 부분 ##
		// 컴포넌트 생성
		resAddress = new JLabel("도로명 주소");
		jibunAddress = new JLabel("지번 주소");
		resX = new JLabel("경도");
		resY = new JLabel("위도");
		
		// 판넬에 컴포넌트 부착
		JPanel pan_result = new JPanel();
		pan_result.setLayout(new GridLayout(4, 1));
		pan_result.add(resAddress);
		pan_result.add(jibunAddress);
		pan_result.add(resX);
		pan_result.add(resY);
		
		// 컨테이너에 판넬 부착
		c.add(BorderLayout.NORTH, pan_address);
		c.add(BorderLayout.CENTER, imageLabel);
		c.add(BorderLayout.SOUTH, pan_result);
		
		frame.setSize(730, 660);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new P06_Geocoding_GUI().initGUI();
	}
}
