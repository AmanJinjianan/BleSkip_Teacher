package com.qixiang.bleskip_teacher;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final String TAG = "ExampleUnitTest";

    @Test
    public void addition_isCorrect() throws Exception {

        assertEquals(true, fgh(2,9));
    }

    public boolean fgh(int hh,int pp){

        if(hh>pp)
        return true;
        else return false;
    }
    /*@Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }*/
}