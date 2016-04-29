package com.cs619.alpha.tankclient.test;

/**
 * Created by Glenn on 4/29/16.
 */
public class ReplayTest {
    private int[][] grid = null;
    public ReplayTest(){
        int count = 1;
        int rows = 16;
        int cols = 16;
        grid = new int[rows][cols];
        for( int i = 0; i < rows; i++ ) {
            for (int k = 0; k < cols; k++) {
                grid[i][k] = count;
                count++;
            }
        }
    }

    public int[][] getGrid(){ return grid; }
}
