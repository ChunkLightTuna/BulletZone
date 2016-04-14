package com.cs619.karen.tankclient;

/**
 * Created by Glenn.
 */
public class Tank {
  private long tankId;
  private int dir;
  private int health;

  public Tank( long id ){
    tankId = id;
    dir = 0;
  }

  public void setId( long id ){ tankId = id; }
  public long getId(){ return tankId; }
  public int getDir(){ return dir; }
  public int getRevDir(){
    if( dir == 0 ){
      return 4;
    }
    else if( dir == 2 ){
      return 6;
    }
    else if( dir == 4 ){
      return 0;
    }
    else{
      return 2;
    }
  }
  public int getLeftDir(){
    if( dir == 0 ){
      return 6;
    }
    else if( dir == 2 ){
      return 0;
    }
    else if( dir == 4 ){
      return 2;
    }
    else{
      return 4;
    }
  }
  public int getRightDir(){
    if( dir == 0 ){
      return 2;
    }
    else if( dir == 2 ){
      return 4;
    }
    else if( dir == 4 ){
      return 6;
    }
    else{
      return 0;
    }
  }
  public void setDir( int tankDir ){ dir = tankDir; }
  public int getHealth(){ return health; };

}