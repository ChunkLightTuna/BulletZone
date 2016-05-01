package com.cs619.alpha.bulletzone.test;

import com.cs619.alpha.bulletzone.rest.PollerTask;

/**
 * Created by Glenn on 4/30/16.
 */
public class PollerTest {
    private PollerTask p;

    /**
     * Tests certain functionality of the pollertask
     *
     */
    public void runTest(){
        p = new PollerTask();

        p.togglePlayMode( false );
        assert( !p.getPlayMode() );

        p.togglePlayMode( true );
        assert( p.getPlayMode() );

        p.setSpeed( 4 );
        assert( p.getReplaySpeed() == 4 );

        p.setSpeed( 2 );
        assert( p.getReplaySpeed() != 4 );
        assert( p.getReplaySpeed() == 2 );

        p.setSpeed( 1 );
        assert( p.getReplaySpeed() == 1);
    }
}
