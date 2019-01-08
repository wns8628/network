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
			System.out.println("[server] binding " + localhostAddress  + ":" + PORT);
			
//			while(true) {

				// 3.accept
				Socket socket = serverSocket.accept(); //Blocking		
				
				// 4. 스레드 run
//			}
			
			//클라이언트 정보얻기
			InetSocketAddress inetRemoteSocketAddress =	(InetSocketAddress)socket.getRemoteSocketAddress(); //리턴이 다른거라 다운캐스팅해야함 
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress(); //ip얻은거
			int remotePort = inetRemoteSocketAddress.getPort();
			System.out.println("connected by client[" + remoteHostAddress +":" + remotePort + "]");
	
			
		try {		
//			-빨떄꼽기----------------------------------------------------------
			 
			 //읽기? 
				//주스트림꼽고
				InputStream is = socket.getInputStream();
				//보조스트림1(인풋스트림리더 )
				InputStreamReader isr = new InputStreamReader(is,"UTF-8");
				//보조스트림2(버퍼리더)
				BufferedReader br = new BufferedReader(isr);
			//쓰기?	
				//주스트림
				OutputStream os = socket.getOutputStream();	 
				//보조스트림1
				OutputStreamWriter osr = new OutputStreamWriter(os,"UTF-8");
				//보조스트림2
				PrintWriter pw = new PrintWriter(osr,true);	
//----------------------------------------------------------------
				
			while(true){

				String data = br.readLine(); //받기 
				if( data == null ) {
					System.out.println("[server] closed by client");
					break;
				}				
				pw.println(data); //에코           //보낸다.
			}
			
		} catch(SocketException e) {
			System.out.println("[server] abnormal close");
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//7.자원정리
				if(socket != null && socket.isClosed() == false) {
					socket.close(); //서버쪽소켓이라 따로 닫아줘야함
				}
			} catch(IOException e) {
				e.printStackTrace();
			 }
    	   }					
		} catch (IOException e) {
			e.printStackTrace();			
		} finally {
			//자원정리 
			try {
				if(serverSocket !=null && serverSocket.isClosed()== false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
