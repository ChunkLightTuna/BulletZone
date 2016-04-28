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
import com.cs619.alpha.tankclient.Tank;
import com.cs619.alpha.tankclient.TankClientActivity;
import com.cs619.alpha.tankclient.rest.BulletZoneRestClient;
import com.cs619.alpha.tankclient.ui.PlayControls;
import com.cs619.alpha.tankclient.ui.ReplayControls;

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

  public Settings(Context context, BulletZoneRestClient restClient, Tank tank) {
    this.context = context;
    this.restClient = restClient;
    this.tank = tank;

    playControls = new PlayControls();
    replayControls = new ReplayControls();
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    Log.w(TAG, "onNavigationItemSelected: " + item.getTitle());
    int id = item.getItemId();

    if (id == R.id.mode_play) {

      ((TankClientActivity) context).getSupportFragmentManager().beginTransaction()
          .replace(R.id.control_container, playControls).commit();

    } else if (id == R.id.mode_replay) {

      ((TankClientActivity) context).getSupportFragmentManager().beginTransaction()
          .replace(R.id.control_container, replayControls).commit();

    } else if (id == R.id.game_join) {
      tank.setId(restClient.join().getResult());

      Log.d(TAG, "tankId is " + tank.getId());

//    } else if (id == R.id.game_quit) {
//      if (tank.getId() != -1)
//        restClient.leave(tank.getId());

    } else if (id == R.id.exit) {
      Intent startMain = new Intent(Intent.ACTION_MAIN);
      startMain.addCategory(Intent.CATEGORY_HOME);
      startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(startMain);
    }

    DrawerLayout drawer = (DrawerLayout) ((Activity) context).getWindow().getDecorView()
        .findViewById(R.id.drawer_layout);

    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

}
