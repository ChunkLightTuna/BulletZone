package com.cs619.karen.tankclient;

import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.cs619.karen.tankclient.controller.Gamepad;
import com.cs619.karen.tankclient.rest.BulletZoneRestClient;
import com.cs619.karen.tankclient.rest.PollerTask;
import com.cs619.karen.tankclient.ui.GridAdapter;
import com.cs619.karen.tankclient.util.BooleanWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

@EActivity(R.layout.activity_main)
public class TankClientActivity extends AppCompatActivity {

  private static final String TAG = "TankClientActivity";
  Gamepad gamepad;


  @Bean
  protected GridAdapter mGridAdapter;

  @ViewById
  protected GridView gridView;


  @RestService
  BulletZoneRestClient restClient;

  @Bean
  PollerTask gridPollTask;

  private BooleanWrapper bw;
  private Tank t;
  private long tankId = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    gamepad = new Gamepad(tankId, restClient, this);


    bw = new BooleanWrapper();
    t = new Tank(tankId);
    Log.wtf(TAG, "t created with " + tankId);
  }

  @AfterViews
  protected void afterViewInjection() {
    mGridAdapter.setTankId(tankId);
    gridPollTask.setAdapter(mGridAdapter);
    joinAsync();
    SystemClock.sleep(500);

    gridView.setAdapter(mGridAdapter);
  }

  @Background
  void joinAsync() {
    try {
      tankId = restClient.join().getResult();

      t.setId(tankId);
      Log.d(TAG, "tankId is " + tankId);
      gridPollTask.doPoll(); // start polling the server
    } catch (Exception e) {

    }
  }

  public void moveForward(View v) {
    gamepad.move( t.getId(), t.getDir() );
  }

  public void moveBk(View v) {
    gamepad.move( t.getId(), t.getRevDir() );

  }

  public void turnL(View v) {
    gamepad.turn( t.getId(), t.getLeftDir() );
    t.setDir(t.getLeftDir());
  }

  public void turnR(View v) {;
    gamepad.turn( t.getId(), t.getRightDir() );
    t.setDir(t.getRightDir());
  }

  public void fire(View v) {
    gamepad.fire( t.getId() );
  }
}
