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


    //inflate fragment
    if (findViewById(R.id.control_container) != null) {
      if (savedInstanceState == null) {
//        PlayControls pc = new PlayControls();
//
//        getSupportFragmentManager().beginTransaction().add(R.id.control_container, pc).commit();

      }
    }

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
      gridPollTask.doPoll(); // start polling the server
    } catch (Exception e) {

    }
  }

    /**
     * provides hook for gamepad move forward.
     * Would have attached listener from Gamepad, but was running into issueproblems.
     *
     */
    @Click({R.id.buttonForward})
    public void moveForward() {
        gamepad.move(t.getId(), t.getDir());
    }

    /**
     * provides hook for gamepad move back.
     * Would have attached listener from Gamepad, but was running into issueproblems.
     *
     */
    @Click({R.id.buttonBackward})
    public void moveBk() {
        gamepad.move(t.getId(), t.getRevDir());
    }

    /**
     * provides hook for gamepad move left.
     * Would have attached listener from Gamepad, but was running into issueproblems.
     *
     */
    @Click({R.id.buttonLeft})
    public void turnL() {
        gamepad.turn(t.getId(), t.getLeftDir());
        t.setDir(t.getLeftDir());
    }

    /**
     * provides hook for gamepad turn right.
     * Would have attached listener from Gamepad, but was running into issueproblems.
     *
     */
    @Click({R.id.buttonRight})
    public void turnR() {
        gamepad.turn(t.getId(), t.getRightDir());
        t.setDir(t.getRightDir());
    }

    /**
     * provides hook for gamepad fire.
     * Would have attached listener from Gamepad, but was running into issueproblems.
     *
     */
    @Click({R.id.buttonFire1})
    public void fireOne() {
        gamepad.fire(t.getId(), 1);
    }

    /**
     * provides hook for gamepad fire.
     * Would have attached listener from Gamepad, but was running into issueproblems.
     *
     * @param v View
     */
    @Click({R.id.buttonFire2})
    public void fireTwo() {
        gamepad.fire(t.getId(), 2);
    }

    /**
     * provides hook for gamepad fire.
     * Would have attached listener from Gamepad, but was running into issueproblems.
     *
     * @param v View
     */
    @Click({R.id.buttonFire3})
    public void fireThree() {
        gamepad.fire(t.getId(), 3);
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
}
