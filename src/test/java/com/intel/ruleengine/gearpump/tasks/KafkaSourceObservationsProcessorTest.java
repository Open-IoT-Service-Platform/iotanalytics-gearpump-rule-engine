package com.intel.ruleengine.gearpump.tasks;

import io.gearpump.cluster.UserConfig;
import io.gearpump.cluster.client.ClientContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.Mockito;

import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientContext.class)
public class KafkaSourceObservationsProcessorTest {
    private static final String TOPIC = "mock_topic";
    private static final String ZOOKEEPER = "mock_zookeeper";
    private static final String URI = "mock_uri";

    @Test
    public void testCreateKafkaSourceObservationsProcessor(){
        ClientContext clientContext = Mockito.mock(ClientContext.class);
        PowerMockito.mockStatic(ClientContext.class);
        PowerMockito.when(ClientContext.apply()).thenReturn(clientContext);

        UserConfig userConfig = getUserConfig();
        KafkaSourceObservationsProcessor kafkaSourceObservationsProcessor = new KafkaSourceObservationsProcessor(userConfig);
        Assert.assertNotNull(kafkaSourceObservationsProcessor);
    }

    private UserConfig getUserConfig(){
        UserConfig config = UserConfig.empty()
                .withString(KafkaSourceObservationsProcessor.KAFKA_TOPIC_PROPERTY, TOPIC)
                .withString(KafkaSourceObservationsProcessor.KAFKA_URI_PROPERTY, URI)
                .withString(KafkaSourceObservationsProcessor.KAFKA_ZOOKEEPER_PROPERTY, ZOOKEEPER);
        return  config;
    }

}
