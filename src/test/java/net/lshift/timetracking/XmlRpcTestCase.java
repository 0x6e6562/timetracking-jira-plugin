/*
 * Created by IntelliJ IDEA.
 * User: Mike
 * Date: Aug 9, 2004
 * Time: 4:09:01 PM
 */
package net.lshift.timetracking;

import com.atlassian.core.util.RandomGenerator;
import junit.framework.TestCase;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class XmlRpcTestCase extends TestCase
{
    public void testServer() throws Exception {

        final String server = "http://localhost:8080/rpc/xmlrpc";
        XmlRpcClient xmlrpc = new XmlRpcClient(server);
        String token = (String) xmlrpc.execute("jira1.login", makeParams("admin", "password"));
        assertNotNull(token);

        String time = (String) xmlrpc.execute("jira2.trackTime", makeParams(token));
        assertEquals("helloworld", time);
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