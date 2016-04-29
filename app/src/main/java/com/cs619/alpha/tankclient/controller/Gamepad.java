package com.cs619.alpha.tankclient.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.cs619.alpha.tankclient.Tank;
import com.cs619.alpha.tankclient.rest.BulletZoneRestClient;
import com.cs619.alpha.tankclient.util.BooleanWrapper;

/**
 * Created by Chris Oelerich on 4/13/16.
 */
public class Gamepad implements SensorEventListener/*, View.OnClickListener*/ {

  private static final String TAG = Gamepad.class.getSimpleName();
  private static final int SHAKE_THRESHOLD = 1200;

  private float[] v = new float[3];
  private float[] vOld = new float[3];
  private long lastUpdate;

  private BooleanWrapper bw;
  private Tank t;

  private BulletZoneRestClient restClient;

  public Gamepad(Tank tank, BulletZoneRestClient restClient, Context context) {

    SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    if (sensor != null) {
      sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    this.restClient = restClient;
    t = tank;

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
    if (t.getId() != -1) {
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
          fire(3);
          Log.d(TAG, "onSensorChanged: firing 3 at " + speed / SHAKE_THRESHOLD + "x speed");
        } else if (speed > 2 * SHAKE_THRESHOLD) {
          fire(2);
          Log.d(TAG, "onSensorChanged: firing 2 at " + speed / SHAKE_THRESHOLD + "x speed");
        } else if (speed > SHAKE_THRESHOLD) {
          fire(1);
          Log.d(TAG, "onSensorChanged: firing 1 at " + speed / SHAKE_THRESHOLD + "x speed");
        }

        vOld[0] = v[0];
        vOld[1] = v[1];
        vOld[2] = v[2];
      }
    }
  }

  /**
   * fire all phasers.
   *
   * @param i long
   */
  public void fire(int i) {
    if (t.getId() != -1) {
      try {
        restClient.fire(t.getId(), i);
      } catch (Exception e) {
        Log.e(TAG, "fire: ", e);
      }
    }
  }

  /**
   * move tank.
   */
  public void down() {
    if (t.getId() != -1) {
      try {
        if( t.getDir() == 4 )
          restClient.move(t.getId(), (byte) t.getDir());
        else if( t.getDir() == 0 ){
          restClient.move(t.getId(), (byte) t.getRevDir());
        }
        else{
          bw = restClient.turn(t.getId(), (byte) 4);
          if (bw.isResult()) {
            t.setDir(4);
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "move: ", e);
      }
    }
  }

  public void up() {
    if (t.getId() != -1) {
      try {
        if( t.getDir() == 0 )
          restClient.move(t.getId(), (byte) t.getDir());
        else if( t.getDir() == 4 ){
          restClient.move(t.getId(), (byte) t.getRevDir());
        }
        else{
          bw = restClient.turn(t.getId(), (byte) 0);
          if (bw.isResult()) {
            t.setDir(0);
          }
        }
        //restClient.move(t.getId(), (byte) t.getDir());
      } catch (Exception e) {
        Log.e(TAG, "move: ", e);
      }
    }
  }

  /**
   * turn tank.
   */
  public void left() {
    if (t.getId() != -1) {
      try {
        if( t.getDir() == 6 )
          restClient.move(t.getId(), (byte) t.getDir());
        else if( t.getDir() == 2 ){
          restClient.move(t.getId(), (byte) t.getRevDir());
        }
        else{
          bw = restClient.turn(t.getId(), (byte) 6);
          if (bw.isResult()) {
            t.setDir(6);
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "turn: ", e);
      }
    }
  }

  public void right() {
    if (t.getId() != -1) {
      try {
        if( t.getDir() == 2 )
          restClient.move(t.getId(), (byte) t.getDir());
        else if( t.getDir() == 6 ){
          restClient.move(t.getId(), (byte) t.getRevDir());
        }
        else{
          bw = restClient.turn(t.getId(), (byte) 2);
          if (bw.isResult()) {
            t.setDir(2);
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "turn: ", e);
      }
    }
  }
}
