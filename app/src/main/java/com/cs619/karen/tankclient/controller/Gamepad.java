package com.cs619.karen.tankclient.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cs619.karen.tankclient.R;
import com.cs619.karen.tankclient.Tank;
import com.cs619.karen.tankclient.rest.BulletZoneRestClient;
import com.cs619.karen.tankclient.util.BooleanWrapper;

/**
 * Created by Chris Oelerich on 4/13/16.
 */
public class Gamepad implements SensorEventListener, View.OnClickListener {
  private static final String TAG = Gamepad.class.getSimpleName();
  private static final int SHAKE_THRESHOLD = 1200;

  float[] v = new float[3];
  float[] vOld = new float[3];
  long lastUpdate;
  Context context;

  BooleanWrapper bw;
  Tank t;

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.buttonFire) {

    } else if (v.getId() == R.id.buttonBackward) {

    } else if (v.getId() == R.id.buttonForward) {

    } else if (v.getId() == R.id.buttonLeft) {

    } else if (v.getId() == R.id.buttonRight) {

    }
  }

  private BulletZoneRestClient restClient;

  public Gamepad(long tankId, BulletZoneRestClient restClient, Context context) {

    SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    if (sensor != null) {
      sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    this.restClient = restClient;
    t = new Tank(tankId);

    this.context = context;

    final Button fireButton = (Button) ((Activity) context).findViewById(R.id.buttonFire);
    Log.wtf(TAG, "onClick()");

    if (fireButton == null) {
      Log.w(TAG, "onCLick() Gamepad: rip");
    }


    fireButton.setBackgroundColor(Color.BLUE);
    fireButton.setOnClickListener(this);

//    bw = restClientFinal.move(t.getId(), (byte) t.getDir());
//    bw = restClientFinal.turn(t.getId(), (byte) t.getRightDir());
//    bw = restClientFinal.move(t.getId(), (byte) t.getRevDir());
//    bw = restClientFinal.move(t.getId(), (byte) t.getLeftDir());
//    bw = restClientFinal.fire(t.getId());
  }

  @Override
  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
  }

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
        fire(t.getId());
      }
      vOld[0] = v[0];
      vOld[1] = v[1];
      vOld[2] = v[2];
    }
  }

  public void fire(long id) {
    try {
      restClient.fire(id);
    } catch (Exception e) {
      Log.e(TAG, "fire: ", e);
    }
  }

  public void move(long id, int dir) {
    try {
      restClient.move(id, (byte) dir);
    } catch (Exception e) {
      Log.e(TAG, "move: ", e);
    }
  }

  public void turn(long id, int dir) {
    try {
      restClient.turn(id, (byte) dir);
    } catch (Exception e) {
      Log.e(TAG, "turn: ", e);
    }
  }
}
