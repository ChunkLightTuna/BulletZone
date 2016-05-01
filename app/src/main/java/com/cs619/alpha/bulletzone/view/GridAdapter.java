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
  public static final int WALL_ID = 1000;
  public static final int DESTRUCTIBLE_WALL_ID = 1500;
  public static final int BULLET_ID_LOWER_BOUND = 2000000;
  public static final int BULLET_ID_UPPER_BOUND = 3000000;
  public static final int TANK_ID_LOWER_BOUND = 10000000;
  public static final int TANK_ID_UPPER_BOUND = 20000000;
  public static final int UP = 0;
  public static final int RIGHT = 2;
  public static final int DOWN = 4;
  public static final int LEFT = 6;

  private final Object monitor = new Object();
  private Context context;

  @SystemService
  protected LayoutInflater inflater;
  private int[][] mEntities = new int[16][16];
  private Tank tank;

  /**
   * Passes the grid from pollertask
   *
   * @param entities
   */
  public void updateList(int[][] entities) {
    synchronized (monitor) {
      this.mEntities = entities;
      this.notifyDataSetChanged();

      checkPulse();
      ((TankClientActivity) context).updateHP(tank.getHealth());
    }
  }

  /**
   * Passes the context to the class
   *
   * @param context
   */
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
      if (val > 0) {
        if (isWall(val)) {
          ((ImageView) view).setImageResource(R.drawable.wall);

        } else if (isDestructibleWall(val)) {
          ((ImageView) view).setImageResource(R.drawable.destructible_wall);

        } else if (isBullet(val)) {
          int dmg = decodeDmg(val);

          if (dmg == 10) {
            ((ImageView) view).setImageResource(R.drawable.bullet1);
          } else if (dmg == 30) {
//              ((ImageView) view).setImageResource(R.drawable.bullet2);
          } else {
//              ((ImageView) view).setImageResource(R.drawable.bullet3);
          }

        } else if (isTank(val)) {
          int direction, up, right, down, left, life;

          direction = val % 10;

          life = ((val % 10000) - (val % 10)) / 10;

          if (tank.getId() == decodeTankId(val)) {
            tank.setHealth(life);

            tank.setLastCol(col);
            tank.setLastRow(row);
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

          if (direction == UP) {
            ((ImageView) view).setImageResource(up);
          } else if (direction == RIGHT) {
            ((ImageView) view).setImageResource(right);
          } else if (direction == DOWN) {
            ((ImageView) view).setImageResource(down);
          } else if (direction == LEFT) {
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
   *
   * @param val int
   * @return boolean
   */
  private boolean isWall(int val) {
    return val == WALL_ID;
  }

  /**
   *
   * @param val int
   * @return boolean
   */
  private boolean isDestructibleWall(int val) {
    return val == DESTRUCTIBLE_WALL_ID;
  }

  /**
   *
   * @param val int
   * @return boolean
   */
  private boolean isBullet(int val) {
    return val >= BULLET_ID_LOWER_BOUND && val <= BULLET_ID_UPPER_BOUND;
  }

  /**
   *
   * @param val int
   * @return boolean
   */
  private boolean isTank(int val) {
    return val >= TANK_ID_LOWER_BOUND && val <= TANK_ID_UPPER_BOUND;
  }

  private int decodeDmg(int val) {
    return ((val % 1000) - (val % 10)) / 10;
  }
  /**
   *
   * @param val int
   * @return boolean
   */
  private int decodeTankId(int val) {
    if (val < 10000000 || val > 20000000) {
      return -1;
    } else {
      return (val / 10000) - (val / 10000000) * 1000;
    }
  }

  /**
   * game deletes tank b4 life is reported at zero. need a way to check if we're still kickin
   */
  private void checkPulse() {
    int col = tank.getLastCol();
    int row = tank.getLastRow();
    if (tank.getHealth() != 0 && col != -1 && row != -1) {

      boolean dead = true;

      if (tank.getId() == decodeTankId(mEntities[(col - 1) % 16][row]) ||
          tank.getId() == decodeTankId(mEntities[(col + 1) % 16][row]) ||
          tank.getId() == decodeTankId(mEntities[col][row]) ||
          tank.getId() == decodeTankId(mEntities[col][(row - 1) % 16]) ||
          tank.getId() == decodeTankId(mEntities[col][(row + 1) % 16])) {
        dead = false;
      }

      if (dead) {
        tank.setId(-1);
        tank.setHealth(0);
        ((TankClientActivity) context).updateHP(0);
      }
    }
  }
}


