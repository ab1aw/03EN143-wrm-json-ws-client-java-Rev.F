package com.persistentsystems.examples.testclient3;

import com.neovisionaries.ws.client.WebSocketException;
import com.persistentsystems.socketclient.auth.WrAuth;
import com.persistentsystems.socketclient.containers.WrGetVariableList;
import com.persistentsystems.socketclient.containers.WrSetVariableList;
import com.persistentsystems.socketclient.except.WebSocketTimeout;
import com.persistentsystems.socketclient.net.WrIpAddress;
import com.persistentsystems.socketclient.results.WrJsonNetworkResult;
import com.persistentsystems.socketclient.results.WrJsonNetworkResults;
import com.persistentsystems.socketclient.results.WrJsonResult;
import com.persistentsystems.socketclient.sockets.WrBlockingSocket;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestClient3BlockingNetwork {
    static int TEST_NO = 0;

    public static void main(String[] xArgs) throws IOException, WebSocketException, WebSocketTimeout {
        System.out.println("in main()");
        if (xArgs[0] == null || xArgs[0].isEmpty()) {
            System.out.println("Must provide test number");
            System.exit(-1);
        }
        TEST_NO = Integer.parseInt(xArgs[0]);

        WrBlockingSocket c1 = new WrBlockingSocket();
        System.out.println("call c1.isOpen() = " + c1.isOpen());
        c1.setAuth(new WrAuth().setPassword("password").setUserName("factory"));
        //c1.setAuth(new WrAuth().setPassword("password"));
        c1.setIpAddress(new WrIpAddress().set("10.3.1.254").setPort(443));
        System.out.println("call c1.connectBlocking()");
        c1.connectBlocking();
        System.out.println("call c1.isOpen() = " + c1.isOpen());
        System.out.println("call c1.getAuth().getPassword() = " + c1.getAuth().getPassword());
        System.out.println("call c1.getAuth().getUserName() = " + c1.getAuth().getUserName());
        System.out.println("call c1.getIpAddress() = " + c1.getIpAddress());

        //System.out.println("call c1.networkGet(protocol_version) = " + c1.networkGet("protocol_version"));

        WrJsonNetworkResults wrJsonNetworkResults = c1.networkGet("protocol_version");
        System.out.println("wrJsonNetworkResults = " + wrJsonNetworkResults);

//        Map<String, String> map = wrJsonNetworkResults.getValues();
//        System.out.println("map.isEmpty() = " + map.isEmpty());

        JSONObject jsObject = wrJsonNetworkResults.getJson();
        Set<String> itr = jsObject.keySet();
        for (String s: itr
             ) {
            System.out.println("    iterator string = " + s);
        }

        Object o = jsObject.get("protocol_version");
        System.out.println("\n    protocol_version getCanonicalName  = " + o.getClass().getCanonicalName());
        System.out.println("    protocol_version value             = " + o);

        Object o2 = jsObject.get("unit_id");
        System.out.println("\n    unit_id getCanonicalName  = " + o2.getClass().getCanonicalName());
        System.out.println("    unit_id value             = " + o2);

        JSONObject jsObject2 = jsObject.getJSONObject("unit_id");
        System.out.println("\n    jsObject2 getCanonicalName = " + jsObject2.getClass().getCanonicalName());
        System.out.println("    jsObject2 value            = " + jsObject2);

        Set<String> itr2 = jsObject2.keySet();
        for (String s: itr2
        ) {
            System.out.println("    iterator string = " + s);
        }

        Object o3 = jsObject2.getString("management_ip");
        System.out.println("\n    management_ip getCanonicalName = " + o3.getClass().getCanonicalName());
        System.out.println("    management_ip value            = " + o3);


        System.out.println("\n\n The IP address of the radio is = " + jsObject.getJSONObject("unit_id").getString("management_ip"));
        //        System.out.println("    jsObject value            = " + o);
/*
        JSONObject jsObject2 = jsObject.getJSONObject("protocol_version");
        Set<String> itr2 = jsObject2.keySet();
        for (String s: itr2
        ) {
            System.out.println("    iterator string = " + s);
        }
*/
        List<WrJsonNetworkResult> wrJsonNetworkResultList = wrJsonNetworkResults.getResults();
        System.out.println("call wrJsonNetworkResultList.isEmpty() = " + wrJsonNetworkResultList.isEmpty());

        for (WrJsonNetworkResult w: wrJsonNetworkResultList
             ) {
            System.out.println("call wrJsonNetworkResultList.get() = " + w.getIp());

        }

        if (TEST_NO == 1) {
            System.out.println("TEST_NO == 1");
            WrGetVariableList getList = new WrGetVariableList();
            getList.add("waverelay_ip");
            getList.add("waverelay_name");
            WrJsonNetworkResults result = c1.networkGet(getList);
            System.out.println("Got: " + result.getValues());
        }

        if (TEST_NO == 2) {
            WrJsonNetworkResults r = c1.networkGet("waverelay_ip");
            System.out.println(r.getValue());
        }

        if (TEST_NO == 3) {
            WrSetVariableList setList = new WrSetVariableList();
            setList.add("waverelay_name", "Test Name");
            WrJsonResult r = c1.networkSet(setList);
            System.out.println("Set: " + r.getValues());
        }

        if (TEST_NO == 4) {
            WrJsonResult res = c1.networkSet("waverelay_name", "TEST_NAME 2");
            System.out.println("Got " + res.getValues());
        }

        c1.close();
    }
}
