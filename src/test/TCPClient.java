package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {

	private static final String SERVER_IP = "218.39.221.93";
	private static final int SERVER_PORT = 5000;
	
	public static void main(String[] args) {

		Socket socket = null;
		try {			
			//1. 소켓 생성
			socket = new Socket();
			
			//1-1 socket buffer size 확인
			int receiveBufferSize = socket.getReceiveBufferSize();
			int sendBufferSize = socket.getSendBufferSize();			
			System.out.println(receiveBufferSize+":"+sendBufferSize);
			
			
			//1-2 socket buffer size 변경
			socket.setReceiveBufferSize(1024 *10);
			socket.setSendBufferSize(1024 *10);
			
			// socket buffer size 사이즈 변경확인
			receiveBufferSize = socket.getReceiveBufferSize();
			sendBufferSize = socket.getSendBufferSize();			
			System.out.println(receiveBufferSize+":"+sendBufferSize);
			
			//1-3 SO_NODELAY 
			socket.setTcpNoDelay(true);//딜레이가없게하겠다 - Nagle off지
			
			//1-4. SO_TIMEOUT
			socket.setSoTimeout(2000); //타임아웃 얼마나줄거냐
			
			//2.서버 연결
			socket.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT));
			System.out.println("[client] server connected");
			
			//3.IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			//4.쓰기 
			String data = "Hello World\n";
			os.write(data.getBytes("UTF-8"));
			
			//5.읽기
			byte[] buffer = new byte[255];
			int readByteCount = is.read(buffer);//Blocking
			
			if(readByteCount == -1) {
				System.out.println("[client] closed by server");
				return;
			}
			
			data = new String(buffer,0,readByteCount,"UTF-8");//받는데로 리턴
			System.out.println("[client]"+ data);
			
		} catch (SocketTimeoutException ex) {
			System.out.println("[client] time out");
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			try {
				if(socket !=null && socket.isClosed()==false) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
