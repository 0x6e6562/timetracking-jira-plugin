package net.lshift.timetracking;

import com.atlassian.jira.issue.worklog.Worklog;
import com.atlassian.jira.issue.worklog.WorklogImpl;
import com.atlassian.jira.issue.worklog.WorklogManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.user.util.UserManager;
import com.opensymphony.user.User;


import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.Calendar;

import org.apache.log4j.Logger;


public class TimeMachine {

    private static final Logger log = Logger.getLogger(TimeMachine.class);

    WorklogManager worklogManager;
    UserManager userManager;
    IssueManager issueManager;


    long newEstimate = 0;

    public TimeMachine(WorklogManager worklogManager, UserManager userManager, IssueManager issueManager) {
        this.worklogManager = worklogManager;
        this.userManager = userManager;
        this.issueManager = issueManager;
    }

    Worklog createWorklogItem(TimeTrackingEntry entry) {

        Worklog worklog = null;

        User user = userManager.getUser(entry.getUser());

        if (user == null) throw new RuntimeException("Could not resolve user: " + entry.getUser());

        log.debug("Resolved user: " + user.getName());
        Issue issue = issueManager.getIssueObject(entry.getIssue());

        if (issue == null) throw new RuntimeException("Could not resolve issue: " + entry.getIssue());

        log.debug("Resolved issue: " + issue.getKey());


        // This could potentially be a very expensive thing to do if you have an issue with many worklogs
        // If this becomes an issue, we'll have to use the underlying entity manager
        List<Worklog> items =  worklogManager.getByIssue(issue);
        for (Worklog existing : items) {
            if (sameDayForUser(existing, entry)) {
                worklog = existing;
                break;
            }
        }

        Long id = null;
        String author = entry.getUser();
        String comment = "";
        Date startDate = entry.getDate();
        String groupLevel = "";
        Long roleLevelId = null;
        Long timeSpent = entry.getTimeSpent() * 60;

        if (null == worklog) {
            worklog = new WorklogImpl(worklogManager, issue, id, author, comment,
                                      startDate, groupLevel, roleLevelId, timeSpent);
            log.info("Creating new worklog item for entry (don't forget to times by 60): " + entry);
            worklog = worklogManager.create(user, worklog, newEstimate, true);
        }
        else if (!worklog.getTimeSpent().equals(timeSpent)){
            worklog = new WorklogImpl(worklogManager, issue, worklog.getId(), worklog.getAuthor(), worklog.getComment(),
                                      worklog.getStartDate(), worklog.getGroupLevel(), worklog.getRoleLevelId(),
                                      timeSpent);
            log.info("Updating existing worklog item for entry (don't forget to times by 60): " + entry);
            worklog = worklogManager.update(user, worklog, newEstimate, false);
        }

        return worklog;
    }

    /**
     * Returns true if the time tracking entry is for the same user and day
     */
    public boolean sameDayForUser(Worklog existing, TimeTrackingEntry entry) {
        if (existing.getAuthor().equals(entry.getUser())) {
            return sameDay(existing.getStartDate(), entry.getDate());
        }
        else {
            return false;
        }
    }

    /**
     * I hate having to implement date routines but I don't want to have include something like jodatime
     * into the plugin because I'd need to supply the jar somehow :-(
     */
    public boolean sameDay(Date first, Date second) {
        boolean same = true;
        Calendar firstCal = new GregorianCalendar();
        firstCal.setTime(first);
        Calendar secondCal = new GregorianCalendar();
        secondCal.setTime(second);
        same &= firstCal.get(Calendar.YEAR) == secondCal.get(Calendar.YEAR);
        same &= firstCal.get(Calendar.MONTH) == secondCal.get(Calendar.MONTH);
        same &= firstCal.get(Calendar.DAY_OF_MONTH) == secondCal.get(Calendar.DAY_OF_MONTH);
        return same;
    }

}
