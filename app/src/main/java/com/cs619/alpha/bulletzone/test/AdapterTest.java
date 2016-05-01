package com.cs619.alpha.bulletzone.test;

import java.util.List;

/**
 * Created by Glenn on 4/30/16.
 */
public class AdapterTest{

    private int fakeTank;
    private int fakeWallSolid;
    private int fakeWallDest;
    private int fakeBullet;
    private int fakeGrid[][];
    private List<int[][]> fakeList;

    /**
     * Creates a list of grids that make a pattern of each object
     *
     */
    public AdapterTest(){
        int rows = 16;
        int cols = 16;
        fakeGrid = new int[rows][cols];

        fakeTank = 17770500;
        fakeBullet = 2777050;
        fakeWallSolid = 1000;
        fakeWallDest = 1500;

        int count = 0;

        while( count != 208 ){
            for( int i = 0; i < rows; i++ ) {
                for (int k = 0; k < cols; k++) {
                    if( (k+3) <= 15 ) {
                        fakeGrid[i][k] = fakeWallSolid;
                        fakeGrid[i][k + 1] = fakeWallDest;
                        fakeGrid[i][k + 2] = fakeBullet;
                        fakeGrid[i][k + 3] = fakeTank;
                        fakeList.add(fakeGrid);
                        count++;
                    }
                    else {
                        count++;
                    }
                }
            }
        }
    }

    /**
     * Returns the list of fake grids
     *
     * @return List
     */
    public List<int[][]> getList(){ return fakeList; }
}
