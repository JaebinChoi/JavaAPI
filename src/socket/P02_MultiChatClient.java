package socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/** Socket Programming : Multi Chatting Program */
public class P02_MultiChatClient {
	public static void main(String[] arg) {
		try {
			Socket socket = new Socket("127.0.0.1", 9999); 				// 채팅 서버 주소 및 포트
			Scanner scanner = new Scanner(System.in);
			
			System.out.print("name >> ");
			String name = scanner.nextLine();
			
			Thread sender = new Thread(new ClientSender(socket, name)); // 서버에 메시지 전송할 Thread
			Thread receiver = new Thread(new ClientReceiver(socket)); 	// 서버로부터 메시지 수신할 Thread
			
			sender.start();												// run()
			receiver.start();											// run()
		} catch (Exception e) { e.printStackTrace(); }
	}

	// Inner Class (Client의 메시지 내용을 Server로 전송하는 Thread)
	static class ClientSender extends Thread {
		Socket socket;
		DataOutputStream out;
		String name;

		ClientSender(Socket socket, String name) { 						// Constructor
			this.socket = socket;
			this.name = name;
			try {
				out = new DataOutputStream(socket.getOutputStream()); 	// 출력 스트림 생성
			} catch (Exception e) { e.printStackTrace(); }
		}

		public void run() {
			Scanner scanner = new Scanner(System.in);
			
			try {
				if (out != null) out.writeUTF(name); 					// 입장 알림을 위한 name 전송
				while (out != null) { 									// 메시지 작성 (quit 전까지 무한 루프)
					String message = scanner.nextLine();
					
					if (message.equals("quit")) break;
					out.writeUTF("[" + name + "]" + message);			// 서버로 메시지 전송
				}
				
				out.close();
				socket.close();
			} catch (Exception e) { e.printStackTrace(); }
		}
	}

	// Inner Class (Server에서 브로드캐스팅한 메시지를 수신하는 Thread)
	static class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream in;

		ClientReceiver(Socket socket) {									// Constructor
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream()); 		// 입력 스트림 생성
			} catch (Exception e) { e.printStackTrace(); }
		}

		public void run() {
			while (in != null) { 										// InputStream != null (메시지 내용이 있는 동안)
				try { System.out.println(in.readUTF()); }				// 메시지 내용 출력
				catch (Exception e) { e.printStackTrace(); break; }
			}
			
			// InputStream == null (Server에서 Thread가 끝까지 가면 종료되면서 InputStream이 소멸됨)
			try { in.close(); socket.close(); }
			catch (Exception e) { e.printStackTrace(); }
		}
	}
}