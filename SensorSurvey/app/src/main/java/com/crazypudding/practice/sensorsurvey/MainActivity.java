package com.crazypudding.practice.sensorsurvey;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    private SensorManager mSensorManager;

    // Individual light and proximity sensor.
    private Sensor mSensorProximity;
    private Sensor mSensorLight;

    // TextView to display current sensor values.
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get instance of the sensorManager.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mTextSensorLight = findViewById(R.id.label_light);
        mTextSensorProximity = findViewById(R.id.label_proximity);

        // Get instance of the default light and proximity sensor.
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        String sensor_error = getResources().getString(R.string.error_no_sensor);

        if (mSensorLight == null) {
            mTextSensorLight.setText(sensor_error);
        }

        if (mSensorProximity == null) {
            mTextSensorProximity.setText(sensor_error);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mSensorProximity != null) {
            mSensorManager.registerListener(this, mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorLight != null) {
            mSensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    private int getRGBValue() {
        return new Random().nextInt(255);
    }

    // Change the windowBackground when the difference exceeds 10.
    float oldLight = 0;
    private void changeWindowBackground(float light) {
        if (Math.abs(light - oldLight) > 10) {
            getWindow().getDecorView().setBackgroundColor(Color.rgb(getRGBValue(), getRGBValue(), getRGBValue()));
        }
        oldLight = light;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get the sensor type.
        int sensorType = event.sensor.getType();

        // Get the sensor value. Depending on the sensor type,
        // the values array may contain a single piece of data or a multidimensional array full of data.
        float currentValue = event.values[0];

        switch (sensorType) {
            // Event came from the light sensor.
            case Sensor.TYPE_LIGHT:
                mTextSensorLight.setText(getResources().getString(R.string.label_light, currentValue));
                changeWindowBackground(currentValue);
                break;
            // Event came from the proximity sensor.
            case Sensor.TYPE_PROXIMITY:
                mTextSensorProximity.setText(getResources().getString(R.string.label_proximity, currentValue));
            default:
                // do nothing
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
