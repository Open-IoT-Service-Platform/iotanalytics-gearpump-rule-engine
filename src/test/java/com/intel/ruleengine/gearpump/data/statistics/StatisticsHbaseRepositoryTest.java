package com.intel.ruleengine.gearpump.data.statistics;

import com.intel.ruleengine.gearpump.data.HbaseProperties;
import com.intel.ruleengine.gearpump.rules.IdGenerator;
import com.intel.ruleengine.gearpump.tasks.messages.Observation;
import io.gearpump.cluster.UserConfig;
import org.apache.hadoop.hbase.client.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


@RunWith(PowerMockRunner.class)
@PrepareForTest(CustomAggregationClient.class)
public class StatisticsHbaseRepositoryTest {
    private static final String TABLE_PREFIX = "MockTablePrefix";
    private static final String ZOOKEEPER_QUORUM = "MockZookeeperQuorum";
    private static final long observationTimestamp = System.currentTimeMillis();
    private List<Observation> observationList;
    private String componentId = IdGenerator.generateId();
    private String value = "100.7";

    @Mock
    private Connection hbaseConnection;

    @Mock
    private ResultScanner resultScanner;

    @Mock
    private Table table;

    @Mock
    private Iterator iterator;

    @Mock
    private Result result;

    @Mock
    private Admin admin;

    private  UserConfig userConfig = getUserConfig();

    @InjectMocks
    private  StatisticsHbaseRepository statisticsHbaseRepository = new StatisticsHbaseRepository(userConfig);

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        Mockito.when(hbaseConnection.getTable(Matchers.any())).thenReturn(table);
        Mockito.when(hbaseConnection.getAdmin()).thenReturn(admin);
        Mockito.when(table.getScanner(Matchers.any(Scan.class))).thenReturn(resultScanner);
        Mockito.when(resultScanner.iterator()).thenReturn(iterator);
        Mockito.when(iterator.hasNext()).thenReturn(true, false);
        Mockito.when(iterator.next()).thenReturn(result);
        CustomAggregationClient customAggregationClient = PowerMockito.mock(CustomAggregationClient.class);
        PowerMockito.whenNew(CustomAggregationClient.class).withAnyArguments().thenReturn(customAggregationClient);
    }

    @Test
    public void testPutObservationForStatisticsRuleCondition() throws Exception{
        observationList = createObservationList(value);
        mockTablePutOperation();
        statisticsHbaseRepository.putObservationForStatisticsRuleCondition(observationList);
        Get mockGet = Mockito.mock(Get.class);
        Assert.assertEquals(result, table.get(mockGet));
    }

    @Test(expected = IOException.class)
    public void testPutObservationForStatisticsRuleConditionWithException() throws Exception{
        Mockito.doThrow(new NumberFormatException()).when(table).put(Matchers.anyList());
        observationList = createObservationList(value);
        statisticsHbaseRepository.putObservationForStatisticsRuleCondition(observationList);
    }

    private UserConfig getUserConfig(){
        UserConfig config = UserConfig.empty()
                .withString(HbaseProperties.TABLE_PREFIX,TABLE_PREFIX)
                .withString(HbaseProperties.ZOOKEEPER_QUORUM, ZOOKEEPER_QUORUM);
        return  config;
    }

    private List<Observation> createObservationList(String value) {
        Observation observation = new Observation();
        observation.setCid(componentId);
        observation.setOn(observationTimestamp);
        observation.setValue(value);
        return Arrays.asList(observation);
    }

    private void mockTablePutOperation() throws IOException{
       Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Mockito.when(table.get(Matchers.any(Get.class))).thenReturn(result);
                return null;
            }
        }).when(table).put(Matchers.anyList());
    }
}
