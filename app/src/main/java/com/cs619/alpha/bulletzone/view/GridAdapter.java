package com.cs619.alpha.bulletzone.view;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cs619.alpha.bulletzone.R;
import com.cs619.alpha.bulletzone.model.Tank;
import com.cs619.alpha.bulletzone.model.TankClientActivity;
import com.cs619.alpha.bulletzone.util.Tools;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

import java.util.HashSet;
import java.util.Set;

/**
 * Populates UI based on gamestate.
 */
@EBean
public class GridAdapter extends BaseAdapter {
  private static final String TAG = "GridAdapter";

  private final Object monitor = new Object();

  private Context context;

  private boolean foundOne = false;
  private int hue = 0;

  private Drawable wall, destructibleWall, tankDown, tankLeft, tankRight, tankUp, tankDownBlue, tankLeftBlue, tankRightBlue, tankUpBLue, bulletOne, bulletTwo, bulletThree;
  Set<Drawable> drawables;

  @SystemService
  protected LayoutInflater inflater;
  private int[][] mEntities = new int[16][16];
  private Tank tank;

  public void updateList(int[][] entities) {
    synchronized (monitor) {
      this.mEntities = entities;

      checkPulse();

      this.notifyDataSetChanged();

      ((TankClientActivity) context).updateHP();
    }
  }

  public void setContext(Context context) {
    this.context = context;

    drawables = new HashSet<>();
    wall = context.getResources().getDrawable(R.drawable.wall);
    drawables.add(wall);
    destructibleWall = context.getResources().getDrawable(R.drawable.destructible_wall);
    drawables.add(destructibleWall);

    tankDown = context.getResources().getDrawable(R.drawable.tank_down);
    drawables.add(tankDown);
    tankLeft = context.getResources().getDrawable(R.drawable.tank_left);
    drawables.add(tankLeft);
    tankRight = context.getResources().getDrawable(R.drawable.tank_right);
    drawables.add(tankRight);
    tankUp = context.getResources().getDrawable(R.drawable.tank_up);
    drawables.add(tankUp);

    tankDownBlue = context.getResources().getDrawable(R.drawable.tank_down_blue);
    drawables.add(tankDownBlue);
    tankLeftBlue = context.getResources().getDrawable(R.drawable.tank_left_blue);
    drawables.add(tankLeftBlue);

    tankRightBlue = context.getResources().getDrawable(R.drawable.tank_right_blue);
    drawables.add(tankRightBlue);
    tankUpBLue = context.getResources().getDrawable(R.drawable.tank_up_blue);
    drawables.add(tankUpBLue);

    bulletOne = context.getResources().getDrawable(R.drawable.bullet_1);
    drawables.add(bulletOne);
    bulletTwo = context.getResources().getDrawable(R.drawable.bullet_2);
    drawables.add(bulletTwo);
    bulletThree = context.getResources().getDrawable(R.drawable.bullet_3);
    drawables.add(bulletThree);
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
        if (val == 1000) {
          ((ImageView) view).setImageDrawable(wall);
        } else if (val == 1500) {
          ((ImageView) view).setImageDrawable(destructibleWall);
        } else if (val >= 2000000 && val <= 3000000) {
          int dmg = ((val % 1000) - (val % 10)) / 10;
          if (dmg == 10) {
            ((ImageView) view).setImageDrawable(bulletOne);
          } else if (dmg == 30) {
            ((ImageView) view).setImageDrawable(bulletTwo);
          } else {
            ((ImageView) view).setImageDrawable(bulletThree);
          }
        } else if (val >= 10000000 && val <= 20000000) {
          int tankId, direction, life;
          Drawable up, right, down, left;
          direction = val % 10;

          life = ((val % 10000) - (val % 10)) / 10;
          tankId = (val / 10000) - (val / 10000000) * 1000;

          if (tank.getId() == tankId) {
            tank.setHealth(life);

            tank.setLastCol(col);
            tank.setLastRow(row);
            foundOne = true;

            up = tankUpBLue;
            right = tankRightBlue;
            down = tankDownBlue;
            left = tankLeftBlue;
          } else {
            up = tankUp;
            right = tankRight;
            down = tankDown;
            left = tankLeft;
          }

          if (direction == 0) {
            ((ImageView) view).setImageDrawable(up);
          } else if (direction == 2) {
            ((ImageView) view).setImageDrawable(right);
          } else if (direction == 4) {
            ((ImageView) view).setImageDrawable(down);
          } else if (direction == 6) {
            ((ImageView) view).setImageDrawable(left);
          }

        }
      } else {
        ((ImageView) view).setImageDrawable(null);
      }

    }
    return view;
  }

  /**
   * @param val int
   * @return boolean
   */
  private int decodeTankId(int val) {

    int tankId = (val < 10000000 || val > 20000000) ? -1 : (val / 10000) - (val / 10000000) * 1000;

    Log.v(TAG, "decodeTankId() called with: " + "val = [" + val + "], computed tankId as [" + tankId + "]");

    return tankId;
  }

  /**
   * game deletes tank b4 life is reported at zero. need a way to check if we're still kickin
   */
  private void checkPulse() {
    int row = tank.getLastRow();
    int col = tank.getLastCol();
    long tankId = tank.getId();

    if (tankId != -1 && row != -1 && col != -1 && tank.getHealth() != 0 && foundOne) {
      boolean dead = true;

      if (tankId == decodeTankId(mEntities[(row + 15) % 16][col]) ||
          tankId == decodeTankId(mEntities[(row + 1) % 16][col]) ||
          tankId == decodeTankId(mEntities[row][col]) ||
          tankId == decodeTankId(mEntities[row][(col + 15) % 16]) ||
          tankId == decodeTankId(mEntities[row][(col + 1) % 16])) {
        dead = false;
      }

      Log.v(TAG, "checkPulse() called with: " +
          "row=[" + row + "], " +
          "col=[" + col + "], " +
          "foundOne=[" + foundOne + "], " +
          "tank.getId()=[" + tankId + "], " +
          "dead=[" + dead + "]");

      if (dead) {
        tank.setId(-1);
        tank.setLastRow(-1);
        tank.setLastCol(-1);
        tank.setHealth(0);
        foundOne = false;
      }
    }
  }

  /**
   *
   * @param hue int
   */
  public void setHue(int hue) {
    ColorFilter colorFilter = Tools.ColorFilterGenerator.adjustHue(hue - this.hue  - 180); //input is 0-360, output is -180 to 180
    this.hue = hue;

    for (Drawable drawable : drawables) {
      drawable.setColorFilter(colorFilter);
    }
  }
}