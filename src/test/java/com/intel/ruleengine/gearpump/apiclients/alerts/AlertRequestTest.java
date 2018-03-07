package com.intel.ruleengine.gearpump.apiclients.alerts;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AlertRequestTest {
    private static final String MESSAGE_TYPE = "MockMessageType";
    private AlertRequest alertRequest;
    private List<Alert> data;

    @Before
    public void setUp(){
        alertRequest = new AlertRequest();
        data = Arrays.asList(new Alert());
    }

    @Test
    public void testMsgType(){
        alertRequest.setMsgType(MESSAGE_TYPE);
        Assert.assertEquals(MESSAGE_TYPE, alertRequest.getMsgType());
    }

    @Test
    public void testData(){
        alertRequest.setData(data);
        Assert.assertEquals(data, alertRequest.getData());
    }
}
