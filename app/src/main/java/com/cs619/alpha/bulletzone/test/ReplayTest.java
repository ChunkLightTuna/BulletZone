package com.cs619.alpha.bulletzone.test;

/**
 * Created by Glenn on 4/29/16.
 *
 * Creates a single grid to pass through the database for functionality check
 *
 */
public class ReplayTest {

    private int[][] grid = null;

    /**
     * Creates a grid with a single int from 1-256
     *
     */
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

    /**
     * Returns the grid created by the constructor.
     *
     * @return grid int[][]
     */
    public int[][] getGrid(){ return grid; }
}
