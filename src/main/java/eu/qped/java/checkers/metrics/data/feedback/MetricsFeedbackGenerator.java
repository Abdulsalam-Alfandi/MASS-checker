package eu.qped.java.checkers.metrics.data.feedback;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsEntry;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessage;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageMulti;
import eu.qped.java.checkers.metrics.data.report.ClassMetricsMessageSingle;
import eu.qped.java.checkers.metrics.settings.MetricSettings;
import eu.qped.java.utils.markdown.MarkdownFormatterUtility;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents helper class to generate design feedback suggestions as strings.
 *
 * @author Jannik Seus
 */

@Getter
public class MetricsFeedbackGenerator {

    /**
     * @param lowerBound the lower threshold of the metric not to be exceeded
     * @param upperBound the upper threshold of the metric not to be exceeded
     */

  
    static List<MetricsFeedback> feedbacks = new ArrayList<>();
    
    
    
    
   

   

    /**
     * Generates a suggestion for the student depending on the exceeding of lower or upper bound
     * of already calculated value of the given metric.
     *
     * @param metric        the given metric
     * @param exceededLower true if lower bound was exceeded, false if upper bound was exceeded
     * @return a metric and boundary specific suggestion
     */
    public String generateMetricSpecificSuggestion(MetricCheckerEntryHandler.Metric metric, boolean exceededLower) {
        if (exceededLower) {
            return generateMetricSpecificSuggestionLower(metric);
        } else {
            return generateMetricSpecificSuggestionUpper(metric);
        }
    }



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
        MetricsFeedbackSuggestion customSuggestion;
        MetricCheckerEntryHandler.Metric metric;
        String suggestionString;
        double metricValue = 0;
        Map<String, Integer> metricValues;

        for (ClassMetricsEntry classMetricsEntry: metricsMap) {
            String className = classMetricsEntry.getClassName();
            List<ClassMetricsMessage> metricsForClass = classMetricsEntry.getMetricsForClass();

            if (metricsForClass != null) {
                for (ClassMetricsMessage metricForClass: metricsForClass) {
                    metric = metricForClass.getMetric();
                    customSuggestion = metricSettings.getCustomSuggestions().get(metric);

                        if (metricForClass instanceof ClassMetricsMessageSingle) {
                            metricValue = ((ClassMetricsMessageSingle) metricForClass).getMetricValue();
                            LowerAndUpperBound lowerAndUpperBound = isThresholdReached(metric, metricSettings, metricValue);
                            generateDefaultSuggestion(metric, customSuggestion, className, metricValue, "", lowerAndUpperBound);
                            
                        } else {
                            metricValues = ((ClassMetricsMessageMulti) metricForClass).getMetricValues();
                            if(metricValues != null) {
                                for (Map.Entry<String, Integer> entry : metricValues.entrySet()) {
                                    LowerAndUpperBound lowerAndUpperBound = isThresholdReached(metric, metricSettings, entry.getValue());
                                    if(lowerAndUpperBound.isUpperBound() || lowerAndUpperBound.isLowerBound()) {
                                        suggestionString = "For method " + MarkdownFormatterUtility.asCodeLine(entry.getKey()) + ":\n";
                                        metricValue = (double) entry.getValue();
                                        generateDefaultSuggestion(metric, customSuggestion, className, metricValue,
                                                suggestionString, lowerAndUpperBound);
                                    }
                                }
                            }
                        }


                   
                }
            }
        }
       
