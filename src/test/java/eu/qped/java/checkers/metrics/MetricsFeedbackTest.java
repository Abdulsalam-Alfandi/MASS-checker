package eu.qped.java.checkers.metrics;

import eu.qped.java.checkers.metrics.data.feedback.DefaultMetricSuggestion;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedback;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackGenerator;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackSuggestion;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessage;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageSingle;
import eu.qped.java.checkers.metrics.settings.MetricConfig;
import eu.qped.java.checkers.metrics.settings.MetricSettings;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsEntry;
import eu.qped.java.checkers.metrics.settings.MetricThreshold;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Test class for {@link MetricsFeedback}.
 *
 * @author Jannik Seus
 */
class MetricsFeedbackTest {

    private MetricsFeedback metricsFeedback1;

    @BeforeEach
    void setUp() {
        metricsFeedback1 = MetricsFeedback.builder()
                .className("TestClass")
                .metric(Metric.AMC)
                .value(99d)
                .body(Metric.AMC.getDescription())
                .suggestion("Change something!")
                .build();
    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateSuggestionTestMetric(Metric metric) {
        metricsFeedback1.setMetric(metric);
        metricsFeedback1.setBody(metric.getDescription());

        assertEquals(metric, metricsFeedback1.getMetric());
    }

    @Test
   // @ValueSource(doubles = {-1d, 0d, 0.5d, 1.0d, 3.3d})
    void generateSuggestionTestValue() {

        MetricThreshold metricThreshold = new MetricThreshold(Metric.AMC,0,50,false);
        MetricConfig metricConfig = new MetricConfig(metricThreshold, null);
        MetricSettings  metricSettings = new MetricSettings();
        metricSettings.setAmcConfig(metricConfig);
        HashMap<Metric, MetricsFeedbackSuggestion> feedbackSuggestionMap = new HashMap<>();
        feedbackSuggestionMap.put(Metric.AMC,null);
        metricSettings.setCustomSuggestions(feedbackSuggestionMap);

        ClassMetricsMessageSingle message = new ClassMetricsMessageSingle(Metric.AMC,-1);
        ArrayList<ClassMetricsMessage> list = new ArrayList<>();
        list.add(message);
        ClassMetricsEntry entry = new ClassMetricsEntry("TestClass", list);
        List<ClassMetricsEntry> entryList = Collections.singletonList( entry);

        MetricsFeedbackGenerator feedbackGenerator = new MetricsFeedbackGenerator();


        assertEquals
                (DefaultMetricSuggestion.AMC.getLowerBoundReachedSuggestion(),
                        feedbackGenerator.generateMetricsCheckerFeedbacks(entryList,metricSettings).get(0));

       /* assertEquals
                ("The " + Metric.AMC + "'s value is too high: Decrease your average method size, e.g. by delegating functionalities to other newly created methods.",
                        MetricsFeedbackGenerator.generateDefaultSuggestion(
                                Metric.AMC, false, true));

        assertThrows(IllegalArgumentException.class,
                () -> MetricsFeedbackGenerator.generateDefaultSuggestion(
                        Metric.AMC,
                        true,
                        true));
        metricsFeedback1.setValue(99d);
        assertEquals(99d, metricsFeedback1.getValue());

        */
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "TestClass", "AnotherTestClass.java", "RandomName"})
    void generateSuggestionTest(String className) {
        metricsFeedback1.setClassName(className);
        assertEquals(className, metricsFeedback1.getClassName());

    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateMetricsCheckerFeedbackTest() {
        MetricsFeedbackGenerator feedbackGenerator = new MetricsFeedbackGenerator();
        MetricSettings metricSettings = MetricSettings.builder().build();
        List<ClassMetricsEntry> metricCheckerEntries =
                List.of(mock(ClassMetricsEntry.class), mock(ClassMetricsEntry.class), mock(ClassMetricsEntry.class),
                        mock(ClassMetricsEntry.class), mock(ClassMetricsEntry.class), mock(ClassMetricsEntry.class));

        assertEquals(feedbackGenerator.generateMetricsCheckerFeedbacks(metricCheckerEntries, metricSettings), List.of());
    }

    @Test
    void testToString() {
        assertEquals(
                "In class 'TestClass.java'\n" +
                        "AMC (Average Method Complexity)\n" +
                        "Measured at: 99.0\n" +
                        "Change something!",
                metricsFeedback1.toString());
        assertEquals(
                "In class 'TestClass.java'\n" +
                        "AMC (Average Method Complexity)\n" +
                        "Measured at: 99.0\n" +
                        "Change something!",
                metricsFeedback1.toString());

        assertEquals(
                "In class 'TestClass.java'\n" +
                        "AMC (Average Method Complexity)\n" +
                        "Measured at: 99.0\n" +
                        "Change something!",
                metricsFeedback1.toString());

        metricsFeedback1 = MetricsFeedback.builder().build();
        assertNotNull(metricsFeedback1);
    }
}