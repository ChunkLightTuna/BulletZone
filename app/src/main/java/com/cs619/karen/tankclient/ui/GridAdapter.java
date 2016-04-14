package com.cs619.karen.tankclient.ui;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cs619.karen.tankclient.R;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

@EBean
public class GridAdapter extends BaseAdapter {
  public static final String TAG = GridAdapter.class.getSimpleName();

  private final Object monitor = new Object();
  @SystemService
  protected LayoutInflater inflater;
  private int[][] mEntities = new int[16][16];
  private long tankId;

  public void updateList(int[][] entities) {
    synchronized (monitor) {
      this.mEntities = entities;
      this.notifyDataSetChanged();
    }
  }

  public void setTankId(long tankId) {
    this.tankId = tankId;
  }

  @Override
  public int getCount() {
    return 16 * 16;
  }

  @Override
  public Object getItem(int position) {
    return mEntities[position / 16][position % 16];
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {

    if (view == null) {
      view = inflater.inflate(R.layout.field_item, null);
      int iconDimen = parent.getContext().getResources().getDisplayMetrics().widthPixels / 16;
      view.setLayoutParams(new GridView.LayoutParams(iconDimen, iconDimen));
      ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);

    }

    int row = position / 16;
    int col = position % 16;

    int val = mEntities[row][col];

    boolean christLives = false;
    for (int[] i : mEntities) {
      for (int j : i) {
        if (tankId == (j / 10000) - (j / 10000000) * 1000) {
          christLives = true;
        }
      }
    }

    if (!christLives) {
      Toast.makeText(view.getContext(), "YOU DED", Toast.LENGTH_LONG).show();
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          System.exit(0);
        }
      }, 3000);
    }

//    If the value is 1TIDLIFX, then the ID of the tank is TID, it has LIF life and its direction is X.
//    (E.g., value = 12220071, tankId = 222, life = 007, direction = 2). Directions: {0 - UP, 2 - RIGHT, 4 - DOWN, 6 - LEFT}


    synchronized (monitor) {
      if (val > 0) {
        if (val == 1000) {
          ((ImageView) view).setImageResource(R.drawable.wall);

        } else if (val >= 2000000 && val <= 3000000) {
          ((ImageView) view).setImageResource(R.mipmap.bullet);

        } else if (val >= 10000000 && val <= 20000000) {
          int tankId, direction, up, right, down, left, life;

          direction = val % 10;

          life = (val % 1000) / 10;

          tankId = (val / 10000) - (val / 10000000) * 1000;


          if (this.tankId == tankId) {
            up = R.drawable.tank_up_blue;
            right = R.drawable.tank_right_blue;
            down = R.drawable.tank_down_blue;
            left = R.drawable.tank_left_blue;
          } else {
            up = R.drawable.tank_up;
            right = R.drawable.tank_right;
            down = R.drawable.tank_down;
            left = R.drawable.tank_left;
          }

          if (direction == 0) {
            ((ImageView) view).setImageResource(up);
          } else if (direction == 2) {
            ((ImageView) view).setImageResource(right);
          } else if (direction == 4) {
            ((ImageView) view).setImageResource(down);
          } else if (direction == 6) {
            ((ImageView) view).setImageResource(left);
          }

        }
      } else {
        ((ImageView) view).setImageDrawable(null);
      }

    }

    return view;
  }
}


