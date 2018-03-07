package com.intel.ruleengine.gearpump.data;

import io.gearpump.cluster.UserConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class HbaseConnManagerTest {
    private static final String ZOOKEEPER_QUORUM = "MockZookeeperQuorum";
    private HbaseConnManager hbaseConnManager;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        UserConfig userConfig = getUserConfig();
        KerberosProperties kerberosProperties = KerberosProperties.fromConfig(userConfig);
        hbaseConnManager = HbaseConnManager.newInstance(kerberosProperties,ZOOKEEPER_QUORUM);
    }

    @Test(expected = IOException.class)
    public void testCreateConnectionWithIOException() throws Exception{
        hbaseConnManager.create();
    }

    private UserConfig getUserConfig(){
        UserConfig config = UserConfig.empty()
                .withString(HbaseProperties.AUTHENTICATION_METHOD, HbaseProperties.KERBEROS_AUTHENTICATION)
                .withString(KerberosProperties.KRB_KDC, "kdc")
                .withString(KerberosProperties.KRB_REALM, "realm")
                .withString(KerberosProperties.KRB_USER, "user")
                .withString(KerberosProperties.KRB_PASS, "password")
                .withString(KerberosProperties.KRB_MASTER_PRINCIPAL, "master_principal")
                .withString(KerberosProperties.KRB_REGIONSERVER_PRINCIPAL, "regionserver_principal");
        return  config;
    }
}
