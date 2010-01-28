package net.lshift.timetracking;

import junit.framework.TestCase;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.commons.io.IOUtils;
import org.apache.xmlrpc.XmlRpcException;

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
        String token = (String) executeOrFail(xmlrpc, LOGIN, makeParams(user, password));
        assertNotNull(token);

        InputStream is = getClass().getClassLoader().getResourceAsStream("integration-test-data.csv");
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer);
        String csv = writer.toString();

        Vector<String> worklogIds = (Vector<String>) executeOrFail(xmlrpc, TRACK_TIME, makeParams(token, csv));

        assertNotNull(worklogIds);
        assertEquals(2, worklogIds.size());
    }

    private static Object executeOrFail(XmlRpcClient client, String command, Vector args) throws Exception {
        Object result = client.execute(command, args);
        if (result instanceof XmlRpcException) {
            fail(((XmlRpcException)result).getMessage());
            return null;
        }
        else {
            return result;
        }
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