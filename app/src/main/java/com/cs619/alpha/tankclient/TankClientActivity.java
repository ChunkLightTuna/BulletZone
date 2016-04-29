package com.cs619.alpha.tankclient;

import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.cs619.alpha.tankclient.controller.Gamepad;
import com.cs619.alpha.tankclient.controller.Settings;
import com.cs619.alpha.tankclient.rest.BulletZoneRestClient;
import com.cs619.alpha.tankclient.rest.PollerTask;
import com.cs619.alpha.tankclient.ui.GridAdapter;
import com.cs619.alpha.tankclient.ui.PlayControls;
import com.cs619.alpha.tankclient.ui.ReplayControls;
import com.cs619.alpha.tankclient.util.BooleanWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Not a God Object we promise. Delegates tasks and provides the bulk of initialization.
 */
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
  private ReplayDatabase replayDatabase;

  public PlayControls playControls;
  public ReplayControls replayControls;

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


    bw = new BooleanWrapper();
    t = new Tank(-1);

    gamepad = new Gamepad(t, restClient, this);


    playControls = PlayControls.newInstance(gamepad);
    replayControls = ReplayControls.newInstance(gridPollTask);

    Log.wtf(TAG, "t created with " + t.getId());


    //inflate play fragment
    if (findViewById(R.id.control_container) != null) {
      if (savedInstanceState == null) {
        getSupportFragmentManager().beginTransaction().add(R.id.control_container, playControls).commit();

      }
    }
  }

  /**
   * facilitates Android Annotations hand waving
   */
  @AfterViews
  protected void afterViewInjection() {

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    Settings ssl = new Settings(this, restClient, t, playControls, replayControls, gridPollTask);
    navigationView.setNavigationItemSelectedListener(ssl);

    replayDatabase = new ReplayDatabase(this);

    mGridAdapter.setTank(t);

    gridPollTask.setAdapter(mGridAdapter);
    gridPollTask.setDatabase(replayDatabase);
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
      gridPollTask.doPoll(); // start polling the server
    } catch (Exception e) {

    }
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
  public void onStop() {
    super.onStop();
    if (t.getId() != -1 && gridPollTask.getPlayMode()) {
      try {
        restClient.leave(t.getId());
      } catch (HttpClientErrorException e) {
        Log.e(TAG, "onStop: ", e);
      }
    }

    replayDatabase.doneWriting(true);

  }
}
