package com.cs619.alpha.tankclient.rest;

/**
 * Created by karenjin on 10/21/15.
 */

import android.os.SystemClock;
import android.util.Log;

import com.cs619.alpha.tankclient.ReplayDatabase;
import com.cs619.alpha.tankclient.ui.GridAdapter;
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
 * <p/>
 * this class needs to be split in two!
 */
@EBean
public class PollerTask {
  private static final String TAG = "GridPollerTask";

  private boolean live;//true == live false == replay

  private int replaySpeed = 1;
  private boolean replayPaused = false;

  private GridAdapter adapter;
  private ReplayDatabase replayDatabase;
  private List<int[][]> replayGrid;
  private ListIterator<int[][]> gridIterator;

  @RestService
  BulletZoneRestClient restClient;

  public PollerTask() {
    live = true;
  }

  public void setMode(boolean live) {

    if (this.live != live) {
      this.live = live;

      if (live) {
        doPoll();
      } else {
        playFromDatabase();
      }
    }
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
        replayDatabase.addGrid(gridWrapper);

        Log.v(TAG, "doPoll() called with: " + gridWrapper);
      } catch (org.springframework.web.client.ResourceAccessException e) {
        Log.e(TAG, "doPoll: ", e);
      }
      // poll server every 100ms
      SystemClock.sleep(100);
    }
  }

  @Background(id = "database_retrieval_task")
  public void playFromDatabase() {
    if (gridIterator == null || !gridIterator.hasNext())
      gridIterator = replayGrid.listIterator(0);

    while (!replayPaused && gridIterator.hasNext()) {
      onGridUpdate(gridIterator.next());
      SystemClock.sleep(100 / replaySpeed);
    }

  }

  public void setSpeed(int replaySpeed) {
    this.replaySpeed = replaySpeed;
    if (replayPaused)
      toggleReplayPaused();
  }

  public void toggleReplayPaused() {
    replayPaused = !replayPaused;
    if (!replayPaused)
      playFromDatabase();
  }

  public boolean getReplayPaused() {
    return replayPaused;
  }

  public int getReplaySpeed() {
    return replaySpeed;
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


  /**
   * Stay on UI thread as to avoid blocks.
   * @param grid int[][]
   */
  @UiThread
  public void onGridUpdate(int[][] grid) {
    adapter.updateList(grid);
    //busProvider.getEventBus().post(new GridUpdateEvent(gw));
  }
}
