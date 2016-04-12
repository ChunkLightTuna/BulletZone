package com.cs619.karen.tankclient.rest;

/**
 * Created by karenjin on 10/21/15.
 */

import android.os.SystemClock;
import android.util.Log;

import com.cs619.karen.tankclient.ui.GridAdapter;
import com.cs619.karen.tankclient.util.GridWrapper;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

@EBean
public class PollerTask {
    private static final String TAG = "GridPollerTask";


    private GridAdapter adapter;

    @RestService
    BulletZoneRestClient restClient;

    @Background(id = "grid_poller_task")
    public void doPoll() {
        while (true) {
            onGridUpdate(restClient.grid());
            // poll server every 100ms
            SystemClock.sleep(100);
        }
    }

    public void setAdapter(GridAdapter adapter) {
        this.adapter = adapter;
    }

    @UiThread
    public void onGridUpdate(GridWrapper gw) {
        Log.d(TAG, "grid at timestamp: " + gw.getTimeStamp());

        adapter.updateList(gw.getGrid());

        //busProvider.getEventBus().post(new GridUpdateEvent(gw));
    }
}
