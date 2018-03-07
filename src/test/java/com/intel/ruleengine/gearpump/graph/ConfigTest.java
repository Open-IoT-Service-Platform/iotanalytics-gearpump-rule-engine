package com.intel.ruleengine.gearpump.graph;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigTest {
    private Config config;
    private static final String KAFKA_OBSERVATIONS_TOPIC = "KAFKA_OBSERVATIONS_TOPIC ";
    private static final String KAFKA_RULE_ENGINE_TOPIC = "KAFKA_RULE_ENGINE_TOPIC";
    private static final String APPLICATION_NAME = "APPLICATION_NAME";
    private static final String ZOOKEEPER_HBASE_QUORUM = "ZOOKEEPER_HBASE_QUORUM";
    private static final String HBASE_TABLE_PREFIX = "HBASE_TABLE_PREFIX";
    private static final String TOKEN = "TOKEN";
    private static final String DASHBOARD_URL = "DASHBOARD_URL";
    private static final String KAFKA_SERVERS = "KAFKA_SERVERS";
    private static final String KAFKA_ZOOKEEPER_QUORUM = "KAFKA_ZOOKEEPER_QUORUM";
    private static final String KRB_KDC = "KRB_KDC";
    private static final String KRB_PASS = "KRB_PASS";
    private static final String KRB_USER = "KRB_USER";
    private static final String KRB_REALM = "KRB_REALM";
    private static final String KRB_MASTER_PRINCIPAL = "KRB_MASTER_PRINCIPAL";
    private static final String KRB_REGIONSERVER_PRINCIPAL = "KRB_REGIONSERVER_PRINCIPAL";
    private static final String HADOOP_SECURITY_AUTHENTICATION = "HADOOP_SECURITY_AUTHENTICATION";

    @Before
    public void setUp() {
       config = new Config();
    }

    @Test
    public void testKafka_observations_topic(){
        config.setKafka_observations_topic(KAFKA_OBSERVATIONS_TOPIC);
        Assert.assertEquals(KAFKA_OBSERVATIONS_TOPIC, config.getKafka_observations_topic());
    }

    @Test
    public void testKafka_rule_engine_topic(){
        config.setKafka_rule_engine_topic(KAFKA_RULE_ENGINE_TOPIC);
        Assert.assertEquals(KAFKA_RULE_ENGINE_TOPIC, config.getKafka_rule_engine_topic());
    }

    @Test
    public void testApplication_name(){
        config.setApplication_name(APPLICATION_NAME);
        Assert.assertEquals(APPLICATION_NAME, config.getApplication_name());
    }

    @Test
    public void testZookeeper_hbase_quorum(){
        config.setZookeeper_hbase_quorum(ZOOKEEPER_HBASE_QUORUM);
        Assert.assertEquals(ZOOKEEPER_HBASE_QUORUM, config.getZookeeper_hbase_quorum());
    }

    @Test
    public void testHbase_table_prefix(){
        config.setHbase_table_prefix(HBASE_TABLE_PREFIX);
        Assert.assertEquals(HBASE_TABLE_PREFIX, config.getHbase_table_prefix());
    }

    @Test
    public void testToken(){
        config.setToken(TOKEN);
        Assert.assertEquals(TOKEN, config.getToken());
    }

    @Test
    public void testDashboard_url(){
        config.setDashboard_url(DASHBOARD_URL);
        Assert.assertEquals(DASHBOARD_URL, config.getDashboard_url());
    }

    @Test
    public void testKafka_servers(){
        config.setKafka_servers(KAFKA_SERVERS);
        Assert.assertEquals(KAFKA_SERVERS, config.getKafka_servers());
    }

    @Test
    public void testKafka_zookeeper_quorum(){
        config.setKafka_zookeeper_quorum(KAFKA_ZOOKEEPER_QUORUM);
        Assert.assertEquals(KAFKA_ZOOKEEPER_QUORUM, config.getKafka_zookeeper_quorum());
    }

    @Test
    public void testKrb_kdc(){
        config.setKrb_kdc(KRB_KDC);
        Assert.assertEquals(KRB_KDC, config.getKrb_kdc());
    }

    @Test
    public void testKrb_pass(){
        config.setKrb_password(KRB_PASS);
        Assert.assertEquals(KRB_PASS, config.getKrb_password());
    }

    @Test
    public void testKrb_user(){
        config.setKrb_user(KRB_USER);
        Assert.assertEquals(KRB_USER, config.getKrb_user());
    }

    @Test
    public void testKrb_realm() {
        config.setKrb_realm(KRB_REALM);
        Assert.assertEquals(KRB_REALM, config.getKrb_realm());
    }

    @Test
    public void testKrb_master_principal(){
        config.setKrb_master_principal(KRB_MASTER_PRINCIPAL);
        Assert.assertEquals(KRB_MASTER_PRINCIPAL, config.getKrb_master_principal());
    }

    @Test
    public void testKrb_regionserver_principal(){
        config.setKrb_regionserver_principal(KRB_REGIONSERVER_PRINCIPAL);
        Assert.assertEquals(KRB_REGIONSERVER_PRINCIPAL, config.getKrb_regionserver_principal());
    }

    @Test
    public void testHadoop_security_authentication(){
        config.setHadoop_security_authentication(HADOOP_SECURITY_AUTHENTICATION);
        Assert.assertEquals(HADOOP_SECURITY_AUTHENTICATION, config.getHadoop_security_authentication());
    }

    @Test
    public void testdashboard_strict_ssl(){
        config.setDashboard_strict_ssl(true);
        Assert.assertTrue(config.getDashboard_strict_ssl());
    }
}
