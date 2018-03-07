package com.intel.ruleengine.gearpump.apiclients.alerts;

import com.intel.ruleengine.gearpump.rules.IdGenerator;
import com.intel.ruleengine.gearpump.tasks.messages.Observation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ConditionTest {
    private static final String componentId = IdGenerator.generateId();
    private static final String pointsValue= "MockPointValue";
    private static final Long timestamp = 10L;

    private Condition condition;
    private Observation observation;
    private List<Condition.Component> components;
    private Condition.Component component;
    private List<Condition.Points> points;
    private Condition.Points point;

    @Before
    public void setUp(){
        observation = new Observation();
        observation.setCid(IdGenerator.generateId());
        observation.setAid(IdGenerator.generateId());
        observation.setValue("value");
        observation.setOn(20L);

        condition = new Condition(observation);
        component = condition.new Component();
        components = Arrays.asList(component);

        point = condition.new Points();
        points = Arrays.asList(point);
    }

    @Test
    public void testComponent(){
        condition.setComponents(components);
        Assert.assertEquals(components, condition.getComponents());
    }

    @Test
    public void testComponentId(){
        component.setComponentId(componentId);
        Assert.assertEquals(componentId, component.getComponentId());
    }

    @Test
    public void testValuePoints(){
        component.setValuePoints(points);
        Assert.assertEquals(points, component.getValuePoints());
    }

    @Test
    public void testPointsValue(){
        point.setValue(pointsValue);
        Assert.assertEquals(pointsValue, point.getValue());
    }

    @Test
    public void testPointsTimestamp(){
        point.setTimestamp(timestamp);
        Assert.assertEquals(timestamp, Long.valueOf(point.getTimestamp()));
    }
}
