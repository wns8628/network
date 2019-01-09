package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestHandler extends Thread {
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );
					
			// get IOStream
			OutputStream outputStream = socket.getOutputStream();  //문자열,이미지이런걸 걍 바이트로보내는게편함
			
																   //읽을때는 스트링으로 읽으니 버퍼드리더로 읽어오면되겠지
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));

			String request = null;
			while(true) {
				String line = br.readLine();
				
				//브라우저가 연결을 끊으면
				if(line == null) { 
					break;
				}
				//Header만 읽음
				if("".equals(line)) {
					break;
				}
				//헤더의 첫번째 라인만처리
				if( request == null) {
					request = line;
				}
			}
			
			consoleLog(request);
			String[] tokens = request.split(" ");
			if("GET".equals(tokens[0])) {
				responesStaticResource(outputStream, tokens[1], tokens[2]);
			} else { //POST, DELETE, PUT, ETC 명령 즉 get아닌건 다 이걸로처리하게해라 일단.
				/*
				 HTTP/1.0 400 BAD Request\r\n
				 error/400.html 에러
				 */
				responesError(outputStream, tokens[2], 400);  //(outputStream, tokens[1], tokens[2]);
			}
		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}

	//url이 들어오면 get방식으로 ex) GET /index.html HTTP/1.0----------
	private void responesStaticResource
		(OutputStream outputStream, String url, String protocol) throws IOException {
		
		if("/".equals(url)) {
			url="/index.html";
		}
		
		File file = new File("./webapp"+ url);
		
		if(file.exists() == false) {
			responesError(outputStream, protocol, 404);
			/*
			 HTTP/1.0 404 File Not Found\r\n 
			 Content-Type:text/html; charset=utf-8\r\n
			  */
			return;
		}
		
		// nio 뉴 입출력 
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
				
		//응답
		outputStream.write( (protocol+" 200 OK\r\n").getBytes( "UTF-8" ));
		outputStream.write( ("Content-Type:" + contentType +"; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() );
		outputStream.write(body);
	};
	//----------------------------------------------------------------
	
	//400,404 Error 처리
	private void responesError(OutputStream outputStream ,String protocol, int errorcode) throws IOException{
		File file = new File("./webapp/error/"+ errorcode + ".html");
		//404에러처리
		if(file.exists() == false) {
			responesError(outputStream, protocol, 404);
			return;
		} 
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType(file.toPath());
		
		//응답
		outputStream.write( (protocol + " "+ errorcode + " bad error\r\n").getBytes( "UTF-8" ));
		outputStream.write( ("Content-Type:" + contentType +"; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() );
		outputStream.write(body);
	}
	
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}
