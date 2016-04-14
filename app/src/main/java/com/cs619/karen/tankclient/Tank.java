package com.cs619.karen.tankclient;

/**
 * Created by Glenn.
 */
public class Tank {
  private long tankId;
  private int dir;
  private int health;

  public Tank(long id) {
    tankId = id;
    dir = 0;
  }

  /**
   * Set tank id.
   *
   * @param id long
   */
  public void setId(long id) {
    tankId = id;
  }

  /**
   * Get tank id.
   *
   * @return int
   */
  public long getId() {
    return tankId;
  }

  /**
   * Get tank direction.
   *
   * @return int
   */
  public int getDir() {
    return dir;
  }

  /**
   * get reverse of tank direction.
   *
   * @return int
   */
  public int getRevDir() {
    if (dir == 0) {
      return 4;
    } else if (dir == 2) {
      return 6;
    } else if (dir == 4) {
      return 0;
    } else {
      return 2;
    }
  }

  /**
   * get direction of tank's left side.
   *
   * @return int
   */
  public int getLeftDir() {
    if (dir == 0) {
      return 6;
    } else if (dir == 2) {
      return 0;
    } else if (dir == 4) {
      return 2;
    } else {
      return 4;
    }
  }

  /**
   * get direction of tank's right side.
   *
   * @return int
   */
  public int getRightDir() {
    if (dir == 0) {
      return 2;
    } else if (dir == 2) {
      return 4;
    } else if (dir == 4) {
      return 6;
    } else {
      return 0;
    }
  }

  /**
   * set tank direction.
   *
   * @param tankDir int
   */
  public void setDir(int tankDir) {
    dir = tankDir;
  }

  /**
   * get tank HP.
   *
   * @return int
   */
  public int getHealth() {
    return health;
  }

  ;

}