package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	private static final int PORT = 5000;
	public static void main(String[] args) {

		ServerSocket serverSocket = null;
		try {
			// 1.서버 소켓생성
			serverSocket = new ServerSocket();
			
			// 2.Binding : Socket에 SocketAddress(IPAddress + port) 바인딩 한다.
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localhostAddress = inetAddress.getHostAddress(); 	      //로컬 ip구함
			serverSocket.bind(new InetSocketAddress(localhostAddress, PORT)); //아이피랑 , 서버 포트지정함
			System.out.println("[server] binding " + localhostAddress  + ":" + PORT);
			
			
			// 3.accept (클라이언트 부터 연결요청을 기다린다.)
			Socket socket = serverSocket.accept(); //Blocking  풀리면 소켓이생겨서 들어가겟지			
			System.out.println("[server] conneted by client");
			
			//클라이언트 정보얻기
			InetSocketAddress inetRemoteSocketAddress =
					(InetSocketAddress)socket.getRemoteSocketAddress(); //리턴이 다른거라 다운캐스팅해야함 
			//System.out.println(inetRemoteSocketAddress.getAddress()); //반환값이 InetAddress객체임 
			//System.out.println(inetRemoteSocketAddress.getAddress().getHostAddress());  //그걸 스트리응로뽑아내			
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress(); //ip얻은거
			int remotePort = inetRemoteSocketAddress.getPort();
			System.out.println("connected by client[" + remoteHostAddress +":" + remotePort + "]");
			
			try {					
				//4. IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
	
				//5. 데이터 읽기
				while(true){
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); //Blocking
					if( readByteCount == -1) {
						// 정상종료 : remote socket이 close()
						// 메소드를 통해서 정상적으로 종료
						System.out.println("[server] closed by client");
						break;
					}			
					
					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[server] received: " + data);
										
					//6.데이터 쓰기
					os.write(data.getBytes("UTF-8"));//에코	
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
