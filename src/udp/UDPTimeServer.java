package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPTimeServer {
	
	public static final int PORT = 6000;
	public static final int BUFFER_SIZE = 1024;
		
	public static void main(String[] args) {
		
		DatagramSocket socket = null;
				
		try {
			//1. socket생성
			socket = new DatagramSocket(PORT);
			

			while(true) {
				//2.데이터수신 
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);		
				socket.receive(receivePacket);
				//3.데이터 전송
				
				
				
				SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS a");
				String data = format.format( new Date() );
				
				byte[] sendData = data.getBytes("UTF-8");
				receivePacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(),receivePacket.getPort());
				socket.send(receivePacket);						
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null && socket.isClosed() ==false) {
				socket.close();
			}
		}
			
		
	}

}
