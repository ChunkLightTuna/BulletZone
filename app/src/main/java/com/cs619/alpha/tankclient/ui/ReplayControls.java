package com.cs619.alpha.tankclient.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs619.alpha.tankclient.R;

/**
 * Created by Chris Oelerich on 4/27/16.
 */
public class ReplayControls extends Fragment implements View.OnClickListener{

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.replay_control_view, container, false);
  }

  @Override
  public void onClick(View v) {
  }
}
