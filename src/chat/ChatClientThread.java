package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

public class ChatClientThread extends Thread {

	private Socket socket;
	
	public ChatClientThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
//		읽기----------------------------------------------------
		try {
			//주스트림꼽고
			InputStream is = socket.getInputStream();
			//보조스트림1(인풋스트림리더 )
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			//보조스트림2(버퍼리더)
			BufferedReader br = new BufferedReader(isr);
			
			String result = null;
			while(true) {
			
				result = br.readLine();
				
				if(result == null) {  //받는게없으면 
					break;					
				}
				
				System.out.println(result);
			}
				
		} catch (IOException e){
			e.printStackTrace();
		}finally {
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
