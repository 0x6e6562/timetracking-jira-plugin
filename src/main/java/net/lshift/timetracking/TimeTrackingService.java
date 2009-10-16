package net.lshift.timetracking;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.worklog.Worklog;
import com.atlassian.jira.issue.worklog.WorklogManager;
import com.atlassian.jira.user.util.UserManager;
import static net.lshift.timetracking.TimeTrackingEntry.parse;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

public class TimeTrackingService implements TimeTracking {

    private static final Logger log = Logger.getLogger(TimeTrackingService.class);

    TimeMachine timeMachine;

    public TimeTrackingService(WorklogManager worklogManager, UserManager userManager, IssueManager issueManager) {
        timeMachine = new TimeMachine(worklogManager, userManager, issueManager);
    }

    public Vector<String> trackTime(String token, String csv) {
        log.debug(csv);
        Vector<String> results = new Vector<String>();
        try {
            List<TimeTrackingEntry> entries = parse(csv);
            log.info("Processing " + entries.size() + " entries ......");
            for (TimeTrackingEntry entry : entries) {
                Worklog worklog = timeMachine.createWorklogItem(entry);
                results.add(worklog.getId().toString());                
            }
        } catch (Exception e) {
            // TODO Somehow return this exception to the client in an intelligent fashion
            throw new RuntimeException(e);
        }
        return results;
    }
}
