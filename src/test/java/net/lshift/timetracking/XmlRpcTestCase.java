/*
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: Aug 9, 2004
 * Time: 4:09:01 PM
 */
package net.lshift.timetracking;

import junit.framework.TestCase;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.commons.io.IOUtils;

import java.util.*;
import java.io.InputStream;
import java.io.StringWriter;

public class XmlRpcTestCase extends TestCase {

    static final String PATH = "timetracking";
    static final String TRACK_TIME = PATH + "." + "trackTime";
    static final String LOGIN = "jira1.login";

    String server;
    String user;
    String password;

    @Override
    public void setUp() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("jira-instance.properties");
        Properties props = new Properties();
        props.load(is);
        server = "http://" + props.get("host") + ":" + props.get("port") + "/rpc/xmlrpc";
        user = (String) props.get("user");
        password = (String) props.get("password");
    }

    public void testServer() throws Exception {

        XmlRpcClient xmlrpc = new XmlRpcClient(server);
        String token = (String) xmlrpc.execute(LOGIN, makeParams(user, password));
        assertNotNull(token);

        InputStream is = getClass().getClassLoader().getResourceAsStream("test2.csv");
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);
        String csv = writer.toString();

        Vector<String> worklogIds = (Vector<String>) xmlrpc.execute(TRACK_TIME, makeParams(token, csv));
        assertNotNull(worklogIds);
        assertEquals(1, worklogIds.size());
    }

    private static Vector makeParams(Object p1)
    {
        Vector params;
        params = new Vector();
        params.add(p1);

        return params;
    }

    private static Vector makeParams(Object p1, Object p2)
    {
        Vector params = makeParams(p1);
        params.add(p2);
        return params;
    }

}