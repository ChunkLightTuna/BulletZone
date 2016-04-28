package com.cs619.alpha.tankclient.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs619.alpha.tankclient.R;
import com.cs619.alpha.tankclient.controller.Gamepad;

/**
 * Created by Chris Oelerich on 4/27/16.
 */
public class PlayControls extends Fragment implements View.OnClickListener{
  private static final String TAG = PlayControls.class.getSimpleName();
  Gamepad gamepad;

  public static PlayControls newInstance(Gamepad gamepad) {

    PlayControls fragment = new PlayControls();

    fragment.gamepad = gamepad;
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.play_control_view, container, false);
    (view.findViewById(R.id.buttonFire1)).setOnClickListener(this);
    (view.findViewById(R.id.buttonFire2)).setOnClickListener(this);
    (view.findViewById(R.id.buttonFire3)).setOnClickListener(this);
    (view.findViewById(R.id.buttonLeft)).setOnClickListener(this);
    (view.findViewById(R.id.buttonRight)).setOnClickListener(this);
    (view.findViewById(R.id.buttonForward)).setOnClickListener(this);
    (view.findViewById(R.id.buttonBackward)).setOnClickListener(this);

    // Inflate the layout for this fragment
    return view;
  }

  @Override
  public void onClick(View v) {

    Log.d(TAG, "onClick() called with: " + "v = [" + v + "]");

    switch (v.getId()) {
      case R.id.buttonFire1:
        gamepad.fire(1);
        break;
      case R.id.buttonFire2:
        gamepad.fire(2);
        break;
      case R.id.buttonFire3:
        gamepad.fire(3);
        break;
      case R.id.buttonLeft:
        gamepad.turnL();
        break;
      case R.id.buttonRight:
        gamepad.turnR();
        break;
      case R.id.buttonForward:
        gamepad.moveFd();
        break;
      case R.id.buttonBackward:
        gamepad.moveBk();
        break;
    }
  }
}
