package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Scanner scan = new Scanner(System.in);
				System.out.print(">");
				String line = scan.nextLine();
				
				if("exit".equals(line)) {
					break;
				}
				
				InetAddress[] inetAddresses = InetAddress.getAllByName(line);								
				for(InetAddress inet :inetAddresses) {
					System.out.print(inet.getHostName()+" : " + inet.getHostAddress() + "\n");
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  	    }	
    }
}
