package com.intel.ruleengine.gearpump.apiclients.alerts;

import com.intel.ruleengine.gearpump.rules.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AlertTest {
    private final String accountId = IdGenerator.generateId();
    private final String ruleId = IdGenerator.generateId();
    private final Long timestamp = 10L;
    private final Long systemOn = 20L;
    private Alert alert;
    private List<Condition> conditions;

    @Before
    public void setUp(){
        conditions = Arrays.asList(new Condition());
        alert = new Alert();
        alert.setAccountId(accountId);
        alert.setRuleId(ruleId);
        alert.setTimestamp(timestamp);
        alert.setConditions(conditions);
        alert.setSystemOn(systemOn);
    }

    @Test
    public void testAccountId(){
        Assert.assertEquals(accountId, alert.getAccountId());
    }

    @Test
    public void testRuleId(){
        Assert.assertEquals(ruleId, alert.getRuleId());
    }

    @Test
    public void testTimestamp(){
        Assert.assertEquals(timestamp, alert.getTimestamp());
    }

    @Test
    public void testConditions(){
        Assert.assertEquals(conditions, alert.getConditions());
    }

    @Test
    public void testSystemOn(){
        Assert.assertEquals(systemOn, alert.getSystemOn());
    }
}
