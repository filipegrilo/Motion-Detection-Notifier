import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	private final short PORT = 444;
	private MotionDetector detector;
	private boolean connected = false;
	
	public Server(MotionDetector detector) throws IOException{
		this.detector = detector;
		detector.setServer(this);
		
		ServerSocket serverSocket = null;
		
		try{
			serverSocket = new ServerSocket(PORT);
		}catch(IOException e){
			System.err.println("Could not listen on port " + PORT);
			System.exit(1);
		}
		
		System.out.println("Listening on port " + PORT);
		
		Socket clientSocket = null;
		
		try{
			clientSocket = serverSocket.accept();
		}catch(IOException e){
			System.err.println("Accept failed");
			System.exit(1);
		}
		
		connected = true;
		System.out.println("Connected!");
		
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		String inputLine, outputLine;
		
		while(connected){
			outputLine = "";
			
			if(detector.motionDetected.equals("yes")){
				detector.motionDetected = "no";
				outputLine = "motion detected";
			}
			
			if(!outputLine.equals("")) System.out.println(outputLine);
			out.println(outputLine);
			
			inputLine = in.readLine();
			if(!inputLine.equals("")) System.out.println(inputLine);
			
			if(inputLine.equals("bye")) connected = false;
		}
		
		out.close();
		in.close();
		clientSocket.close();
		serverSocket.close();
		System.exit(0);
	}
	
	public boolean isConnected(){
		return connected;
	}
}
