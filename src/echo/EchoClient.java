package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

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
	
//			쓰기--------------------------------------------------
			//주스트림
			OutputStream os = socket.getOutputStream();	//내보낸다 
			//보조스트림1
			OutputStreamWriter osr = new OutputStreamWriter(os,"UTF-8");
			//보조스트림2
			PrintWriter pw = new PrintWriter(osr, true);	
			
//			읽기----------------------------------------------------
			//주스트림꼽고
			InputStream is = socket.getInputStream();
			//보조스트림1(인풋스트림리더 )
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			//보조스트림2(버퍼리더)
			BufferedReader br = new BufferedReader(isr);	
			
//			-------------------------------------------------------
			while(true) {					
				Scanner scan = new Scanner(System.in);
				System.out.print(">>");
				String data = scan.nextLine();
				
				if(data.equals("exit")) {
					break;
				}
				
				pw.println(data); 				   //보내기
				String recive = br.readLine(); 	   //받기 
				
				if( recive == null ){
					System.out.println("[client] closed by server");
					break;
				}						
				System.out.println("<<" + recive); //받은거 출력 				
			}
//			--------------------------------------------------------		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null && socket.isClosed() ==false) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
