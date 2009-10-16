package net.lshift.timetracking;

import junit.framework.TestCase;

import java.util.Date;
import java.util.GregorianCalendar;

public class TimeMachineTest extends TestCase {

    public void testIsSameDay() {
        TimeMachine tm = new TimeMachine(null, null, null);
        Date first = new GregorianCalendar(2009, 10 - 1, 13 ).getTime();
        Date second = new GregorianCalendar(2009, 10 - 1, 13 ).getTime();
        assertTrue(tm.sameDay(first, second));
        Date third = new GregorianCalendar(2010, 10 - 1, 13 ).getTime();
        assertFalse(tm.sameDay(first, third));
    }
}
