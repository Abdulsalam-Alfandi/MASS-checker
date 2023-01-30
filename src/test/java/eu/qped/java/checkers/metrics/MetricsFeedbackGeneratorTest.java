package eu.qped.java.checkers.metrics;

import eu.qped.java.checkers.metrics.data.feedback.DefaultMetricSuggestion;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackGenerator;
import eu.qped.java.checkers.metrics.data.feedback.DefaultMetricSuggestion;
import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;
import eu.qped.java.checkers.metrics.settings.MetricSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link MetricsFeedbackGenerator}.
 *
 * @author Jannik Seus
 */
class MetricsFeedbackGeneratorTest {

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateSuggestion(Metric metric) {
        assertEquals("You are within the " + metric.toString() + "'s threshold.", MetricsFeedbackGenerator.generateDefaultSuggestion(metric, false, false));
        assertEquals("The " + metric + "'s value is too low: " + MetricsFeedbackGenerator.generateMetricSpecificSuggestion(metric, true), MetricsFeedbackGenerator.generateDefaultSuggestion(metric, true, false));
        assertEquals("The " + metric + "'s value is too high: " + MetricsFeedbackGenerator.generateMetricSpecificSuggestion(metric, false), MetricsFeedbackGenerator.generateDefaultSuggestion(metric, false, true));
        assertThrows(IllegalArgumentException.class, () -> MetricsFeedbackGenerator.generateDefaultSuggestion(metric, true, true));
    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateMetricSpecificSuggestion(Metric metric) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method generateMetricSpecificSuggestionLowerMethod = MetricsFeedbackGenerator.class.getDeclaredMethod("generateMetricSpecificSuggestionLower", Metric.class);
        generateMetricSpecificSuggestionLowerMethod.setAccessible(true);
        assertEquals(MetricsFeedbackGenerator.generateMetricSpecificSuggestion(metric, true), generateMetricSpecificSuggestionLowerMethod.invoke(null, metric));
        generateMetricSpecificSuggestionLowerMethod.setAccessible(false);

        Method generateMetricSpecificSuggestionUpperMethod = MetricsFeedbackGenerator.class.getDeclaredMethod("generateMetricSpecificSuggestionUpper", Metric.class);
        generateMetricSpecificSuggestionUpperMethod.setAccessible(true);
        assertEquals(MetricsFeedbackGenerator.generateMetricSpecificSuggestion(metric, false), generateMetricSpecificSuggestionUpperMethod.invoke(null, metric));
        generateMetricSpecificSuggestionUpperMethod.setAccessible(false);
    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateMetricSpecificSuggestionLower(Metric metric) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method generateMetricSpecificSuggestionLowerMethod = MetricsFeedbackGenerator.class.getDeclaredMethod("generateMetricSpecificSuggestionLower", Metric.class);
        generateMetricSpecificSuggestionLowerMethod.setAccessible(true);
        DefaultMetricSuggestion defaultMetricSuggestionByMetric = getSuggestionByMetric(metric);
        assert defaultMetricSuggestionByMetric != null;
        assertEquals(defaultMetricSuggestionByMetric.getLowerBoundReachedSuggestion(), generateMetricSpecificSuggestionLowerMethod.invoke(null, metric));
        InvocationTargetException illegalArgument = assertThrows(InvocationTargetException.class, () -> generateMetricSpecificSuggestionLowerMethod.invoke(null, new Object[]{null}));
        assertEquals(IllegalArgumentException.class.getName(), illegalArgument.getTargetException().getClass().getName());
        generateMetricSpecificSuggestionLowerMethod.setAccessible(false);

    }

    @ParameterizedTest
    @EnumSource(Metric.class)
    void generateMetricSpecificSuggestionUpper(Metric metric) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method generateMetricSpecificSuggestionUpperMethod = MetricsFeedbackGenerator.class.getDeclaredMethod("generateMetricSpecificSuggestionUpper", Metric.class);
        generateMetricSpecificSuggestionUpperMethod.setAccessible(true);
        DefaultMetricSuggestion defaultMetricSuggestionByMetric = getSuggestionByMetric(metric);
        assert defaultMetricSuggestionByMetric != null;
        assertEquals(defaultMetricSuggestionByMetric.getUpperBoundReachedSuggestion(), generateMetricSpecificSuggestionUpperMethod.invoke(null, metric));
        InvocationTargetException illegalArgument = assertThrows(InvocationTargetException.class, () -> generateMetricSpecificSuggestionUpperMethod.invoke(null, new Object[]{null}));
        assertEquals(IllegalArgumentException.class.getName(), illegalArgument.getTargetException().getClass().getName());
        generateMetricSpecificSuggestionUpperMethod.setAccessible(false);
    }

    // Method generateDesignFeedbacks() already covered in DesignCheckerTest.java

    private static DefaultMetricSuggestion getSuggestionByMetric(Metric metric) {
        switch (metric) {
            case AMC:
                return DefaultMetricSuggestion.AMC;
            case CA:
                return DefaultMetricSuggestion.CA;
            case CAM:
                return DefaultMetricSuggestion.CAM;
            case CBM:
                return DefaultMetricSuggestion.CBM;
            case CBO:
                return DefaultMetricSuggestion.CBO;
            case CC:
                return DefaultMetricSuggestion.CC;
            case CE:
                return DefaultMetricSuggestion.CE;
            case DAM:
                return DefaultMetricSuggestion.DAM;
            case DIT:
                return DefaultMetricSuggestion.DIT;
            case IC:
                return DefaultMetricSuggestion.IC;
            case LCOM:
                return DefaultMetricSuggestion.LCOM;
            case LCOM3:
                return DefaultMetricSuggestion.LCOM3;
            case LOC:
                return DefaultMetricSuggestion.LOC;
            case MFA:
                return DefaultMetricSuggestion.MFA;
            case MOA:
                return DefaultMetricSuggestion.MOA;
            case NOC:
                return DefaultMetricSuggestion.NOC;
            case NPM:
                return DefaultMetricSuggestion.NPM;
            case RFC:
                return DefaultMetricSuggestion.RFC;
            case WMC:
                return DefaultMetricSuggestion.WMC;
        }
        return null;
    }

    @Test
    void isThresholdReachedMetricNull() throws NoSuchMethodException {
        Method isThresholdReachedMethod = MetricsFeedbackGenerator.class.getDeclaredMethod("isThresholdReached", Metric.class, MetricSettings.class, double.class, boolean.class);
        isThresholdReachedMethod.setAccessible(true);
        InvocationTargetException illegalArgument = assertThrows(InvocationTargetException.class, () -> isThresholdReachedMethod.invoke(null, null, null, 0d, false));
        assertEquals(IllegalArgumentException.class.getName(), illegalArgument.getTargetException().getClass().getName());

        isThresholdReachedMethod.setAccessible(false);
    }
}