package com.example.filipe.motiondetection;


import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private boolean connected = false;
    private String inputLine, outputLine;

    public Client(String ip, short port){


        try{
            Socket clientSocket = new Socket(ip, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            connected = true;
            MainActivity.text = "connected";

            while(connected){
                inputLine = in.readLine();
                outputLine = MainActivity.outMessage;

                if(inputLine.equals("motion detected")) MainActivity.text = "Motion Detected";

                out.println(outputLine);

                if(outputLine.equals("bye")) connected = false;
                if(!outputLine.equals("")) MainActivity.outMessage = "";
            }

            in.close();
            clientSocket.close();
            System.exit(0);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
