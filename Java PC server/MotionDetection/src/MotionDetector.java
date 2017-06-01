import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;


public class MotionDetector implements WebcamMotionListener{
	public String motionDetected = "no";
	private Server server;
	
	public MotionDetector(){
		WebcamMotionDetector detector = new WebcamMotionDetector(Webcam.getDefault());
		detector.setInterval(500);
		detector.addMotionListener(this);
		detector.start();
	}

	public void motionDetected(WebcamMotionEvent arg0) {
		if(server != null && server.isConnected()) motionDetected = "yes";
	}
	
	public void setServer(Server server){
		this.server = server;
	}
}
