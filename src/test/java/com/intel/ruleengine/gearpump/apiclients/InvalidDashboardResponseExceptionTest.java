package com.intel.ruleengine.gearpump.apiclients;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvalidDashboardResponseExceptionTest {
    private static final String MESSAGE = "MockMessage";
    private InvalidDashboardResponseException invalidDashboardResponseException;

    @Test
    public void  testCreateWithMessageAndThrowable(){
        invalidDashboardResponseException = new InvalidDashboardResponseException(MESSAGE, new Throwable());
        try {
            throw invalidDashboardResponseException;
        }catch (InvalidDashboardResponseException e){
            Assert.assertEquals(MESSAGE, e.getMessage());
        }
    }

    @Test(expected = InvalidDashboardResponseException.class)
    public void  testCreateWithThrowable() throws Exception{
        invalidDashboardResponseException = new InvalidDashboardResponseException(new Throwable());
        throw  invalidDashboardResponseException;
    }
}
