package org.wso2.carbon.event.simulator.core.internal.generator;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.event.simulator.core.exception.InsufficientAttributesException;
import org.wso2.carbon.event.simulator.core.exception.InvalidConfigException;
import org.wso2.carbon.event.simulator.core.service.EventSimulatorDataHolder;
import org.wso2.carbon.stream.processor.common.exception.ResourceNotFoundException;
import org.wso2.siddhi.query.api.definition.Attribute;
import util.StreamProcessorUtil;

import java.util.ArrayList;
import java.util.Arrays;


public class SingleEventGeneratorTest {
    private static final String validSingleEventConfig = "{\n" +
            "  \"streamName\": \"FooStream\",\n" +
            "  \"executionPlanName\": \"TestExecutionPlan\",\n" +
            "  \"timestamp\": \"1488615136958\",\n" +
            "  \"data\": [\n" +
            "    null,\n" +
            "    \"9\",\n" +
            "    \"45\"\n" +
            "  ]\n" +
            "}";
    private static final String withoutTimestamp = "{\n" +
            "  \"streamName\": \"FooStream\",\n" +
            "  \"executionPlanName\": \"TestExecutionPlan\",\n" +
            "  \"data\": [\n" +
            "    null,\n" +
            "    \"9\",\n" +
            "    \"45\"\n" +
            "  ]\n" +
            "}";
    private static final String executionPlanNotDeployed = "{\n" +
            "  \"streamName\": \"FooStream\",\n" +
            "  \"executionPlanName\": \"ExecutionPlan\",\n" +
            "  \"timestamp\": \"1488615136958\",\n" +
            "  \"data\": [\n" +
            "    null,\n" +
            "    \"9\",\n" +
            "    \"45\"\n" +
            "  ]\n" +
            "}";
    private static final String streamNotFound = "{\n" +
            "  \"streamName\": \"FooStream1\",\n" +
            "  \"executionPlanName\": \"TestExecutionPlan\",\n" +
            "  \"timestamp\": \"1488615136958\",\n" +
            "  \"data\": [\n" +
            "    null,\n" +
            "    \"9\",\n" +
            "    \"45\"\n" +
            "  ]\n" +
            "}";
    private static final String streamNameNotProvided = "{\n" +
            "  \"streamName\": \"\",\n" +
            "  \"executionPlanName\": \"TestExecutionPlan\",\n" +
            "  \"timestamp\": \"1488615136958\",\n" +
            "  \"data\": [\n" +
            "    null,\n" +
            "    \"9\",\n" +
            "    \"45\"\n" +
            "  ]\n" +
            "}";
    private static final String executionPlanNameNotProvided = "{\n" +
            "  \"streamName\": \"FooStream1\",\n" +
            "  \"executionPlanName\": null,\n" +
            "  \"timestamp\": \"1488615136958\",\n" +
            "  \"data\": [\n" +
            "    null,\n" +
            "    \"9\",\n" +
            "    \"45\"\n" +
            "  ]\n" +
            "}";
    private static final String insufficientAttributesProvided = "{\n" +
            "  \"streamName\": \"FooStream\",\n" +
            "  \"executionPlanName\": \"TestExecutionPlan\",\n" +
            "  \"timestamp\": \"1488615136958\",\n" +
            "  \"data\": [\n" +
            "    null,\n" +
            "    \"9\"\n" +
            "  ]\n" +
            "}";

    @BeforeClass
    public void setUp() throws Exception {
        EventSimulatorDataHolder.getInstance().setEventStreamService(new StreamProcessorUtil());
        StreamProcessorUtil streamProcessorUtil = (StreamProcessorUtil) EventSimulatorDataHolder.getInstance()
                .getEventStreamService();
        streamProcessorUtil.addStreamAttributes("TestExecutionPlan", "FooStream",
                new ArrayList<Attribute>() {
                    {
                        add(new Attribute("symbol", Attribute.Type.STRING));
                        add(new Attribute("price", Attribute.Type.FLOAT));
                        add(new Attribute("volume", Attribute.Type.LONG));

                    }
                });
    }

    @Test
    public void testEventWithAllProperties() throws Exception {
        SingleEventGenerator.sendEvent(validSingleEventConfig);
        StreamProcessorUtil streamProcessorUtil = (StreamProcessorUtil) EventSimulatorDataHolder.getInstance()
                .getEventStreamService();
        Assert.assertEquals("TestExecutionPlan", streamProcessorUtil.getEventsReceived().get(0)
                .getExecutionPlanName());
        Assert.assertEquals("FooStream", streamProcessorUtil.getEventsReceived().get(0).getStreamName());
        Object[] data = new Object[]{null, 9f, 45L};
        Assert.assertEquals(1488615136958L, streamProcessorUtil.getEventsReceived().get(0).getEvent()
                .getTimestamp());
        Assert.assertEquals(Arrays.deepToString(data), Arrays.deepToString(streamProcessorUtil.getEventsReceived()
                .get(0).getEvent().getData()));
    }

    @Test
    public void testEventWithoutTimestamp() throws Exception {
        long startTimestamp = System.currentTimeMillis();
        SingleEventGenerator.sendEvent(withoutTimestamp);
        long endTimestamp = System.currentTimeMillis();
        StreamProcessorUtil streamProcessorUtil = (StreamProcessorUtil) EventSimulatorDataHolder.getInstance()
                .getEventStreamService();
        Assert.assertFalse(streamProcessorUtil.getEventsReceived().isEmpty());
        long eventTimestamp = streamProcessorUtil.getEventsReceived().get(0).getEvent().getTimestamp();
        Assert.assertTrue(eventTimestamp >= startTimestamp && eventTimestamp <= endTimestamp);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testExecutionPlanNotDeployed() throws Exception {
        SingleEventGenerator.sendEvent(executionPlanNotDeployed);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testStreamNotDeployed() throws Exception {
        SingleEventGenerator.sendEvent(streamNotFound);
    }

    @Test(expectedExceptions = InvalidConfigException.class)
    public void testStreamNotAvailable() throws Exception {
        SingleEventGenerator.sendEvent(streamNameNotProvided);
    }

    @Test(expectedExceptions = InvalidConfigException.class)
    public void testExecutionPlanNameNotAvailable() throws Exception {
        SingleEventGenerator.sendEvent(executionPlanNameNotProvided);
    }

    @Test(expectedExceptions = InsufficientAttributesException.class)
    public void testInsufficientEventData() throws Exception {
        SingleEventGenerator.sendEvent(insufficientAttributesProvided);

    }

    @BeforeMethod
    public void init() {
        ((StreamProcessorUtil) EventSimulatorDataHolder.getInstance().getEventStreamService()).resetEvents();
    }

    @AfterClass
    public void tearDown() throws Exception {
        EventSimulatorDataHolder.getInstance().setEventStreamService(null);
    }

}
