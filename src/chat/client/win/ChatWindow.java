package chat.client.win;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import chat.ChatClientThread;

public class ChatWindow {
	
	private Socket socket;
	private Frame frame;
	private Panel pannel;
	private Button buttonSend;
	private TextField textField;
	private TextArea textArea;

	public ChatWindow(String name, Socket socket) {
		
		frame = new Frame(name);
		pannel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);
		
		this.socket = socket;		
		new ChatClientReceiveThread(socket).start();

	}

	public void show() {
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent actionEvent ) {
				sendMessage();
			}
		});

		// Textfield
		textField.setColumns(80);

		// Pannel
		pannel.setBackground(Color.LIGHT_GRAY);
		pannel.add(textField);
		pannel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, pannel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.pack();
	}
	
	private void sendMessage() {
	
		try {		

			//쓰기--------------------------------------------------
			//주스트림
			OutputStream os;
			os = socket.getOutputStream();//내보낸다 
			//보조스트림1
			OutputStreamWriter osr = new OutputStreamWriter(os,"UTF-8");
			//보조스트림2
			PrintWriter pw = new PrintWriter(osr, true);
			//-----------------------------------------------------

		    String msg = textField.getText();
			if(msg.equals("")) {
				msg =" ";
			}
			String msgdata = "message" + ":" + msg;						
			pw.println(msgdata);

		
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
//	--------------------------------------------------------
	class ChatClientReceiveThread extends Thread{
		private Socket socket;
		
		public ChatClientReceiveThread(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
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
				  
		          textArea.append(result);
		          textArea.append("\n");
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
	
//	-----------------------------------------------------------
}
