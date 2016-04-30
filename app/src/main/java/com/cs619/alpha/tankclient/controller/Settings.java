package com.cs619.alpha.tankclient.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import com.cs619.alpha.tankclient.R;
import com.cs619.alpha.tankclient.model.Tank;
import com.cs619.alpha.tankclient.model.TankClientActivity;
import com.cs619.alpha.tankclient.rest.BulletZoneRestClient;
import com.cs619.alpha.tankclient.rest.PollerTask;
import com.cs619.alpha.tankclient.ui.PlayControls;
import com.cs619.alpha.tankclient.ui.ReplayControls;

import org.springframework.web.client.HttpClientErrorException;

/**
 * Created by Chris Oelerich on 4/26/16.
 */
public class Settings
    implements NavigationView.OnNavigationItemSelectedListener {

  private final static String TAG = Settings.class.getSimpleName();
  private Context context;
  private BulletZoneRestClient restClient;
  private Tank tank;
  private PlayControls playControls;
  private ReplayControls replayControls;
  private PollerTask poller;

  public Settings(Context context, BulletZoneRestClient restClient, Tank tank,
                  PlayControls playControls, ReplayControls replayControls, PollerTask poller) {
    this.context = context;
    this.restClient = restClient;
    this.tank = tank;
    this.poller = poller;

    this.playControls = playControls;
    this.replayControls = replayControls;
  }

  /**
   * This slide out menu allows the user to toggle b/w play and replay modes as well as joining and
   * leaving the game.
   *
   * @param item MenuItem
   * @return boolean
   */
  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    Log.w(TAG, "onNavigationItemSelected: " + item.getTitle());

    switch (item.getItemId()) {

      case R.id.mode_play:
        ((TankClientActivity) context).getSupportFragmentManager().beginTransaction()
            .replace(R.id.control_container, playControls).commit();
        poller.togglePlayMode(true);
        break;

      case R.id.mode_replay:
        ((TankClientActivity) context).getSupportFragmentManager().beginTransaction()
            .replace(R.id.control_container, replayControls).commit();
        poller.togglePlayMode(false);
        break;

      case R.id.game_join:
        tank.setId(restClient.join().getResult());
        poller.startRecording();
        Log.d(TAG, "tankId is " + tank.getId());
        break;

      case R.id.game_leave:
        if (tank.getId() != -1)
          try {
            restClient.leave(tank.getId());
          } catch (HttpClientErrorException e) {
            Log.e(TAG, "onNavigationItemSelected: ", e);
          }
        poller.stopRecording();
        break;

      case R.id.exit:
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
        break;
    }

    DrawerLayout drawer = (DrawerLayout) ((Activity) context).getWindow().getDecorView()
        .findViewById(R.id.drawer_layout);

    if (item.getItemId() != R.id.mode_play) {
      drawer.closeDrawer(GravityCompat.START);
    }

    return true;
  }

}
