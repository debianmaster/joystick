package com.authorwjf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity implements SensorEventListener {
	
	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
    private Socket socket;
    private PrintWriter out;
    private Sensor mOrientation;
    private Sensor mProximity;
    private float lastOri=0;
	private EditText host;
	private EditText port;
	private EditText usr;
	private EditText pwd;
	private Button btnStart;
	private Button btnStop;
	private ImageView imgUp;
	private ImageView imgDown;
	private Boolean isStarted=false;
    /** Called when the activity is first created. */
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main);                
        btnStart  = (Button) findViewById(R.id.btnStart);
        btnStop  = (Button) findViewById(R.id.btnStop);
        host = (EditText) findViewById(R.id.txtServer);
        port = (EditText) findViewById(R.id.txtPort);
        imgUp  = (ImageView) findViewById(R.id.imgUp);
        imgDown  = (ImageView) findViewById(R.id.imgDown);
        usr  = (EditText) findViewById(R.id.txtUser);
        pwd  = (EditText) findViewById(R.id.txtPwd);
        btnStart.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				try {		        	
					socket = new Socket(host.getText().toString(),Integer.parseInt(port.getText().toString()));
					isStarted=true;
					out = new PrintWriter(socket.getOutputStream(), true);
					out.print("{\"Action\":\"Auth\",\"User\":\""+ usr.getText().toString()+"\",\"Pwd\":\""+pwd.getText().toString()+"\"}");
					out.flush();
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
					usr.setVisibility(View.INVISIBLE);
					pwd.setVisibility(View.INVISIBLE);
					host.setVisibility(View.INVISIBLE);
					port.setVisibility(View.INVISIBLE);
					imgUp.setVisibility(View.VISIBLE);
					imgDown.setVisibility(View.VISIBLE);					
				} 
				catch (UnknownHostException e) {									
				} 
				catch (IOException e) {					
				}
			}
		});
        btnStop.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				try {
					isStarted=false;
					socket.close();
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
					usr.setVisibility(View.VISIBLE);
					pwd.setVisibility(View.VISIBLE);
					host.setVisibility(View.VISIBLE);
					port.setVisibility(View.VISIBLE);
					imgUp.setVisibility(View.INVISIBLE);
					imgDown.setVisibility(View.INVISIBLE);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
        imgUp.setOnClickListener(new View.OnClickListener() {
        	//@Override
        	public void onClick(View v) {        	  
        		out.print("{\"Action\":\"Speed\",\"Speed\":\"Inc\"}");
        		out.flush();
            }        
        });
        imgDown.setOnClickListener(new View.OnClickListener() {
     	   //@Override
     	   public void onClick(View v) {
     		  out.print("{\"Action\":\"Speed\",\"Speed\":\"Dec\"}");
     		 out.flush();
     	   }        
        });
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, mOrientation , SensorManager.SENSOR_ORIENTATION);
        btnStop.setEnabled(false);
        imgUp.setMinimumHeight(70);
		imgDown.setMinimumHeight(70);
        imgUp.setVisibility(View.INVISIBLE);
		imgDown.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("NewApi")
	protected void onResume() {
        super.onResume();
        try{
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_ORIENTATION);
        isStarted=true;
        }
        catch(Exception e){        
        }
    }

    @SuppressLint("NewApi")
	protected void onPause() {
        super.onPause();        
        try{
	        mSensorManager.unregisterListener(this);
			isStarted=false;
        }
        catch(Exception e){        
        }
    }

	@SuppressLint("NewApi")
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@SuppressLint("NewApi")
	@Override
	public void onSensorChanged(SensorEvent event) {		
		try{			
			float azimuth_angle = event.values[0];
		    float pitch_angle = event.values[1];
		    float roll_angle = event.values[2];
		    if(Math.abs((lastOri - azimuth_angle)) >= 1){
		    	//out.println("{azimuth:\""+ azimuth_angle + "\",pitch:\"" + pitch_angle +"\",roll:\"" + roll_angle+"\"}");
		    	//Log.e("app","{azimuth:\""+ Math.abs((lastOri - azimuth_angle)) + "\",pitch:\"" + pitch_angle +"\",roll:\"" + roll_angle+"\"}");		    	
		    	if(isStarted){
		    		//Log.e("azmith",String.valueOf(azimuth_angle));
		    		out.print("{\"Action\":\"Angle\",\"Angle\":\""+String.valueOf(azimuth_angle)+"\"}");
		    		out.flush();
		    	}
		    	lastOri = azimuth_angle;
		    }
		}
		catch(Exception e)
		{
			//Log.e("SOCK",e.getMessage());
		}
	}	
	
}