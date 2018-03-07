package com.intel.ruleengine.gearpump.apiclients.rules;

import com.intel.ruleengine.gearpump.apiclients.InvalidDashboardResponseException;
import com.intel.ruleengine.gearpump.apiclients.DashboardConfigProvider;
import com.intel.ruleengine.gearpump.apiclients.rules.model.ComponentRulesResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class DashboardRulesApiTest {
    private static final String TOKEN = "Token";
    private static final String URL = "LocalHost";

    private DashboardRulesApi dashboardRulesApi;

    @Mock
    private ResponseEntity<String> responseEntity;

    @Mock
    private DashboardConfigProvider dashboardConfig;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(dashboardConfig.getToken()).thenReturn(TOKEN);
        Mockito.when(dashboardConfig.getUrl()).thenReturn(URL);

        dashboardRulesApi = new DashboardRulesApi(dashboardConfig, restTemplate);
    }

    @Test
    public void testGetActiveComponentsRules()throws Exception{
        Mockito.when(restTemplate.exchange(Matchers.any(String.class), Matchers.any(HttpMethod.class),
                Matchers.any(HttpEntity.class),Matchers.any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        List<ComponentRulesResponse> componentRulesResponseList =  dashboardRulesApi.getActiveComponentsRules();
        Assert.assertNull(componentRulesResponseList);
    }

    @Test(expected = InvalidDashboardResponseException.class)
    public void testGetActiveComponentsRulesWithInvalidDashboardResponseException() throws Exception{
        Mockito.when(restTemplate.exchange(Matchers.any(String.class), Matchers.any(HttpMethod.class),
                Matchers.any(HttpEntity.class),Matchers.any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        dashboardRulesApi.getActiveComponentsRules();
    }

    @Test
    public void testMarkRulesSynchronized() throws Exception{
        Set<String> rulesId = new HashSet<String>();
        rulesId.add("Id1");

        Mockito.when(restTemplate.exchange(Matchers.any(String.class), Matchers.any(HttpMethod.class),
                Matchers.any(HttpEntity.class),Matchers.any(Class.class))).
                thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocationOnMock) {
                        Mockito.when(responseEntity.getBody()).thenReturn(rulesId.toString());
                        return responseEntity;
                    }
                });
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        dashboardRulesApi.markRulesSynchronized(rulesId);
        Assert.assertEquals(rulesId.toString(),responseEntity.getBody());
    }

    @Test(expected = InvalidDashboardResponseException.class)
    public void testMarkRulesSynchronizedWithInvalidDashboardResponseException() throws Exception{
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        Mockito.when(restTemplate.exchange(Matchers.any(String.class), Matchers.any(HttpMethod.class),
                Matchers.any(HttpEntity.class),Matchers.any(Class.class))).thenReturn(responseEntity);
        Set<String> rulesId = new HashSet<String>();
        rulesId.add("id1");
        dashboardRulesApi.markRulesSynchronized(rulesId);
    }



}
