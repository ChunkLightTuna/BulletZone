package com.cs619.alpha.tankclient.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.cs619.alpha.tankclient.R;
import com.cs619.alpha.tankclient.rest.PollerTask;

/**
 * Created by Chris Oelerich on 4/27/16.
 */
public class ReplayControls extends Fragment implements View.OnClickListener {
  private static final String TAG = ReplayControls.class.getSimpleName();
  private PollerTask pollerTask;
  private ImageButton playPauseButton;
  private SeekBar replaySeekBar;

  /**
   * Static fragment constructor
   *
   * @param pollertask PollerTask
   * @return ReplayControls
   */
  public static ReplayControls newInstance(PollerTask pollertask) {

    ReplayControls replayControls = new ReplayControls();
    replayControls.pollerTask = pollertask;
    pollertask.setController(replayControls);

    return replayControls;
  }

  /**
   * Make sure fragment always defaults to play arrow on view
   */
  @Override
  public void onResume() {
    super.onResume();
    playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
  }

  /**
   * Instantiation!
   *
   * @param inflater           LayoutInflater
   * @param container          ViewGroup
   * @param savedInstanceState Bundle
   * @return View
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.replay_control_view, container, false);

    (view.findViewById(R.id.faster)).setOnClickListener(this);
    (view.findViewById(R.id.slow)).setOnClickListener(this);

    replaySeekBar = (SeekBar) (view.findViewById(R.id.seekBar));
    replaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStopTrackingTouch() called with: " + "seekBar = [" + seekBar + "] with Progress" + seekBar.getProgress());

        pollerTask.setReplayPosition(seekBar.getProgress());
      }
    });

    playPauseButton = (ImageButton) view.findViewById(R.id.playPause);
    playPauseButton.setOnClickListener(this);

    return view;
  }

  /**
   * onClick listener for all the replay buttons
   *
   * @param v View
   */
  @Override
  public void onClick(View v) {


    Log.d(TAG, "onClick() called with: " + "v = [" + v + "]");

    switch (v.getId()) {
      case R.id.faster:
        pollerTask.setSpeed(pollerTask.getReplaySpeed() + 1);
        break;
      case R.id.slow:
        pollerTask.setSpeed(pollerTask.getReplaySpeed() - 1);
        break;
      case R.id.playPause:
        pollerTask.toggleReplayPaused();
        break;
    }
  }

  /**
   * Primarily for external non-UI tasks to toggle the appearance of the play/pause button
   *
   * @param b boolean
   */
  public void setPauseButtonOnUiThread(boolean b) {
    final boolean b1 = b;
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (b1) {
          playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        } else {
          playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
        }
      }
    });
  }

//  private Runnable updateSeekBar = new Runnable() {
//    public void run() {
//      long totalDuration = mediaPlayer.getDuration();
//      long currentDuration = mediaPlayer.getCurrentPosition();
//
//      // Displaying Total Duration time
//      remaining.setText(""+ milliSecondsToTimer(totalDuration-currentDuration));
//      // Displaying time completed playing
//      elapsed.setText(""+ milliSecondsToTimer(currentDuration));
//
//      // Updating progress bar
//      seekbar.setProgress((int)currentDuration);
//
//      // Call this thread again after 15 milliseconds => ~ 1000/60fps
//      seekHandler.postDelayed(this, 15);
//    }
//  };

}
