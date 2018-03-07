package com.intel.ruleengine.gearpump.graph;

import com.google.gson.Gson;
import com.intel.ruleengine.gearpump.apiclients.DashboardConfig;
import io.gearpump.cluster.UserConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GraphConfigTest {
    private static final String APPLCATION_NAME = "ApplicationName";
    private static final boolean SSL_VERIFCATION = false;
    private GraphConfig graphConfig;

    @Before
    public void setUp(){
        Config config = new Config();
        config.setDashboard_strict_ssl(SSL_VERIFCATION);
        config.setApplication_name(APPLCATION_NAME);
        Gson gson = new Gson();
        graphConfig = new GraphConfig(gson.toJson(config));
    }

    @Test
    public void testGetConfig(){
        UserConfig userConfig = graphConfig.getConfig();
        Assert.assertEquals(SSL_VERIFCATION,Boolean.valueOf(userConfig.getString(DashboardConfig
                .DASHBOARD_STRICT_SSL_VERIFICATION).get()));
    }

    @Test
    public void testGetGraphApplicationName(){
        Assert.assertEquals(APPLCATION_NAME, graphConfig.getGraphApplicationName());
    }
}
