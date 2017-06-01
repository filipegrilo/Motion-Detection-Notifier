package com.example.filipe.motiondetection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {
    private String ip = "192.168.2.104";
    private short port = 444;

    public static String text = "Offline";
    public static String outMessage = "";
    private boolean connectButton = true;
    private boolean alarm = false;

    private EditText tfIP, tfPORT;
    private TextView tvInfo;
    private RelativeLayout layout;
    private Vibrator vibrator;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tfIP = (EditText)findViewById(R.id.editText);
        tfPORT = (EditText)findViewById(R.id.editText2);
        tvInfo = (TextView) findViewById(R.id.textView);
        layout = (RelativeLayout) findViewById(R.id.layout);
        final Button button = (Button) findViewById(R.id.button);
        stopButton = (Button) findViewById(R.id.button2);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        tfIP.setText(ip);
        tfPORT.setText(""+port);
        stopButton.setVisibility(View.INVISIBLE);

        new Thread(new Runnable() {
            public void run() {
                while(true){
                    if(text.equals("Motion Detected")) startAlarm();
                    else if(text.equals("connected")) changeInfoMessage("Connected");

                    text = "";
                }
        }}).start();

        button.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){


                        if(connectButton){
                            connectButton = false;
                            button.setText("Disconnect");

                            new Thread(new Runnable() {
                                public void run() {
                                    ip = tfIP.getText().toString();
                                    port = Short.parseShort(tfPORT.getText().toString());

                                    new Client(ip, port);
                                }
                            }).start();
                        }else{
                            connectButton = true;
                            button.setText("Connect");

                            outMessage = "bye";
                        }
                    }
                }
        );

        stopButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        stopAlarm();
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeInfoMessage(final String message){
        runOnUiThread(new Runnable() {
            public void run() {
                tvInfo.setText(message);
            }
        });
    }

    private void startAlarm(){
        if(!alarm){
            alarm = true;

            new Thread(new Runnable(){
                public void run(){
                    boolean isRed = false;

                    runOnUiThread(new Runnable() {
                        public void run() {
                            tvInfo.setText("Motion Detected");
                            stopButton.setVisibility(View.VISIBLE);
                        }
                    });

                    while(alarm){
                        isRed = !isRed;

                        try{
                            if(isRed){
                                vibrator.vibrate(500);

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        layout.setBackgroundColor(Color.RED);
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        layout.setBackgroundColor(Color.WHITE);
                                    }
                                });
                            }

                            Thread.sleep(500);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
        }
    }

    private void stopAlarm(){
        alarm = false;

        runOnUiThread(new Runnable() {
            public void run() {
                stopButton.setVisibility(View.INVISIBLE);
                layout.setBackgroundColor(Color.WHITE);
                tvInfo.setText("Connected");
            }
        });
    }
}
