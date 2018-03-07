package com.intel.ruleengine.gearpump.data.alerts;

import com.intel.ruleengine.gearpump.data.HbaseProperties;
import com.intel.ruleengine.gearpump.rules.IdGenerator;
import com.intel.ruleengine.gearpump.rules.Operators;
import com.intel.ruleengine.gearpump.rules.RuleCreator;
import com.intel.ruleengine.gearpump.tasks.messages.Observation;
import com.intel.ruleengine.gearpump.tasks.messages.Rule;
import com.intel.ruleengine.gearpump.tasks.messages.RulesWithObservation;
import io.gearpump.cluster.UserConfig;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RuleConditionsHbaseRepositoryTest {
    private static final String TABLE_PREFIX = "MockTablePrefix";
    private static final String ZOOKEEPER_QUORUM = "MockZookeeperQuorum";
    private static final String ROW_DELIMITER = ":";
    private static final String FIELD_FORMAT_TYPE = "%s";
    private static final String ROW_FORMAT = FIELD_FORMAT_TYPE + ROW_DELIMITER + FIELD_FORMAT_TYPE + ROW_DELIMITER + FIELD_FORMAT_TYPE;

    private List<RulesWithObservation> rulesWithObservationList;
    private RulesWithObservation rulesWithObservation;
    private UserConfig userConfig = getUserConfig();
    private String componentId = IdGenerator.generateId();
    private String accountId = IdGenerator.generateId();
    private long observationTimestamp = System.currentTimeMillis();
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

    @InjectMocks
    private RuleConditionsHbaseRepository ruleConditionsHbaseRepository = new RuleConditionsHbaseRepository(userConfig);

    @Before
    public void setUp() throws  Exception{
        MockitoAnnotations.initMocks(this);
        Mockito.when(hbaseConnection.getTable(Matchers.any())).thenReturn(table);
        Mockito.when(table.getScanner(Matchers.any(Scan.class))).thenReturn(resultScanner);
        Mockito.when(resultScanner.iterator()).thenReturn(iterator);
        Mockito.when(iterator.hasNext()).thenReturn(true, false);
        Mockito.when(iterator.next()).thenReturn(result);
    }

    @Test
    public void testPutTimebasedRuleComponents() throws Exception{
        mockTablePutOperation();
        Observation observation = createObservation(value);
        List<Rule> inputRules = Arrays.asList(
                RuleCreator.createRuleWithSingleTimeBasedCondition(Operators.EQUAL, value, componentId));
        rulesWithObservation = new RulesWithObservation(observation, inputRules);
        rulesWithObservationList = Arrays.asList(rulesWithObservation);
        ruleConditionsHbaseRepository.putTimebasedRuleComponents(rulesWithObservationList, true);
        Get get = Mockito.mock(Get.class);
        Assert.assertEquals(result, table.get(get));
    }

    @Test
    public void testGetLastTimebasedComponentObservation() throws  Exception{
        long lastTime = System.currentTimeMillis();
        byte[] row = Bytes.toBytes(String.format(ROW_FORMAT, accountId, componentId, lastTime));
        Mockito.when(result.getRow()).thenReturn(row);
        ScanProperties scanProperties = new ScanProperties();
        scanProperties.withStop(lastTime);
        ComponentObservation componentObservation =  ruleConditionsHbaseRepository.getLastTimebasedComponentObservation(scanProperties, false);
        Assert.assertEquals(lastTime, componentObservation.getTimestamp());
    }

    @Test
    public void TestPutFulfilledConditionsForObservation() throws Exception{
        mockTablePutOperation();
        Observation observation = createObservation(value);
        List<Rule> inputRules = Arrays.asList(
                RuleCreator.createRuleWithSingleTimeBasedCondition(Operators.EQUAL, value, componentId));
        rulesWithObservation = new RulesWithObservation(observation, inputRules);
        rulesWithObservationList = Arrays.asList(rulesWithObservation);
        ruleConditionsHbaseRepository.putFulfilledConditionsForObservation(rulesWithObservationList);
        Get get = Mockito.mock(Get.class);
        Assert.assertEquals(result, table.get(get));
    }

    @Test
    public void testGetFulfilledConditionsForComponent() throws Exception{
        long lastTime = System.currentTimeMillis();
        byte[] row = Bytes.toBytes(String.format(ROW_FORMAT, accountId, componentId, lastTime));
        Mockito.when(result.getRow()).thenReturn(row);
        ScanProperties scanProperties = new ScanProperties();
        scanProperties.withStop(lastTime);
        List componentObservationList =  ruleConditionsHbaseRepository.getFulfilledConditionsForComponent(scanProperties);
        Assert.assertFalse(componentObservationList.isEmpty());
    }

    private UserConfig getUserConfig(){
        UserConfig config = UserConfig.empty()
                .withString(HbaseProperties.TABLE_PREFIX,TABLE_PREFIX)
                .withString(HbaseProperties.ZOOKEEPER_QUORUM, ZOOKEEPER_QUORUM);
        return  config;
    }

    private Observation createObservation(String value) {
        Observation observation = new Observation();
        observation.setCid(componentId);
        observation.setOn(observationTimestamp);
        observation.setValue(value);
        return observation;
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
