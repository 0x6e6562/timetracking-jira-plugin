package net.lshift.timetracking;

import com.atlassian.jira.issue.worklog.Worklog;
import junit.framework.TestCase;
import org.easymock.classextension.EasyMock;

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

    public void testSameDayAndDifferentUser() {
        String user1 = "user1";
        String user2 = "user2";

        Date first = new GregorianCalendar(2009, 10 - 1, 13 ).getTime();

        Worklog worklog = EasyMock.createMock(Worklog.class);
        EasyMock.expect(worklog.getAuthor()).andReturn(user1);
        EasyMock.expect(worklog.getStartDate()).andReturn(first);

        TimeTrackingEntry entry = EasyMock.createMock(TimeTrackingEntry.class);
        EasyMock.expect(entry.getUser()).andReturn(user2);
        EasyMock.expect(entry.getDate()).andReturn(first);
        
        EasyMock.replay(worklog, entry);

        TimeMachine tm = new TimeMachine(null, null, null);
        assertFalse(tm.sameDayForUser(worklog, entry));
    }

    public void testSameDayAndSameUser() {
        String user1 = "user1";

        Date first = new GregorianCalendar(2009, 10 - 1, 13 ).getTime();

        Worklog worklog = EasyMock.createMock(Worklog.class);
        EasyMock.expect(worklog.getAuthor()).andReturn(user1);
        EasyMock.expect(worklog.getStartDate()).andReturn(first);

        TimeTrackingEntry entry = EasyMock.createMock(TimeTrackingEntry.class);
        EasyMock.expect(entry.getUser()).andReturn(user1);
        EasyMock.expect(entry.getDate()).andReturn(first);

        EasyMock.replay(worklog, entry);

        TimeMachine tm = new TimeMachine(null, null, null);
        assertTrue(tm.sameDayForUser(worklog, entry));
    }
}
