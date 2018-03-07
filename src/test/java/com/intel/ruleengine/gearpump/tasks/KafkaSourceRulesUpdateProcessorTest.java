package com.intel.ruleengine.gearpump.tasks;

import io.gearpump.cluster.UserConfig;
import io.gearpump.cluster.client.ClientContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientContext.class)
public class KafkaSourceRulesUpdateProcessorTest {
    private static final String TOPIC = "TOPIC";
    private static final String ZOOKEEPER = "ZOOKEEPER";
    private static final String URI = "URI";


    @Test
    public void testCreateKafkaSourceRulesUpdateProcessor(){
        ClientContext clientContext = Mockito.mock(ClientContext.class);
        PowerMockito.mockStatic(ClientContext.class);
        PowerMockito.when(ClientContext.apply()).thenReturn(clientContext);

        UserConfig userConfig = getUserConfig();
        KafkaSourceRulesUpdateProcessor kafkaSourceRulesUpdateProcessor = new KafkaSourceRulesUpdateProcessor(userConfig);
        Assert.assertNotNull(kafkaSourceRulesUpdateProcessor);
    }

    private UserConfig getUserConfig(){
        UserConfig config = UserConfig.empty()
                .withString(KafkaSourceProcessor.KAFKA_ZOOKEEPER_PROPERTY, ZOOKEEPER)
                .withString(KafkaSourceProcessor.KAFKA_URI_PROPERTY, URI)
                .withString(KafkaSourceRulesUpdateProcessor.KAFKA_TOPIC_PROPERTY, TOPIC);
        return  config;
    }
}
