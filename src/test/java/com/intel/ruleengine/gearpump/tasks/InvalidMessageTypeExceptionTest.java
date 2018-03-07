package com.intel.ruleengine.gearpump.tasks;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvalidMessageTypeExceptionTest {
    private final static String ERROR_MESSAGE = "ErrorMesssage";
    private InvalidMessageTypeException invalidMessageTypeException;

    @Mock
    private Throwable throwable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateInvalidMessageTypeExceptionWithoutParams(){
        invalidMessageTypeException = new InvalidMessageTypeException();
        Assert.assertNotNull(invalidMessageTypeException);
    }

    @Test
    public void testCreateInvalidMessageTypeExceptionWithThrowableAndString(){
        invalidMessageTypeException = new InvalidMessageTypeException(ERROR_MESSAGE,throwable);
        Assert.assertEquals(ERROR_MESSAGE, invalidMessageTypeException.getMessage());
    }

}
