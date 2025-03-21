package com.example.hangman355;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorHandler implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Runnable onShake;

    public SensorHandler(SensorManager sensorManager, Runnable onShake) {
        this.sensorManager = sensorManager;
        this.onShake = onShake;
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (x > 10 || y > 10 || z > 10) {
                onShake.run();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void unregister() {
        sensorManager.unregisterListener(this);
    }
}