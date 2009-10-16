package net.lshift.timetracking;

import junit.framework.TestCase;

import java.io.InputStream;
import java.util.List;
import java.util.Date;
import java.util.GregorianCalendar;

public class ParserTest extends TestCase {

    public void testParse() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("test1.csv");
        List<TimeTrackingEntry> entries = TimeTrackingEntry.parse(is);
        assertNotNull(entries);
        assertEquals(16, entries.size());
        TimeTrackingEntry third = entries.get(2);
        //2009-10-13,5.5
        assertEquals("davey.jones", third.getUser());
        assertEquals("ABC-4", third.getIssue());
        // What drugs were Sun on when they came up with this API?
        assertEquals(new GregorianCalendar(2009, 10 - 1, 13 ).getTime(), third.getDate());
        assertEquals(55L, third.getTimeSpent().longValue());
    }
}
