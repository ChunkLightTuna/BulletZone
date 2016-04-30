package com.cs619.alpha.tankclient.model;

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
    health = 100;
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
   * set tank direction.
   *
   * @param tankDir int
   */
  public void setDir(int tankDir) {
    dir = tankDir;
  }

  /**
   * Health getter.
   *
   * @return int
   */
  public int getHealth() {
    return health;
  }

  /**
   * Health setter.
   * @param health int
   */
  public void setHealth(int health){ this.health = health; }

}