        return feedbacks;
    }


    /**
     * Generates a suggestion for the student depending on the exceeding of am already calculated value of a metric.
     *
     * @param metric     the given metric
     * @return a nicely formatted suggestion as String.
     */
    public static void generateDefaultSuggestion(MetricCheckerEntryHandler.Metric metric, MetricsFeedbackSuggestion customSuggestion,
                                          String className, double metricValue,String methodName, LowerAndUpperBound lowerAndUpperBound) {
        if (customSuggestion == null) {
            if (lowerAndUpperBound.isLowerBound()) {
                feedbacks.add(new MetricsFeedback(className, metric.getDescription(), metric, metricValue, methodName+generateMetricSpecificSuggestionLower(metric)));
            } else if (lowerAndUpperBound.isUpperBound()) {
                feedbacks.add(new MetricsFeedback(className, metric.getDescription(), metric, metricValue, methodName+generateMetricSpecificSuggestionUpper(metric)));
            }
        } else {
            if (lowerAndUpperBound.isLowerBound()) {
                feedbacks.add(new MetricsFeedback(className, metric.getDescription(), metric, metricValue, methodName+generateMetricSpecificSuggestionLower(metric)));
            } else if (lowerAndUpperBound.isUpperBound()) {
                feedbacks.add(new MetricsFeedback(className, metric.getDescription(), metric, metricValue, methodName+generateMetricSpecificSuggestionUpper(metric)));
            }

        }
    }


    /**
     * Generates a suggestion for the student depending on the exceeding of the upper bound
     * of already calculated value of the given metric.
     *
     * @param metric the given metric
     * @return a metric and lower bound specific suggestion
     */
    private static String generateMetricSpecificSuggestionUpper(MetricCheckerEntryHandler.Metric metric) {

        if (metric != null) {
            switch (metric) {
                case AMC:
                    return DefaultMetricSuggestion.AMC.getUpperBoundReachedSuggestion();
                case CA:
                    return DefaultMetricSuggestion.CA.getUpperBoundReachedSuggestion();
                case CAM:
                    return DefaultMetricSuggestion.CAM.getUpperBoundReachedSuggestion();
                case CBM:
                    return DefaultMetricSuggestion.CBM.getUpperBoundReachedSuggestion();
                case CBO:
                    return DefaultMetricSuggestion.CBO.getUpperBoundReachedSuggestion();
                case CC:
                    return DefaultMetricSuggestion.CC.getUpperBoundReachedSuggestion();
                case CE:
                    return DefaultMetricSuggestion.CE.getUpperBoundReachedSuggestion();
                case DAM:
                    return DefaultMetricSuggestion.DAM.getUpperBoundReachedSuggestion();
                case DIT:
                    return DefaultMetricSuggestion.DIT.getUpperBoundReachedSuggestion();
                case IC:
                    return DefaultMetricSuggestion.IC.getUpperBoundReachedSuggestion();
                case LCOM:
                    return DefaultMetricSuggestion.LCOM.getUpperBoundReachedSuggestion();
                case LCOM3:
                    return DefaultMetricSuggestion.LCOM3.getUpperBoundReachedSuggestion();
                case LOC:
                    return DefaultMetricSuggestion.LOC.getUpperBoundReachedSuggestion();
                case MFA:
                    return DefaultMetricSuggestion.MFA.getUpperBoundReachedSuggestion();
                case MOA:
                    return DefaultMetricSuggestion.MOA.getUpperBoundReachedSuggestion();
                case NOC:
                    return DefaultMetricSuggestion.NOC.getUpperBoundReachedSuggestion();
                case NPM:
                    return DefaultMetricSuggestion.NPM.getUpperBoundReachedSuggestion();
                case RFC:
                    return DefaultMetricSuggestion.RFC.getUpperBoundReachedSuggestion();
                case WMC:
                    return DefaultMetricSuggestion.WMC.getUpperBoundReachedSuggestion();
            }
        }
        throw new IllegalArgumentException("Invalid metric given.");
    }

    /**
     * Generates a suggestion for the student depending on the exceeding of the lower bound
     * of already calculated value of the given metric.
     *
     * @param metric the given metric
     * @return a metric and lower bound specific suggestion
     */
    private static String generateMetricSpecificSuggestionLower(MetricCheckerEntryHandler.Metric metric) {
        if (metric != null) {
            switch (metric) {
                case AMC:
                    return DefaultMetricSuggestion.AMC.getLowerBoundReachedSuggestion();
                case CA:
                    return DefaultMetricSuggestion.CA.getLowerBoundReachedSuggestion();
                case CAM:
                    return DefaultMetricSuggestion.CAM.getLowerBoundReachedSuggestion();
                case CBM:
                    return DefaultMetricSuggestion.CBM.getLowerBoundReachedSuggestion();
                case CBO:
                    return DefaultMetricSuggestion.CBO.getLowerBoundReachedSuggestion();
                case CC:
                    return DefaultMetricSuggestion.CC.getLowerBoundReachedSuggestion();
                case CE:
                    return DefaultMetricSuggestion.CE.getLowerBoundReachedSuggestion();
                case DAM:
                    return DefaultMetricSuggestion.DAM.getLowerBoundReachedSuggestion();
                case DIT:
                    return DefaultMetricSuggestion.DIT.getLowerBoundReachedSuggestion();
                case IC:
                    return DefaultMetricSuggestion.IC.getLowerBoundReachedSuggestion();
                case LCOM:
                    return DefaultMetricSuggestion.LCOM.getLowerBoundReachedSuggestion();
                case LCOM3:
                    return DefaultMetricSuggestion.LCOM3.getLowerBoundReachedSuggestion();
                case LOC:
                    return DefaultMetricSuggestion.LOC.getLowerBoundReachedSuggestion();
                case MFA:
                    return DefaultMetricSuggestion.MFA.getLowerBoundReachedSuggestion();
                case MOA:
                    return DefaultMetricSuggestion.MOA.getLowerBoundReachedSuggestion();
                case NOC:
                    return DefaultMetricSuggestion.NOC.getLowerBoundReachedSuggestion();
                case NPM:
                    return DefaultMetricSuggestion.NPM.getLowerBoundReachedSuggestion();
                case RFC:
                    return DefaultMetricSuggestion.RFC.getLowerBoundReachedSuggestion();
                case WMC:
                    return DefaultMetricSuggestion.WMC.getLowerBoundReachedSuggestion();
            }
        }
        throw new IllegalArgumentException("Invalid metric given.");
    }

    

    /**
     * Checks whether the lower or upper threshold of a given metricThreshold was exceeded.
     *
     * @param metric         the given metric
     * @param metricSettings the settings for design guidelines
     * @param value          the given metricThreshold's value
     * @return whether the minimum (lower=true) or maximum (lower=false) threshold was exceeded.
     */
    public static LowerAndUpperBound isThresholdReached(MetricCheckerEntryHandler.Metric metric, MetricSettings metricSettings, double value) {


        if (metric != null) {
            switch (metric) {
                case AMC:
                      return new LowerAndUpperBound(
                            value < metricSettings.getAmcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getAmcConfig().getMetricThreshold().getUpperBound(), 
                            metric);
                case CAM:
                      return new LowerAndUpperBound(
                            value < metricSettings.getCamConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCamConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CA:
                      return new LowerAndUpperBound(
                            value < metricSettings.getCaConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCaConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CBM:
                      return new LowerAndUpperBound(
                            value < metricSettings.getCbmConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCbmConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CBO:
                      return new LowerAndUpperBound(
                            value < metricSettings.getCboConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCboConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CC:
                      return new LowerAndUpperBound(
                            value < metricSettings.getCcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCcConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CE:
                      return new LowerAndUpperBound(
                            value < metricSettings.getCeConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCeConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case DAM:
                      return new LowerAndUpperBound(
                            value < metricSettings.getDamConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getDamConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case DIT:
                      return new LowerAndUpperBound(
                            value < metricSettings.getDitConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getDitConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case IC:
                      return new LowerAndUpperBound(
                            value < metricSettings.getIcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getIcConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case LCOM:
                      return new LowerAndUpperBound(
                            value < metricSettings.getLcomConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getLcomConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case LCOM3:
                      return new LowerAndUpperBound(
                            value < metricSettings.getLcom3Config().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getLcom3Config().getMetricThreshold().getUpperBound(),
                            metric);
                case LOC:
                      return new LowerAndUpperBound(
                            value < metricSettings.getLocConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getLocConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case MOA:
                      return new LowerAndUpperBound(
                            value < metricSettings.getMoaConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getMoaConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case MFA:
                      return new LowerAndUpperBound(
                            value < metricSettings.getMfaConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getMfaConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case NOC:
                      return new LowerAndUpperBound(
                            value < metricSettings.getNocConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getNocConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case NPM:
                      return new LowerAndUpperBound(
                            value < metricSettings.getNpmConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getNpmConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case RFC:
                      return new LowerAndUpperBound(
                            value < metricSettings.getRfcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getRfcConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case WMC:
                      return new LowerAndUpperBound(
                            value < metricSettings.getWmcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getWmcConfig().getMetricThreshold().getUpperBound(),
                            metric);
            }
            
        }
        
        throw new IllegalArgumentException("Illegal Metric given.");
    }



}
