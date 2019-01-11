package chat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import echo.EchoServer;

public class ChatServerThread extends Thread {

	private Socket socket;
	private String nickname;
	private List<Writer> listWriters;
	PrintWriter pw;
	
	public ChatServerThread(Socket socket , List<Writer> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}
		
	@Override
	public void run() {
		//클라이언트 정보얻기
		InetSocketAddress inetRemoteSocketAddress =	(InetSocketAddress)socket.getRemoteSocketAddress(); //리턴이 다른거라 다운캐스팅해야함 
		String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress(); //ip얻은거
		int remotePort = inetRemoteSocketAddress.getPort();
		EchoServer.log("connected by client[" + remoteHostAddress +":" + remotePort + "]" );		
		
		try {
		//-빨떄꼽기----------------------------------------------------------
		 //읽기 br
			//주스트림꼽고
			InputStream is = socket.getInputStream();
			//보조스트림1(인풋스트림리더 )
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			//보조스트림2(버퍼리더)
			BufferedReader br = new BufferedReader(isr);
		//쓰기 pw	
			//주스트림
			OutputStream os = socket.getOutputStream();	 
			//보조스트림1
			OutputStreamWriter osr = new OutputStreamWriter(os,"UTF-8");
			//보조스트림2
			pw = new PrintWriter(osr,true);	
		//----------------------------------------------------------------
			
			//3. 요청 처리 			
			while( true ) {
				//읽었는데 없으면 끊김
			   String request  = br.readLine();
			   System.out.println("대기중");
			   
			   if( request == null ) {
				   EchoServer.log( "클라이언트로 부터 연결 끊김" );
			      break;
			   }
			   
			   //있으면 프로토콜분석
			   System.out.println("request : " + request);
			   String[] tokens = request.split( ":" );			   
			   for(String s : tokens) {
				   System.out.println(s);						   
			    }
				if( "join".equals( tokens[0] ) ) { 		 		 //조인 프로토콜을 받으면
					   doJoin( tokens[1], pw );					 //응답해줘야지 ok pw추가해줘야지 한명들어오니
				} 
				else if( "quit".equals( tokens[1] ) ) {    //quit프로토콜을 받으면
					   doQuit(pw);
				} 
				else if( "message".equals( tokens[0] ) ){  //message프로토콜을 받으면 
					   doMessage( tokens[1] );
				} else {
					ChatServer.log( "에러:알수 없는 요청(" + tokens[0] + ")" );
				}
			}	
				
		} catch (SocketException e) {
			EchoServer.log("abnormal close : "+ e );
		} catch (IOException e) {		
			EchoServer.log("error :"+ e );	
		} finally {
			try {
				//7.자원정리
				if(socket != null && socket.isClosed() == false) {
					socket.close(); //서버쪽소켓이라 따로 닫아줘야함
				}
			} catch(IOException e) {
				EchoServer.log("error :"+ e );		
			}
		}	
	}
	
	//-------------------------------------------------------브로드캐스트 메소드
	private void broadcast( String data ) { //모든 pw에 다보낸다 메세지를 ㅇㅋ ? 
		   synchronized( listWriters ) {
			   for( Writer writer : listWriters ) {
   				   ((PrintWriter) writer).println(data);
		      }
		   }
	}
	//-------------------------------------------------------pw쓰는 메소드 

	private void addWriter(Writer pw) {
	   synchronized( listWriters ) {
		  listWriters.add( pw );
	   }
	}	
	//------------------------------------------------------처리 메소드
	
	//채팅방 접속
	private void doJoin(String nickName, Writer writer) {  
		//1.닉네임설정하고
		this.nickname = nickName;  
				
		//2.브로드캐스트
		String data = nickName + "님이 참여하셨습니다.";
		broadcast(data); 		    	
		
		//3. pw추가
		addWriter( writer );		      	
		
		//4. ack 
		((PrintWriter) writer).println("join:ok");
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void doQuit(Writer writer) {
		removeWriter( writer );
	   String data = nickname + "님이 퇴장 하였습니다."; 
	   broadcast( data );
	}
	private void removeWriter(Writer writer) {
		listWriters.remove(writer);
	}

	private void doMessage(String msg) {		
		broadcast( nickname + "님: "+ msg);				
	}
}
