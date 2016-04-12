package com.cs619.karen.tankclient;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.cs619.karen.tankclient.rest.BulletZoneRestClient;
import com.cs619.karen.tankclient.rest.PollerTask;
import com.cs619.karen.tankclient.ui.GridAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

@EActivity(R.layout.activity_main)
public class TankClientActivity extends AppCompatActivity {

    private static final String TAG = "TankClientActivity";


    @Bean
    protected GridAdapter mGridAdapter;

    @ViewById
    protected GridView gridView;


    @RestService
    BulletZoneRestClient restClient;

    @Bean
    PollerTask gridPollTask;

    private long tankId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @AfterViews
    protected void afterViewInjection() {
        gridPollTask.setAdapter(mGridAdapter);
        joinAsync();
        SystemClock.sleep(500);
        gridView.setAdapter(mGridAdapter);
    }

    @Background
    void joinAsync() {
        try {
            tankId = restClient.join().getResult();

            Log.d(TAG, "tankId is " + tankId);
            gridPollTask.doPoll(); // start polling the server
        } catch (Exception e) {

        }
    }
}
