package com.intel.ruleengine.gearpump.apiclients;

import io.gearpump.cluster.UserConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DashboardConfigProviderTest {
    private static final String DASHBOARD_TOKEN_PROPERTY= "Token";
    private static final String DASHBOARD_URL_PROPERTY= "LocalHost";
    private static final String DASHBOARD_STRICT_SSL_VERIFICATION= "true";
    private DashboardConfigProvider dashboardConfigProvider;
    private UserConfig userConfig;

    @Before
    public void setUp() {
        userConfig = UserConfig.empty()
                .withString(DashboardConfig.DASHBOARD_TOKEN_PROPERTY, DASHBOARD_TOKEN_PROPERTY)
                .withString(DashboardConfig.DASHBOARD_URL_PROPERTY, DASHBOARD_URL_PROPERTY )
                .withString(DashboardConfig.DASHBOARD_STRICT_SSL_VERIFICATION, DASHBOARD_STRICT_SSL_VERIFICATION);
        dashboardConfigProvider = new DashboardConfigProvider(userConfig);
    }

    @Test
    public void testGetUrl(){
        String url = dashboardConfigProvider.getUrl();
        Assert.assertEquals(DASHBOARD_URL_PROPERTY, url);
    }

    @Test
    public void testGetToken(){
        String token = dashboardConfigProvider.getToken();
        Assert.assertEquals(DASHBOARD_TOKEN_PROPERTY, token);
    }

    @Test
    public void  testIsStrictSSL(){
        boolean isStrictSSL = dashboardConfigProvider.isStrictSSL();
        Assert.assertEquals(Boolean.valueOf(DASHBOARD_STRICT_SSL_VERIFICATION), isStrictSSL);
    }
}
