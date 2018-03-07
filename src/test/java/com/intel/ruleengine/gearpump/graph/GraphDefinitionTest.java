package com.intel.ruleengine.gearpump.graph;


import io.gearpump.streaming.javaapi.Processor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class GraphDefinitionTest {
    @Mock
    private  ProcessorsBuilder processorsBuilder;

    @Mock
    private Processor kafkaSourceObservationsProcessor;

    @Mock
    private Processor kafkaSourceRulesUpdateProcessor;

    @Mock
    private Processor checkObservationInRules;

    @Mock
    private Processor sendAlerts;

    @Mock
    private Processor downloadRulesTask;

    @Mock
    private Processor persistRulesTask;

    @Mock
    private Processor persistComponentAlerts;

    @Mock
    private Processor checkRules;

    @Mock
    private Processor getRulesForComponent;

    @Mock
    private Processor persistObservation;

    private  GraphDefinition graphDefinition;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(processorsBuilder.getSendAlertsProcessor()).thenReturn(sendAlerts);
        Mockito.when(processorsBuilder.getCheckObservationInRulesProcessor()).thenReturn(checkObservationInRules);
        Mockito.when(processorsBuilder.getDownloadRulesProcessor()).thenReturn(downloadRulesTask);
        Mockito.when(processorsBuilder.getPersistRulesProcessor()).thenReturn(persistRulesTask);
        Mockito.when(processorsBuilder.getPersistComponentAlertsProccesor()).thenReturn(persistComponentAlerts);
        Mockito.when(processorsBuilder.getCheckRulesProcessor()).thenReturn(checkRules);
        Mockito.when(processorsBuilder.getRulesForComponentProcessor()).thenReturn(getRulesForComponent);
        Mockito.when(processorsBuilder.getKafkaSourceObservations()).thenReturn(kafkaSourceObservationsProcessor);
        Mockito.when(processorsBuilder.getKafkaSourceRulesUpdate()).thenReturn(kafkaSourceRulesUpdateProcessor);
        Mockito.when(processorsBuilder.getPersistObservationProcessor()).thenReturn(persistObservation);
        graphDefinition = new GraphDefinition(processorsBuilder);
    }

    @Test
    public void testGetDefinition(){
        Map definition = graphDefinition.getDefinition();
        Assert.assertTrue(definition.containsKey(checkRules));
        Assert.assertEquals(definition.get(checkRules), Arrays.asList(sendAlerts));
    }
}
