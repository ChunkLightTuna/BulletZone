package com.cs619.alpha.bulletzone.test;

import com.cs619.alpha.bulletzone.model.Tank;

/**
 * Created by Glenn on 4/30/16.
 */
public class TankTest {
    private Tank t;

    /**
     * Makes sure all tank methods run properly
     *
     */
    public void runTest(){
        long id = 878;

        Tank t = new Tank();

        t.setId(id);

        assert(id == t.getId());

        t.setDir( 4 );
        assert( t.getDir() == 4 );
        assert( t.getRevDir() == 0 );

        t.setDir( 0 );
        assert( t.getDir() == 0 );
        assert( t.getRevDir() == 4 );

        t.setDir( 2 );
        assert( t.getDir() == 2 );
        assert( t.getRevDir() == 6 );

        t.setDir( 6 );
        assert( t.getDir() == 6 );
        assert( t.getRevDir() == 2 );

        t.setHealth(100);
        assert(t.getHealth() == 100);

        for( int i = 99; i > 0; i-- ){
            t.setHealth(i);
            assert( t.getHealth() == i );
        }

        t.setId( 876 );
        assert(t.getId() != id);
    }
}
