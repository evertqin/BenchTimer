package com.benchtimer.tests.src;

import android.test.InstrumentationTestCase;

import com.benchtimer.time.CountUpTimer;
import com.benchtimer.utils.Utils;

/**
 * Created by evert on 3/22/2014.
 */
public class SampleTest extends InstrumentationTestCase {

    CountUpTimer countUpTimer1;
    CountUpTimer countUpTimer2;

    public void test() {
        String source = "5:00:34";
        long output = Utils.convertToTime(source);
        System.out.println(output);

        countUpTimer1 = new CountUpTimer() {
            @Override
            public void onTick(long currentElapseInMills) {
                System.out.println("CountupTimer 1 " + currentElapseInMills);
            }

        };
        countUpTimer1.start();

        countUpTimer2 = new CountUpTimer() {
            @Override
            public void onTick(long currentElapseInMills) {
                System.out.println("CountupTimer 2 " + currentElapseInMills);
            }

        };
        countUpTimer2.start();

    }

}

