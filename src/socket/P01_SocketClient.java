package socket;

import java.net.*;
import java.util.Scanner;
import java.io.*;

/** Socket Programming : ECHO(메아리) Program */
public class P01_SocketClient {
	public static void main(String[] args) {
		try {
			// Server에 접속
			Socket socket = new Socket("127.0.0.1", 5000); // --> accept(), Server 정보 저장
			System.out.println("Connection Success");
			
			// 전송할 데이터 입력
			Scanner sc = new Scanner(System.in);
			String message = sc.nextLine();
			
			// 데이터 전송
			OutputStream out = socket.getOutputStream();		// 서버로 전송할 OutputStream : Byte Stream (빨때라고 생각)
			DataOutputStream dos = new DataOutputStream(out);	// 한글 데이터를 전송하기 위해 DataOutputStream 사용
			dos.writeUTF(message);								// 데이터 전송
			
			// 데이터 수신
			InputStream in = socket.getInputStream();			// 서버에서 받을 InputStream : Byte Stream (빨때라고 생각)
			DataInputStream dis = new DataInputStream(in);		// 한글 데이터를 입력 받기 위해 DataInputStream 사용
			String reply = dis.readUTF();						// 데이터 수신할 때까지 대기
			System.out.println("Reply  >>  " + reply);
			
			dis.close();
			dos.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
