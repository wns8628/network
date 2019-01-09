package thread;

public class MultithreadEx03 {

	public static void main(String[] args) {
		Thread thread1 = new AlphabeticThread();
		Thread thread2 = new DigitThread();
//		Thread thread3 = new UppercaseAlphabeticRunnableimpl();//이건 스레드클래스가아니지 
		Thread thread3 = new Thread(new UppercaseAlphabeticRunnableimpl());//이렇게
			
//		Runnable runnable = new UppercaseAlphabeticRunnableimpl();
//		new Thread(runnable).start(); 이렇게하던지
		
		thread1.start();
		thread2.start();
		thread3.start();
			
	}

}
