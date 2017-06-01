import java.io.IOException;
import java.util.Scanner;

public class Main{
	static MotionDetector detector;
	
	public static void main(String[] args) throws IOException{
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					detector = new MotionDetector();
					Server server = new Server(detector);
				} catch (IOException e) {
					System.err.println("Server start failed.");
					System.exit(1);
				}
			}{
			
		}}).start();
		
		System.in.read();
	}
}
