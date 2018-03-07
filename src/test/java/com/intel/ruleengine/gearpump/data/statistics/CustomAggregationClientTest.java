package com.intel.ruleengine.gearpump.data.statistics;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.coprocessor.ColumnInterpreter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class CustomAggregationClientTest {
    private static final String TABLE_NAME = "table_name";
    private static final long START_ROW = 0;
    private static final long END_ROW = 1;
    private TableName tableName = TableName.valueOf(TABLE_NAME);
    private Scan scan ;
    private CustomAggregationClient customAggregationClient;

    @Mock
    private Connection hbaseConnection;

    @Mock
    private Table table;

    @Mock
    private Map familyMap;

    @Mock
    private ColumnInterpreter columnInterpreter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        scan = new Scan();
        customAggregationClient = new CustomAggregationClient(hbaseConnection);
    }

    @Test
    public void testClose() throws Exception{
        Mockito.when(hbaseConnection.isClosed()).thenReturn(false);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Mockito.when(hbaseConnection.isClosed()).thenReturn(true);
                return null;
            }
        }).when(hbaseConnection).close();
        customAggregationClient.close();
        Assert.assertEquals(true, hbaseConnection.isClosed());
    }

    @Test(expected = IOException.class)
    public void testMaxWithMultiFamily() throws Throwable{
        Mockito.when(hbaseConnection.getTable(Matchers.any())).thenReturn(table);
        Mockito.when(familyMap.size()).thenReturn(2);
        scan.setStartRow(Bytes.toBytes(START_ROW));
        scan.setStopRow(Bytes.toBytes(END_ROW));
        scan.setFamilyMap(familyMap);
        customAggregationClient.max(tableName,columnInterpreter, scan);
    }

    @Test
    public void testMax() throws Throwable{
        Mockito.when(hbaseConnection.getTable(Matchers.any())).thenReturn(table);
        Mockito.when(familyMap.size()).thenReturn(1);
        scan.setStartRow(Bytes.toBytes(START_ROW));
        scan.setStopRow(Bytes.toBytes(END_ROW));
        scan.setFamilyMap(familyMap);
        customAggregationClient.max(tableName,columnInterpreter, scan);
    }

    @Test
    public void testRowCount() throws  Throwable{
        Mockito.when(hbaseConnection.getTable(Matchers.any())).thenReturn(table);
        Assert.assertEquals(0, customAggregationClient.rowCount(tableName,columnInterpreter,scan));
    }

    @Test
    public void testStd() throws  Throwable{
        Mockito.when(hbaseConnection.getTable(Matchers.any())).thenReturn(table);
        Mockito.when(familyMap.size()).thenReturn(1);
        scan.setFamilyMap(familyMap);
        Assert.assertEquals(0.0, customAggregationClient.std(tableName,columnInterpreter,scan),0.00001);
    }

    @Test
    public void testAvg() throws Throwable{
        Mockito.when(hbaseConnection.getTable(Matchers.any())).thenReturn(table);
        Mockito.when(familyMap.size()).thenReturn(1);
        scan.setFamilyMap(familyMap);
        Assert.assertEquals(0.0, customAggregationClient.avg(tableName, columnInterpreter, scan), 0.00001);
    }
}
