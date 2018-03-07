package com.intel.ruleengine.gearpump.data.rules;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.intel.ruleengine.gearpump.data.HbaseProperties;
import com.intel.ruleengine.gearpump.rules.IdGenerator;
import com.intel.ruleengine.gearpump.rules.Operators;
import com.intel.ruleengine.gearpump.rules.RuleCreator;
import com.intel.ruleengine.gearpump.rules.RuleStatus;
import com.intel.ruleengine.gearpump.tasks.messages.Rule;
import io.gearpump.cluster.UserConfig;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class RulesHbaseRepositoryTest {
    private static final String TABLE_PREFIX = "MockTablePrefix";
    private static final String ZOOKEEPER_QUORUM = "MockZookeeperQuorum";
    private final String accountId = IdGenerator.generateId();
    private final String componentId = IdGenerator.generateId();

    private List<Rule> ruleList;
    private ImmutableMap<String, List<Rule>> newComponentsRules;
    private byte[] row;

    @Mock
    private Connection hbaseConnection;

    @Mock
    private ResultScanner resultScanner;

    @Mock
    private  Table table;

    @Mock
    private Iterator iterator;

    @Mock
    private Result result;

    @Mock
    private Admin admin;

    private  UserConfig userConfig = getUserConfig();

    @InjectMocks
    private  RulesHbaseRepository rulesHbaseRepository  = new RulesHbaseRepository(userConfig);

    @Before
    public void setUp() throws  Exception{
        MockitoAnnotations.initMocks(this);
        row = new RulesRowCreator(accountId, componentId).createRow();
        ruleList = CreateRuleList();
        newComponentsRules = CreateComponentsRules();

        Mockito.when(hbaseConnection.getTable(Matchers.any())).thenReturn(table);
        Mockito.when(hbaseConnection.getAdmin()).thenReturn(admin);
        Mockito.when(table.getScanner(Matchers.any(Scan.class))).thenReturn(resultScanner);
        Mockito.when(resultScanner.iterator()).thenReturn(iterator);
        Mockito.when(iterator.hasNext()).thenReturn(true, false);
        Mockito.when(iterator.next()).thenReturn(result);
        Mockito.when(result.getRow()).thenReturn(row);
    }
    @Test
    public void testputRulesAndRemoveNotExistingOnes() throws  Exception{
        Gson gson = new Gson();
        Mockito.when(result.getValue(Matchers.anyObject(),Matchers.anyObject())).thenReturn(Bytes.toBytes(gson.toJson(ruleList)));
        rulesHbaseRepository.putRulesAndRemoveNotExistingOnes(newComponentsRules);
    }
    @Test
    public void testGetComponentsRules() throws Exception{
        HashSet<String> componentIdSet = new HashSet<>();
        componentIdSet.add(componentId);
        Map<String, List<Rule>> resultMap = rulesHbaseRepository.getComponentsRules(accountId,componentIdSet);
        Assert.assertTrue( resultMap.keySet().contains(componentId));
    }

    @Test
    public void testCreateTable() throws Exception{
        Mockito.when(admin.tableExists(Matchers.any())).thenReturn(false);
        rulesHbaseRepository.createTable();
    }

    private List<Rule> CreateRuleList(){
        Rule activeRule = RuleCreator.createRuleWithSingleCondition(Operators.EQUAL, "5", accountId);
        Rule notActiveRule = RuleCreator.copyRule(activeRule);
        notActiveRule.setStatus(RuleStatus.DELETED);
        List<Rule> rules;
        rules = new ArrayList<>();
        rules.add(activeRule);
        rules.add(notActiveRule);
        return rules;
    }

    private ImmutableMap<String, List<Rule>> CreateComponentsRules(){
        return ImmutableMap.of(
                accountId, ruleList,
                componentId, ruleList
        );
    }

    private UserConfig getUserConfig(){
        UserConfig config = UserConfig.empty()
                .withString(HbaseProperties.TABLE_PREFIX,TABLE_PREFIX)
                .withString(HbaseProperties.ZOOKEEPER_QUORUM, ZOOKEEPER_QUORUM);
        return  config;
    }

}
