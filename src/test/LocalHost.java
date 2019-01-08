package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
		
			InetAddress	inetAddress = InetAddress.getLocalHost();
		
			String hostname = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
				
			System.out.println(hostname + ":" + hostAddress);
			
			byte[] addresses = inetAddress.getAddress();
			for(byte address : addresses) {
				System.out.print(address & 0x000000ff); //마스킹
				System.out.print(".");
			}
			
		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}	
