package com.cs619.alpha.bulletzone.rest;

/**
 * Created by karenjin on 10/21/15.
 */

import android.os.SystemClock;
import android.util.Log;

import com.cs619.alpha.bulletzone.model.ReplayDatabase;
import com.cs619.alpha.bulletzone.view.GridAdapter;
import com.cs619.alpha.bulletzone.view.ReplayControls;
import com.cs619.alpha.bulletzone.util.GridWrapper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import java.util.List;
import java.util.ListIterator;

/**
 * Continually makes requests to server for status updates. Supplies full state, not just diffs!
 * this class needs to be split in two!
 */
@EBean
public class PollerTask {
  private static final String TAG = "PollerTask";

  private boolean live;//true == live false == replay
  private boolean record = false; //Records to DB

  private int replaySpeed = 1;
  private boolean replayPaused = false;

  private GridAdapter adapter;
  private ReplayDatabase replayDatabase;
  private ReplayControls replayControls;

  private List<int[][]> replayGrid;
  private ListIterator<int[][]> griderator;

  @RestService
  BulletZoneRestClient restClient;

  public PollerTask() {
    live = true;
  }

  /**
   * Controls the boolean that switches between play and replay
   *
   * @param live boolean
   */
  public void togglePlayMode(boolean live) {
    Log.d(TAG, "togglePlayMode() called with: " + "live = [" + live + "]");
    if (this.live != live) {
      this.live = live;

      if (live) {
        doPoll();
      } else {
        stopRecording();
        replayPaused = true;
        playFromDatabase();
      }
    }
  }

  /**
   * Returns whether play mode is active or not.
   *
   * @return boolean
   */
  public boolean getPlayMode() {
    return live;
  }

  /**
   * poll server.
   */
  @Background(id = "grid_poller_task")
  public void doPoll() {
    while (live) {
      try {
        GridWrapper gridWrapper = restClient.grid();

        onGridUpdate(gridWrapper.getGrid());
        Log.v(TAG, "doPoll: " + record);
        if (record) {
          replayDatabase.addGrid(gridWrapper);
        }
      } catch (org.springframework.web.client.ResourceAccessException e) {
        Log.e(TAG, "doPoll: ", e);
      }
      // poll server every 100ms
      SystemClock.sleep(100);
    }
  }

  /**
   * replay from local database
   */
  @Background(id = "database_retrieval_task")
  public void playFromDatabase() {
    Log.d(TAG, "playFromDatabase() called");
    if (griderator == null || !griderator.hasNext()) {
      replayGrid = replayDatabase.readGrid();
      griderator = replayGrid.listIterator(0);
    }

    while (!replayPaused && griderator.hasNext()) {
      replayControls.setSeekerOnUiThread(100 * griderator.nextIndex() / replayGrid.size());
      onGridUpdate(griderator.next());
      SystemClock.sleep(100 / replaySpeed);
    }

    if (!griderator.hasNext() && replayControls != null) {

      replayPaused = true;
      replayControls.setPauseButtonOnUiThread(true);
    }
  }

  /**
   * seek to position.
   *
   * @param i int
   */
  public void setReplayPosition(int i) {
    int pos = Math.max(1, Math.min(i, 99)) * replayGrid.size() / 100;
    Log.d(TAG, "setReplayPosition() called with: " + "i = [" + i + "] pos is" + pos + "replayGrid.size() is " + replayGrid.size());

    boolean wasPaused = replayPaused;
    if (!replayPaused)
      replayPaused = true;

    griderator = replayGrid.listIterator(pos);
    if (!wasPaused) {
      replayPaused = false;
      playFromDatabase();
    }
  }

  /**
   * @param replaySpeed int
   */
  public void setSpeed(int replaySpeed) {
    this.replaySpeed = Math.max(1, Math.min(replaySpeed, 4));
  }

  /**
   * Toggles Pause in replay mode
   */
  public void toggleReplayPaused() {
    Log.d(TAG, "toggleReplayPaused() called");
    replayPaused = !replayPaused;
    if (!replayPaused)
      playFromDatabase();

    replayControls.setPauseButtonOnUiThread(replayPaused);
  }

  /**
   * Returns current replay speed in replay mode
   *
   * @return int
   */
  public int getReplaySpeed() {
    Log.d(TAG, "getReplaySpeed() returned: " + replaySpeed);
    return replaySpeed;
  }

  /**
   * Starts recording grid for replay mode
   */
  public void startRecording() {
    Log.d(TAG, "startRecording() called");
    replayDatabase.flush();
    this.record = true;
  }

  /**
   * Stops recording grid for replay mode
   */
  public void stopRecording() {
    Log.d(TAG, "stopRecording() called");
    replayDatabase.doneWriting(true);
    this.record = false;
  }

  /**
   * Attach grid adapter for UI hookin.
   *
   * @param adapter GridAdapter
   */
  public void setAdapter(GridAdapter adapter) {
    this.adapter = adapter;
  }

  /**
   * Attach database for reading from database.
   *
   * @param replayDatabase ReplayDatabase
   */
  public void setDatabase(ReplayDatabase replayDatabase) {
    this.replayDatabase = replayDatabase;
  }

  /**
   * Attach replay controls for controlling playback.
   *
   * @param replayControls ReplayControls
   */
  public void setController(ReplayControls replayControls) {
    this.replayControls = replayControls;
  }

  /**
   * Stay on UI thread as to avoid blocks.
   *
   * @param grid int[][]
   */
  @UiThread
  public void onGridUpdate(int[][] grid) {
    adapter.updateList(grid);
    //busProvider.getEventBus().post(new GridUpdateEvent(gw));
  }
}