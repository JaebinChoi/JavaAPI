package socket;

import java.io.*;
import java.net.*;
import java.util.*;

/** Socket Programming : Multi Chatting Program */
public class P02_MultiChatServer {
	HashMap clients; // Client 정보를 담을 Map

	P02_MultiChatServer() {
		clients = new HashMap();
		Collections.synchronizedMap(clients); // 동기화(하나의 Thread가 HashMap을 사용 중일 경우 다른 Thread에서 접근 불가)
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(9999); 													// 서버에서 오픈한 포트 번호
			System.out.println("start server...");
			
			while (true) { 																			// 여기서 Client의 접속을 받아들임
				socket = serverSocket.accept(); 													// Client 접속 대기 (Client 정보 저장)
				System.out.println(socket.getInetAddress() + ":" + socket.getPort() + " connect!"); // Client 정보

				ServerReceiver thread = new ServerReceiver(socket);									// Client에서 전송하는 데이터를 받는 쓰레드 실행
				thread.start(); 																	// run()
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	// Inner Class (Client에서 전송하는 메시지를 수신하고 브로드캐스팅하는 Thread)
	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in; 															// 입력 스트림
		DataOutputStream out; 															// 출력 스트림

		ServerReceiver(Socket socket) { 												// Constructor
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (Exception e) { e.printStackTrace(); }
		}

		public void run() {
			String name = "";
			
			try {
				name = in.readUTF(); 													// 사용자 이름 수신
				if (clients.get(name) != null) { 										// 같은 이름의 사용자 존재
					out.writeUTF("#Aleady exist name : " + name);
					out.writeUTF("#Please reconnect by other name");
					System.out.println(socket.getInetAddress() + ":" + socket.getPort() + " disconnect!");
					
					in.close();
					out.close();
					socket.close();
					socket = null;
				} else { 																// 같은 이름이 존재 X
					sendToAll("#" + name + " join!"); 									// Client 입장을 브로드캐스팅 (본인 제외)
					clients.put(name, out);												// key : 사용자 이름, value : 해당 사용자의 OutputStream
					
					while (in != null) { 												// 채팅 메시지를 읽는다 (quit 전까지 무한루프)
						sendToAll(in.readUTF());
					}
				}
			} catch (IOException e) { e.printStackTrace(); }
			finally { 																	// 위 while 문에서 나오면 (quit 입력했을 경우)
				if (socket != null) { 													// 소켓은 무조건 있음
					sendToAll("#" + name + " exit!");									// 퇴장 메시지 브로드캐스팅
					clients.remove(name); 												// 소켓 제거
					System.out.println(socket.getInetAddress() + ":" + socket.getPort() + " disconnect!");
				}
			}
		}
	}

	void sendToAll(String msg) { 														// 브로드캐스팅 기능
		Iterator iterator = clients.keySet().iterator(); 								// Client의 Key 값들을 Iterator 배열에 저장
		
		while (iterator.hasNext()) {
			try {
				DataOutputStream out = (DataOutputStream) clients.get(iterator.next()); // 해당 Client의 OutputStream을 가져옴
				out.writeUTF(msg); 														// 출력
			} catch (IOException e) { e.printStackTrace(); }
		}
	}

	public static void main(String[] args) {
		new P02_MultiChatServer().start();
	}
}