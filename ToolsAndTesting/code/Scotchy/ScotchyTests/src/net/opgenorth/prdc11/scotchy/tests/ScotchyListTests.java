package net.opgenorth.prdc11.scotchy.tests;

import android.test.ActivityInstrumentationTestCase2;
import net.opgenorth.prdc11.scotchy.Scotchy;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class net.opgenorth.prdc11.scotchy.ScotchyListTests \
 * net.opgenorth.prdc11.intro.tests/android.test.InstrumentationTestRunner
 */
public class ScotchyListTests extends ActivityInstrumentationTestCase2<Scotchy> {

    public ScotchyListTests() {
        super("net.opgenorth.prdc11.intro", Scotchy.class);
    }
}
