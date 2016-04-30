package com.cs619.alpha.bulletzone.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cs619.alpha.bulletzone.R;
import com.cs619.alpha.bulletzone.model.Tank;
import com.cs619.alpha.bulletzone.model.TankClientActivity;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

/**
 * Populates UI based on gamestate.
 */
@EBean
public class GridAdapter extends BaseAdapter {
  public static final String TAG = GridAdapter.class.getSimpleName();

  private final Object monitor = new Object();
  private Context context;

  private int lastTankX = -1;
  private int lastTankY = -1;

  @SystemService
  protected LayoutInflater inflater;
  private int[][] mEntities = new int[16][16];
  private Tank tank;

  public void updateList(int[][] entities) {
    synchronized (monitor) {
      this.mEntities = entities;
      this.notifyDataSetChanged();
    }
  }

  public void setContext(Context context) {
    this.context = context;
  }

  /**
   * set tank id.
   *
   * @param tank Tank
   */
  public void setTank(Tank tank) {
    this.tank = tank;
  }

  /**
   * get number of slots on board. Used for adapter pattern.
   *
   * @return int
   */
  @Override
  public int getCount() {
    return 16 * 16;
  }

  /**
   * get item based on position. Used for adapter pattern.
   *
   * @param position int
   * @return Object
   */
  @Override
  public Object getItem(int position) {
    return mEntities[position / 16][position % 16];
  }

  /**
   * get itemid based on position.
   *
   * @param position int
   * @return long
   */
  @Override
  public long getItemId(int position) {
    return position;
  }

  /**
   * Get specific view based on position and context.
   *
   * @param position int
   * @param view     View
   * @param parent   ViewGroup
   * @return View
   */
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

//    checkPulse();

//    If the value is 1TIDLIFX, then the ID of the tank is TID, it has LIF life and its direction is X.
//    (E.g., value = 12220071, tankId = 222, life = 007, direction = 2). Directions: {0 - UP, 2 - RIGHT, 4 - DOWN, 6 - LEFT}
    synchronized (monitor) {

      ((TankClientActivity) context).updateHP(tank.getHealth());

      if (val > 0) {
        if (val == 1000) {
          ((ImageView) view).setImageResource(R.drawable.wall);
        } else if (val == 1500) {
          ((ImageView) view).setImageResource(R.drawable.destructable_wall);
        } else if (val >= 2000000 && val <= 3000000) {
          int dmg = ((val % 1000) - (val % 10)) / 10;
          if (dmg == 10) {
            ((ImageView) view).setImageResource(R.drawable.bullet1);
          } else if (dmg == 30) {
//              ((ImageView) view).setImageResource(R.drawable.bullet2);
          } else {
//              ((ImageView) view).setImageResource(R.drawable.bullet3);
          }
        } else if (val >= 10000000 && val <= 20000000) {
          int tankId, direction, up, right, down, left, life;
          direction = val % 10;

          life = ((val % 10000) - (val % 10)) / 10;
          tankId = (val / 10000) - (val / 10000000) * 1000;

          if (tank.getId() == tankId) {
            tank.setHealth(life);

            lastTankX = col;
            lastTankY = row;

            ((TankClientActivity) context).updateHP(life);

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
//    }

    return view;
  }

  /**
   * game deletes tank b4 life is reported at zero. need a way to check if we're still kickin
   */
  private void checkPulse() {
    boolean christLives = false;
    if (tank.getId() != -1) {


//      lastTankX;
//      lastTankY;


      for (int[] i : mEntities) {
        for (int j : i) {
          if (tank.getId() == (j / 10000) - (j / 10000000) * 1000) {
            christLives = true;
          }
        }
      }
      if (!christLives) {
        tank.setId(-1);
        ((TankClientActivity) context).updateHP(0);
      }
    }
  }
}


