package com.cs619.alpha.bulletzone.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs619.alpha.bulletzone.R;
import com.cs619.alpha.bulletzone.model.Tank;
import com.cs619.alpha.bulletzone.rest.BulletZoneRestClient;
import com.cs619.alpha.bulletzone.util.BooleanWrapper;

/**
 * Created by Chris Oelerich on 4/27/16.
 */
public class PlayControls extends Fragment implements View.OnClickListener {
  private static final String TAG = PlayControls.class.getSimpleName();
  private BulletZoneRestClient restClient;
  private Tank t;
  private BooleanWrapper bw;

  /**
   * Static fragment constructor
   * @param bulletZoneRestClient BulletZoneRestClient
   * @param tank Tank
   * @return PlayControls
   */
  public static PlayControls newInstance(BulletZoneRestClient bulletZoneRestClient, Tank tank) {

    PlayControls fragment = new PlayControls();
    fragment.restClient = bulletZoneRestClient;
    fragment.t = tank;

    return fragment;
  }

  /**
   * Inflate fragment view
   * @param inflater LayoutInflater
   * @param container ViewGroup
   * @param savedInstanceState Bundle
   * @return View
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.play_control_view, container, false);

    (view.findViewById(R.id.buttonFire1)).setOnClickListener(this);
    (view.findViewById(R.id.buttonFire2)).setOnClickListener(this);
    (view.findViewById(R.id.buttonFire3)).setOnClickListener(this);
    (view.findViewById(R.id.buttonLeft)).setOnClickListener(this);
    (view.findViewById(R.id.buttonRight)).setOnClickListener(this);
    (view.findViewById(R.id.buttonUp)).setOnClickListener(this);
    (view.findViewById(R.id.buttonDown)).setOnClickListener(this);

    // Inflate the layout for this fragment
    return view;
  }

  /**
   * onClick listeners for all buttons under play control view.
   * @param v View
   */
  @Override
  public void onClick(View v) {

    Log.d(TAG, "onClick() called with: " + "v = [" + v + "]");

    switch (v.getId()) {
      case R.id.buttonFire1:
        fire(1);
        break;
      case R.id.buttonFire2:
        fire(2);
        break;
      case R.id.buttonFire3:
        fire(3);
        break;
      case R.id.buttonLeft:
        left();
        break;
      case R.id.buttonRight:
        right();
        break;
      case R.id.buttonUp:
        up();
        break;
      case R.id.buttonDown:
        down();
        break;
    }
  }

  /**
   * fire all phasers. pew pew!!
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
   * Turn/move tank down.
   */
  private void down() {
    if (t.getId() != -1) {
      try {
        if (t.getDir() == 4)
          restClient.move(t.getId(), (byte) t.getDir());
        else if (t.getDir() == 0) {
          restClient.move(t.getId(), (byte) t.getRevDir());
        } else {
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

  /**
   * Turn/move tank up.
   */
  private void up() {
    if (t.getId() != -1) {
      try {
        if (t.getDir() == 0)
          restClient.move(t.getId(), (byte) t.getDir());
        else if (t.getDir() == 4) {
          restClient.move(t.getId(), (byte) t.getRevDir());
        } else {
          bw = restClient.turn(t.getId(), (byte) 0);
          if (bw.isResult()) {
            t.setDir(0);
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "move: ", e);
      }
    }
  }

  /**
   * Turn/move tank left.
   */
  private void left() {
    if (t.getId() != -1) {
      try {
        if (t.getDir() == 6)
          restClient.move(t.getId(), (byte) t.getDir());
        else if (t.getDir() == 2) {
          restClient.move(t.getId(), (byte) t.getRevDir());
        } else {
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

  /**
   * Turn/move tank right.
   */
  private void right() {
    if (t.getId() != -1) {
      try {
        if (t.getDir() == 2)
          restClient.move(t.getId(), (byte) t.getDir());
        else if (t.getDir() == 6) {
          restClient.move(t.getId(), (byte) t.getRevDir());
        } else {
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
