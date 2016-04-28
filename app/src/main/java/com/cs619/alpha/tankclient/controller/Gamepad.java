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
  //  private Context context;
//
  private BooleanWrapper bw;
  private Tank tank;

  private BulletZoneRestClient restClient;

//  /**
//   * Unused as yet
//   * @param v View
//   */
//  @Override
//  public void onClick(View v) {
//    if (v.getId() == R.id.buttonFire) {
//
//    } else if (v.getId() == R.id.buttonBackward) {
//
//    } else if (v.getId() == R.id.buttonForward) {
//
//    } else if (v.getId() == R.id.buttonLeft) {
//
//    } else if (v.getId() == R.id.buttonRight) {
//
//    }
//  }

  public Gamepad(Tank tank, BulletZoneRestClient restClient, Context context) {

    SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    if (sensor != null) {
      sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    this.restClient = restClient;
    this.tank = tank;

//    this.context = context;
//
//    final Button fireButton = (Button) ((Activity) context).findViewById(R.id.buttonFire);
//    Log.wtf(TAG, "onClick()");
//
//    if (fireButton == null) {
//      Log.w(TAG, "onCLick() Gamepad: rip");
//    }
//
//    fireButton.setBackgroundColor(Color.BLUE);
//    fireButton.setOnClickListener(this);

//    bw = restClientFinal.move(tank.getId(), (byte) tank.getDir());
//    bw = restClientFinal.turn(tank.getId(), (byte) tank.getRightDir());
//    bw = restClientFinal.move(tank.getId(), (byte) tank.getRevDir());
//    bw = restClientFinal.move(tank.getId(), (byte) tank.getLeftDir());
//    bw = restClientFinal.fire(tank.getId());
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

      if (speed > SHAKE_THRESHOLD) {
        Log.wtf("sensor", "shake detected w/ speed: " + speed);
        fire(1);
      }
      vOld[0] = v[0];
      vOld[1] = v[1];
      vOld[2] = v[2];
    }
  }

  /**
   * fire all phasers.
   *
   * @param i long
   */
  public void fire(int i) {
    try {
      restClient.fire(tank.getId(), i);
    } catch (Exception e) {
      Log.e(TAG, "fire: ", e);
    }
  }

  /**
   * move tank.
   *
   */
  public void moveBk(){//(long id, int dir) {
    try {
      restClient.move(tank.getId(), (byte) tank.getRevDir());
    } catch (Exception e) {
      Log.e(TAG, "move: ", e);
    }
  }

  public void moveFd(){//(long id, int dir) {
    try {
      restClient.move(tank.getId(), (byte) tank.getDir() );
    } catch (Exception e) {
      Log.e(TAG, "move: ", e);
    }
  }

  /**
   * turn tank.
   *
   */
  public void turnL(){//(long id, int dir) {
    try {
      bw = restClient.turn(tank.getId(), (byte) tank.getLeftDir() );
      if( bw.isResult() ){
        tank.setDir(tank.getLeftDir() );
      }
    } catch (Exception e) {
      Log.e(TAG, "turn: ", e);
    }
  }

  public void turnR(){//(long id, int dir) {
    try {
      bw = restClient.turn(tank.getId(), (byte) tank.getRightDir() );
      if( bw.isResult() ){
        tank.setDir(tank.getRightDir() );
      }
    } catch (Exception e) {
      Log.e(TAG, "turn: ", e);
    }
  }
}
