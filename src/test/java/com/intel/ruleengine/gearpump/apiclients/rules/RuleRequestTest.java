package com.intel.ruleengine.gearpump.apiclients.rules;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RuleRequestTest {
    private static final  String SYNCHRONIZATION_STATUS = "MockStatus";
    private RuleRequest ruleRequest;
    private List<String> status;

    @Before
    public void setUp() {
        status = Arrays.asList(SYNCHRONIZATION_STATUS);
        ruleRequest = new RuleRequest();
    }

    @Test
    public void testStatus(){
        ruleRequest.setStatus(status);
        Assert.assertEquals(status, ruleRequest.getStatus());
    }

    @Test
    public void testSynchronizationStatus(){
        ruleRequest.setSynchronizationStatus(SYNCHRONIZATION_STATUS);
        Assert.assertEquals(SYNCHRONIZATION_STATUS, ruleRequest.getSynchronizationStatus());

    }
}
