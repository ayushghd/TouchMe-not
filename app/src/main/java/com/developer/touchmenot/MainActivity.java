package com.developer.touchmenot;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkButtonBuilder;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SparkButton b;
    SeekBar sb;
    private SensorManager sensorMan;
    private Sensor accelerometer;
    private int flag=0, flag2=0;
    private float[] mGravity;
    private float mAccel;
    private float sensitivity;

    private float mAccelCurrent;
    private float mAccelLast;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b  = findViewById(R.id.button);
        sb = findViewById(R.id.seekBar);
        sensitivity = sb.getProgress()*0.1f;
        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sensitivity=progress*0.1f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag2=0;
                if(flag==0) {
                    b.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            flag = 1;
                            b.setChecked(true);
                        }

                    }, 5000);
                }
                else
                {
                    mediaPlayer.stop();
                    flag=0;
                    b.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener( this);
            mediaPlayer.stop();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(flag==1) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity = event.values.clone();
                // Shake detection
                float x = mGravity[0];
                float y = mGravity[1];
                float z = mGravity[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;
                // Make this higher or lower according to how much
                // motion you want to detect
                if (mAccel > sensitivity && flag2==0) {
                    flag2=1;
                    Log.e("Hila Hila",""+sensitivity);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarmsound);
                    mediaPlayer.start();
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }
}
