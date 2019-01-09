package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer {
	
	private static final int PORT = 5000;	
	public static void main(String[] args) {

		ServerSocket serverSocket = null;				
		try {
			// 1.서버 소켓생성
			serverSocket = new ServerSocket();
					
			// 2.바인딩 
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress(); 	      //로컬 ip구함
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT)); //아이피랑 , 서버 포트지정함
//			System.out.println("[server] binding " + localhostAddress  + ":" + PORT);
			log("binding " + localhostAddress  + ":" + PORT);
			
			while(true) {
				// 3.accept
				Socket socket = serverSocket.accept(); //Blocking		
				
				// 4. 스레드 run
				Thread thread = new EchoServerReceiveThread(socket); //넘겨준다 소켓
				thread.start();				
				// 5.수행후 다시 무한루프
			}
			
							
		} catch (IOException e) {
			log("error :"+ e );		
		} finally {
			//자원정리 
			try {
				if(serverSocket !=null && serverSocket.isClosed()== false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				log("error :"+ e );		
			}
		}
	}
	
	public static void log( String log ) {
		System.out.println("[server : " + Thread.currentThread().getId() + "]" + log);
	}
}
