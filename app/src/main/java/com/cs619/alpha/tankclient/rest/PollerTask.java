package com.cs619.alpha.tankclient.rest;

/**
 * Created by karenjin on 10/21/15.
 */

import android.os.SystemClock;
import android.util.Log;

import com.cs619.alpha.tankclient.ReplayDatabase;
import com.cs619.alpha.tankclient.ui.GridAdapter;
import com.cs619.alpha.tankclient.ui.ReplayControls;
import com.cs619.alpha.tankclient.util.GridWrapper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import java.util.List;
import java.util.ListIterator;

/**
 * Continually makes requests to server for status updates. Supplies full state, not just diffs!
 * <p/>
 * <p/>
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

  public void togglePlayMode(boolean live) {
    Log.d(TAG, "togglePlayMode() called with: " + "live = [" + live + "]");
    if (this.live != live) {
      this.live = live;

      if (live) {
        doPoll();
      } else {
        stopRecording();
        playFromDatabase();
      }
    }
  }

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
    Log.d(TAG, "playFromDatabase() called with: " + "");
    if (griderator == null || !griderator.hasNext()) {
      replayGrid = replayDatabase.readGrid();
      griderator = replayGrid.listIterator(0);
    }

    while (!replayPaused && griderator.hasNext()) {
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

  public void setSpeed(int replaySpeed) {
    this.replaySpeed = Math.max(1, Math.min(replaySpeed, 4));
  }

  public void toggleReplayPaused() {
    Log.d(TAG, "toggleReplayPaused() called with: " + "");
    replayPaused = !replayPaused;
    if (!replayPaused)
      playFromDatabase();

    replayControls.setPauseButtonOnUiThread(replayPaused);
  }

  public int getReplaySpeed() {
    Log.d(TAG, "getReplaySpeed() called with: " + "");
    return replaySpeed;
  }

  public void startRecording() {
    Log.d(TAG, "startRecording() called with: " + "");
    replayDatabase.flush();
    this.record = true;
  }

  public void stopRecording() {
    Log.d(TAG, "stopRecording() called with: " + "");
    replayDatabase.doneWriting(true);
    this.record = false;
  }


  /**
   * attach adapter to grid for UI hookin.
   *
   * @param adapter GridAdapter
   */
  public void setAdapter(GridAdapter adapter) {
    this.adapter = adapter;
  }

  public void setDatabase(ReplayDatabase replayDatabase) {
    this.replayDatabase = replayDatabase;
  }

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
