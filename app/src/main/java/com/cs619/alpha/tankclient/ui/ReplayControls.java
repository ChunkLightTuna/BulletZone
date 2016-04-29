package com.cs619.alpha.tankclient.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cs619.alpha.tankclient.R;
import com.cs619.alpha.tankclient.rest.PollerTask;

/**
 * Created by Chris Oelerich on 4/27/16.
 */
public class ReplayControls extends Fragment implements View.OnClickListener {
  private static final String TAG = ReplayControls.class.getSimpleName();
  PollerTask pollerTask;

  public static ReplayControls newInstance(PollerTask pollertask) {

    ReplayControls fragment = new ReplayControls();
    fragment.pollerTask = pollertask;

    return fragment;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    View view = inflater.inflate(R.layout.replay_control_view, container, false);
    (view.findViewById(R.id.faster)).setOnClickListener(this);
    (view.findViewById(R.id.slow)).setOnClickListener(this);
    (view.findViewById(R.id.playPause)).setOnClickListener(this);

    return view;
  }

  @Override
  public void onClick(View v) {


    Log.d(TAG, "onClick() called with: " + "v = [" + v + "]");

    switch (v.getId()) {
      case R.id.faster:
        pollerTask.setSpeed(Math.min(pollerTask.getReplaySpeed() + 1, 4));
        break;
      case R.id.slow:
        pollerTask.setSpeed(Math.max(1, pollerTask.getReplaySpeed() - 1));
        break;
      case R.id.playPause:
        pollerTask.toggleReplayPaused();

        if (pollerTask.getReplayPaused())
          ((ImageButton) v).setImageResource(R.drawable.ic_pause_black_24dp);
        else
          ((ImageButton) v).setImageResource(R.drawable.ic_play_arrow_black_24dp);
        break;
    }
  }
}
