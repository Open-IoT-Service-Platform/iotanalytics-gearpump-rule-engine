package com.intel.ruleengine.gearpump.apiclients.alerts;

import com.intel.ruleengine.gearpump.apiclients.InvalidDashboardResponseException;
import com.intel.ruleengine.gearpump.tasks.messages.Observation;
import com.intel.ruleengine.gearpump.apiclients.DashboardConfigProvider;
import com.intel.ruleengine.gearpump.tasks.messages.Rule;
import com.intel.ruleengine.gearpump.tasks.messages.RulesWithObservation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class DashboardAlertsApiTest {
    private static final String TOKEN = "token";
    private static final String URL = "localhost";

    private DashboardAlertsApi dashboardAlertsApi;
    private List<RulesWithObservation> rulesWithObservationsList;

    @Mock
    private ResponseEntity<String> responseEntity;

    @Mock
    private RestTemplate template;

    @Mock
    private DashboardConfigProvider dashboardConfig;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(template.exchange(Matchers.any(String.class), Matchers.any(HttpMethod.class),
                Matchers.any(HttpEntity.class),Matchers.any(Class.class))).thenReturn(responseEntity);
        Mockito.when(dashboardConfig.getToken()).thenReturn(TOKEN);
        Mockito.when(dashboardConfig.getUrl()).thenReturn(URL);

        dashboardAlertsApi = new DashboardAlertsApi(dashboardConfig, template);
        rulesWithObservationsList = createRulesWithObservationsList();
    }

    @Test
    public void testPushAlertWithOKStatus() throws  Exception{
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        dashboardAlertsApi.pushAlert(rulesWithObservationsList);
    }

    @Test(expected = InvalidDashboardResponseException.class)
    public void testPushAlertWithInvalidDashboardResponseException()throws Exception{
        Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        dashboardAlertsApi.pushAlert(rulesWithObservationsList);
    }

    private List<RulesWithObservation> createRulesWithObservationsList(){
        List<Rule> ruleList = Arrays.asList(new Rule());
        Map<String, String > attrs = new HashMap<>();
        attrs.put("Key1","Value1");
        attrs.put("Key2","Value2");

        Observation observation = new Observation();
        observation.setAid("aid");
        observation.setOn(10L);
        observation.setValue("value");
        observation.setAttributes(attrs);
        observation.setLoc(Arrays.asList(1.0));
        observation.setSystemOn(1L);

        return Arrays.asList(new RulesWithObservation(observation,ruleList));
    }


}
