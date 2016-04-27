package com.cs619.alpha.tankclient;

import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.cs619.alpha.tankclient.controller.Gamepad;
import com.cs619.alpha.tankclient.rest.BulletZoneRestClient;
import com.cs619.alpha.tankclient.rest.PollerTask;
import com.cs619.alpha.tankclient.ui.GridAdapter;
import com.cs619.alpha.tankclient.util.BooleanWrapper;

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
public class TankClientActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

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


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
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


  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      // Handle the camera action
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }


}
