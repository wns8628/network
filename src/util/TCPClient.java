package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {

	private static final String SERVER_IP = "218.39.221.93";
	private static final int SERVER_PORT = 5000;
	
	public static void main(String[] args) {

		Socket socket = null;
		try {			
			//1. 소켓 생성
			socket = new Socket();
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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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
