package thread;

public class MultithreadEx01 {

	public static void main(String[] args) {

		Thread digitThread = new DigitThread();
		digitThread.start();
		
		for(char c = 'a'; c <='z'; c++) {
			System.out.print(c); //메인도스레드니 이렇게가능 아디
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
