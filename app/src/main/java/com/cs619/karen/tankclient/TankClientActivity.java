package com.cs619.karen.tankclient;

import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

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

/**
 * Not a God Object we promise. Delegates tasks and provides the bulk of initialization.
 */
@EActivity(R.layout.activity_main)
public class TankClientActivity extends AppCompatActivity {

  private static final String TAG = "TankClientActivity";
  Gamepad gamepad;

  String[] drawerItems;
  DrawerLayout drawerLayout;
  ListView drawerList;

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

  /**
   * Google android lifecycle yo.
   *
   * @param savedInstanceState Bundle
   */
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



    drawerItems = getResources().getStringArray(R.array.nav_drawer_items);
    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawerList = (ListView) findViewById(R.id.drawer_list);

  }

  /**
   * facilitates Android Annotations hand waving
   */
  @AfterViews
  protected void afterViewInjection() {
    mGridAdapter.setTankId(tankId);
    gridPollTask.setAdapter(mGridAdapter);
    joinAsync();
    SystemClock.sleep(500);

    gridView.setAdapter(mGridAdapter);
  }

  /**
   * handles non-UI tasks ie poller
   */
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

  /**
   * provides hook for gamepad move forward.
   * Would have attached listener from Gamepad, but was running into issueproblems.
   *
   * @param v View
   */
  public void moveForward(View v) {
    gamepad.move(t.getId(), t.getDir());
  }

  /**
   * provides hook for gamepad move back.
   * Would have attached listener from Gamepad, but was running into issueproblems.
   *
   * @param v View
   */
  public void moveBk(View v) {
    gamepad.move(t.getId(), t.getRevDir());
  }

  /**
   * provides hook for gamepad move left.
   * Would have attached listener from Gamepad, but was running into issueproblems.
   *
   * @param v View
   */
  public void turnL(View v) {
    gamepad.turn(t.getId(), t.getLeftDir());
    t.setDir(t.getLeftDir());
  }

  /**
   * provides hook for gamepad turn right.
   * Would have attached listener from Gamepad, but was running into issueproblems.
   *
   * @param v View
   */
  public void turnR(View v) {
    gamepad.turn(t.getId(), t.getRightDir());
    t.setDir(t.getRightDir());
  }

  /**
   * provides hook for gamepad fire.
   * Would have attached listener from Gamepad, but was running into issueproblems.
   *
   * @param v View
   */
  public void fire(View v) {
    gamepad.fire(t.getId());
  }
}
