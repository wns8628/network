package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import echo.EchoServerReceiveThread;

public class ChatServer {
	
	private static final int PORT = 5000;	
	public static void main(String[] args) {

		List<Writer> listWriters = new ArrayList<Writer>(); //공유객체 담는
		ServerSocket serverSocket = null;				
		try {
			// 1.서버 소켓생성
			serverSocket = new ServerSocket();
			
			//1-1 set option SO_reuseaddr
			//종료후 빨리 바인딩위해
			serverSocket.setReuseAddress(true);
			
			// 2.바인딩 
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress(); 	      //로컬 ip구함
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT)); //아이피랑 , 서버 포트지정함
			log("binding " + localhostAddress  + ":" + PORT);
			
			while(true) {
				// 3.accept
				Socket socket = serverSocket.accept(); //Blocking		
				
				// 4. 스레드 run
				Thread thread = new ChatServerThread(socket , listWriters); //넘겨준다 소켓
				thread.start();				
				// 5. 무한루프		
			}
			
		} catch (IOException e) {
			log("error : " + e);
		} finally {
			//자원정리
			try {
				if(serverSocket !=null && serverSocket.isClosed() == false) {	
					serverSocket.close();
				}
			} catch (IOException e) {
				log("error : " + e);
			}
			
		}
	}
 
	
	//로그처리함수
	public static void log( String log ) {
		System.out.println("[server : " + Thread.currentThread().getId() + "]" + log);
	}
}
