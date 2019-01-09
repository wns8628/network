package thread;

public class UppercaseAlphabeticRunnableimpl extends UppercaseAlphabetic implements Runnable {

	@Override
	public void run() {
		print();           //걍메소드가 추가된거지 스레드 클래스가아님
	}

}
