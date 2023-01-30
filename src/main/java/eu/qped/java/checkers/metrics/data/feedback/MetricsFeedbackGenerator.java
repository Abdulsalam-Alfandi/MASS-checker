package eu.qped.java.checkers.metrics.data.feedback;

import eu.qped.java.checkers.metrics.data.report.ClassMetricsEntry;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessage;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageMulti;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageSingle;
import eu.qped.java.checkers.metrics.settings.MetricSettings;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;

/**
 * Represents helper class to generate design feedback suggestions as strings.
 *
 * @author Jannik Seus
 */
public class MetricsFeedbackGenerator {



    /**
     * Generates the DesignFeedback to the corresponding classes, metrics, and designSettings (min/max thresholds)
     *
     * @param metricsMap     the map containing classnames, metrics and corresponding values
     * @param metricSettings the settings on which the feedback depends on //TODO wip configure design settings
     * @return the generated Feedback as a List.
     */
    public static List<MetricsFeedback> generateMetricsCheckerFeedbacks(List<ClassMetricsEntry> metricsMap, MetricSettings
            metricSettings) {
        List<MetricsFeedback> feedbacks = new ArrayList<>();

        metricsMap.forEach(classMetricsEntry -> {

            String className = classMetricsEntry.getClassName();
            List<ClassMetricsMessage> metricsForClass = classMetricsEntry.getMetricsForClass();

            if (metricsForClass != null) {
                metricsForClass.forEach(metricForClass -> {
                    boolean lowerThresholdReached;
                    boolean upperThresholdReached;
                    Metric metric = metricForClass.getMetric();
                    String suggestionString = "";
                    double metricValue = 0;
                    Map<String, Integer> metricValues;

                    if (metricForClass instanceof ClassMetricsMessageSingle) {
                        metricValue = (double) ((ClassMetricsMessageSingle) metricForClass).getMetricValue();

                        LowerOrUpperBound lowerOrUpper = isThresholdReached(metric, metricSettings, metricValue);
                        MetricsFeedbackSuggestion customSuggestion = metricSettings.getCustomSuggestions().get(metric);
                        suggestionString = lowerOrUpper.generateDefaultSuggestion(metric, customSuggestion);
                        
                    } else {
                        metricValues = ((ClassMetricsMessageMulti) metricForClass).getMetricValues();
                        for (Map.Entry<String, Integer> entry : metricValues.entrySet()) {
                            suggestionString = "For method " + MarkdownFormatterUtility.asCodeLine(entry.getKey()) + ":\n";
                            LowerOrUpperBound lowerOrUpper = isThresholdReached(metric, metricSettings, entry.getValue());

                            suggestionString += lowerOrUpper.generateDefaultSuggestion(metric, metricSettings.getCustomSuggestions().get(metric));
                            metricValue = (double) entry.getValue();
                        }
                    }

                    boolean addFeedback = suggestionString == "";
                    if (addFeedback) {
                        feedbacks.add(new MetricsFeedback(className, metric.getDescription(), metric, metricValue, suggestionString));
                    }
                    
                });
            }
        });
        return feedbacks;
    }

    private static String generateCustomSuggestion(MetricsFeedbackSuggestion customSuggestion, boolean lowerThresholdReached, boolean upperThresholdReached) {
        if (lowerThresholdReached && !upperThresholdReached) {
            return customSuggestion.getSuggestionLowerBoundExceeded();
        } else if (upperThresholdReached && !lowerThresholdReached) {
            return customSuggestion.getSuggestionUpperBoundExceeded();
        } else if (!upperThresholdReached) {
            return "";
        } else {
            throw new IllegalStateException("Both thresholds cannot be exceeded at the same time.");
        }
    }

    /**
     * Checks whether the lower or upper threshold of a given metricThreshold was exceeded.
     *
     * @param metric         the given metric
     * @param metricSettings the settings for design guidelines
     * @param value          the given metricThreshold's value
     * @return whether the minimum (lower=true) or maximum (lower=false) threshold was exceeded.
     */
    private static LowerOrUpperBound isThresholdReached(Metric metric, MetricSettings metricSettings, double value) {


            if (metric != null) {
                switch (metric) {
                    case AMC:
                        return new LowerOrUpperBound(
                                value < metricSettings.getAmcConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getAmcConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case CAM:
                        return new LowerOrUpperBound(
                                value < metricSettings.getCamConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getCamConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case CA:
                        return new LowerOrUpperBound(
                                value < metricSettings.getCaConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getCaConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case CBM:
                        return new LowerOrUpperBound(
                                value < metricSettings.getCbmConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getCbmConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case CBO:
                        return new LowerOrUpperBound(
                                value < metricSettings.getCboConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getCboConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case CC:
                        return new LowerOrUpperBound(
                                value < metricSettings.getCcConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getCcConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case CE:
                        return new LowerOrUpperBound(
                                value < metricSettings.getCeConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getCeConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case DAM:
                        return new LowerOrUpperBound(
                                value < metricSettings.getDamConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getDamConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case DIT:
                        return new LowerOrUpperBound(
                                value < metricSettings.getDitConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getDitConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case IC:
                        return new LowerOrUpperBound(
                                value < metricSettings.getIcConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getIcConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case LCOM:
                        return new LowerOrUpperBound(
                                value < metricSettings.getLcomConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getLcomConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case LCOM3:
                        return new LowerOrUpperBound(
                                value < metricSettings.getLcom3Config().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getLcom3Config().getMetricThreshold().getUpperBound(),
                                metric);
                    case LOC:
                        return new LowerOrUpperBound(
                                value < metricSettings.getLocConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getLocConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case MOA:
                        return new LowerOrUpperBound(
                                value < metricSettings.getMoaConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getMoaConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case MFA:
                        return new LowerOrUpperBound(
                                value < metricSettings.getMfaConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getMfaConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case NOC:
                        return new LowerOrUpperBound(
                                value < metricSettings.getNocConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getNocConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case NPM:
                        return new LowerOrUpperBound(
                                value < metricSettings.getNpmConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getNpmConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case RFC:
                        return new LowerOrUpperBound(
                                value < metricSettings.getRfcConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getRfcConfig().getMetricThreshold().getUpperBound(),
                                metric);
                    case WMC:
                        return new LowerOrUpperBound(
                                value < metricSettings.getWmcConfig().getMetricThreshold().getLowerBound(),
                                value > metricSettings.getWmcConfig().getMetricThreshold().getUpperBound(),
                                metric);
                }
            }


        throw new IllegalArgumentException("Illegal Metric given.");
    }


}
