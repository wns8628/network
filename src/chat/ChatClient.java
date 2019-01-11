package chat;

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

import echo.EchoServerReceiveThread;

public class ChatClient {
	
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
			
			//쓰기--------------------------------------------------
			//주스트림
			OutputStream os = socket.getOutputStream();	//내보낸다 
			//보조스트림1
			OutputStreamWriter osr = new OutputStreamWriter(os,"UTF-8");
			//보조스트림2
			PrintWriter pw = new PrintWriter(osr, true);
			//-----------------------------------------------------

			System.out.print("닉네임>>");			
			Scanner scan = new Scanner(System.in);
			String joindata = "join" + ":" + scan.nextLine();
			pw.println(joindata); //닉네임 보내기

			//닉네임보내고 읽기전용 스레드생성
			Thread thread = new ChatClientThread(socket);
			thread.start();	
						
			while(true) {	
				
				String msg = scan.nextLine();
				if(msg.equals("")) {
					msg =" ";
				}
				
				String msgdata = "message" + ":" + msg;		
				
				
				pw.println(msgdata);
			}
		
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null && socket.isClosed() ==false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
