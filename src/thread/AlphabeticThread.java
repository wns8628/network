package thread;

public class AlphabeticThread extends Thread {

	@Override
	public void run() {
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
