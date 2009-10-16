package net.lshift.timetracking;

import com.atlassian.jira.issue.worklog.Worklog;
import com.atlassian.jira.issue.worklog.WorklogImpl;
import com.atlassian.jira.issue.worklog.WorklogManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.user.util.UserManager;
import com.opensymphony.user.User;


import java.util.Date;

import org.apache.log4j.Logger;


public class TimeMachine {

    private static final Logger log = Logger.getLogger(TimeMachine.class);

    WorklogManager worklogManager;
    UserManager userManager;
    IssueManager issueManager;


    public TimeMachine(WorklogManager worklogManager, UserManager userManager, IssueManager issueManager) {
        this.worklogManager = worklogManager;
        this.userManager = userManager;
        this.issueManager = issueManager;
    }

    Worklog createWorklogItem(TimeTrackingEntry entry) {
        User user = userManager.getUser(entry.getUser());

        if (user == null) throw new RuntimeException("Could not resolve user: " + entry.getUser());

        log.info("Resolved user: " + user.getName());
        Issue issue = issueManager.getIssueObject(entry.getIssue());

        if (issue == null) throw new RuntimeException("Could not resolve issue: " + entry.getIssue());

        log.info("Resolved issue: " + issue.getKey());

        Long id = null;
        String author = "";
        String comment = "";
        Date startDate = entry.getDate();
        String groupLevel = "";
        Long roleLevelId = null;
        Long timeSpent = entry.getTimeSpent();

        Worklog worklog = new WorklogImpl(worklogManager, issue, id, author, comment,
                startDate, groupLevel, roleLevelId, timeSpent);

        long newEstimate = 0;

        boolean dispatchEvent = true;

        log.info("Creating new worklog item for entry: " + entry);

        return worklogManager.create(user, worklog, newEstimate, dispatchEvent);
    }

}
