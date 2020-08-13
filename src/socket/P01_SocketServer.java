package socket;

import java.net.*;
import java.io.*;

/** Socket Programming : ECHO(메아리) Program */
public class P01_SocketServer {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(5000); // 임의의 포트 Open
			System.out.println("Server is ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				// 새로운 소켓이 생성될 때까지 접속 대기(Blocking)
			 	Socket socket = serverSocket.accept(); 					// Client의 정보를 알고 있는 소켓
			 	System.out.println("Client Connect Success");
			 	
			 	// 데이터 수신
			 	InputStream in = socket.getInputStream(); 				// Client에서 받을 InputStream : Byte Stream (빨때라고 생각)
			 	DataInputStream dis = new DataInputStream(in); 			// 한글 데이터를 입력 받기 위해 DataInputStream 사용
			 	String message = dis.readUTF();							// 데이터 수신할 때까지 대기
			 	
			 	// 데이터 전송
			 	OutputStream out = socket.getOutputStream();			// Client에 전송할 OuputStream : Byte Stream
			 	DataOutputStream dos = new DataOutputStream(out);		// 한글 데이터를 전송하기 위해 DataOutputStream 사용
			 	dos.writeUTF("[ECHO]  " + message + "  (From Server)");	// Server에서 데이터 전송
			 	
			 	dos.close();
			 	dis.close();
			 	socket.close(); 										// 소켓 연결 끊기
			 	System.out.println("Client Socket Close");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // while
	} // main
} // class
