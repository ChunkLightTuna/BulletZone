package com.cs619.alpha.tankclient.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.cs619.alpha.tankclient.ui.PlayControls;

/**
 * Created by Chris Oelerich on 4/13/16.
 */
public class ShakeSensor implements SensorEventListener {

  private static final String TAG = ShakeSensor.class.getSimpleName();
  private static final int SHAKE_THRESHOLD = 1200;

  private float[] v = new float[3];
  private float[] vOld = new float[3];
  private long lastUpdate;
  private PlayControls playControls;

  public ShakeSensor(Context context, PlayControls playControls) {

    this.playControls = playControls;

    SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    if (sensor != null) {
      sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }
  }

  /**
   * Boilerplate for SensorEventListener
   *
   * @param sensor   Sensor
   * @param accuracy int
   */
  @Override
  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
  }

  /**
   * Boilerplate for SensorEventListener
   * logic for what to do whne things go ashaken lie here.
   *
   * @param event SensorEvent
   */
  @Override
  public final void onSensorChanged(SensorEvent event) {
    long curTime = System.currentTimeMillis();
    // only allow one update every 100ms.
    if ((curTime - lastUpdate) > 100) {
      long diffTime = (curTime - lastUpdate);
      lastUpdate = curTime;

      v[0] = event.values[0];
      v[1] = event.values[1];
      v[2] = event.values[2];

      float speed = Math.abs(v[0] + v[1] + v[0] - vOld[0] - vOld[1] - vOld[2]) / diffTime * 10000;

      if (speed > 3 * SHAKE_THRESHOLD) {
        playControls.fire(3);
        Log.d(TAG, "onSensorChanged: firing 3 at " + speed / SHAKE_THRESHOLD + "x speed");
      } else if (speed > 2 * SHAKE_THRESHOLD) {
        playControls.fire(2);
        Log.d(TAG, "onSensorChanged: firing 2 at " + speed / SHAKE_THRESHOLD + "x speed");
      } else if (speed > SHAKE_THRESHOLD) {
        playControls.fire(1);
        Log.d(TAG, "onSensorChanged: firing 1 at " + speed / SHAKE_THRESHOLD + "x speed");
      }

      vOld[0] = v[0];
      vOld[1] = v[1];
      vOld[2] = v[2];
    }
  }
}
