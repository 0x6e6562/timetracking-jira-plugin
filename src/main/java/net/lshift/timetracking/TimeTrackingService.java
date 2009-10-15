package net.lshift.timetracking;

import com.atlassian.jira.issue.worklog.WorklogManager;
import com.atlassian.jira.issue.worklog.Worklog;
import com.atlassian.jira.issue.worklog.WorklogImpl;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.user.util.UserManager;
import com.opensymphony.user.User;

import java.util.Date;
import java.util.Vector;

public class TimeTrackingService implements TimeTracking {

    WorklogManager worklogManager;
    UserManager userManager;
    IssueManager issueManager;

    public void setWorklogManager(WorklogManager worklogManager) {
        this.worklogManager = worklogManager;
    }

    public TimeTrackingService(WorklogManager worklogManager, UserManager userManager, IssueManager issueManager) {
        this.worklogManager = worklogManager;
        this.userManager = userManager;
        this.issueManager = issueManager;
    }

    public Vector<String> trackTime(String csv) {

        // Loop over the whole file
        
        TimeTrackingEntry entry = new TimeTrackingEntry(csv);

        User user = userManager.getUser(entry.getUser());
        Issue issue = issueManager.getIssueObject(entry.getIssue());

        Long id = null;
        String author = "";
        String comment = "";
        Date startDate = new Date();
        String groupLevel = "";
        Long roleLevelId = null;
        Long timeSpent = 9L;

        Worklog worklog = new WorklogImpl(worklogManager, issue, id, author, comment,
                                          startDate, groupLevel, roleLevelId, timeSpent);

        long newEstimate = 7;

        boolean dispatchEvent  = true ;

        Worklog updatedWorklog = worklogManager.create(user, worklog, newEstimate, dispatchEvent);

        Vector<String> results = new Vector<String>();

        results.add(updatedWorklog.getId().toString());

        return results;
    }
}
