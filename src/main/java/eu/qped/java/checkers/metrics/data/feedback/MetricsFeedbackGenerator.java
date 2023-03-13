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

import static eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;

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

    boolean lowerBound;
    boolean upperBound;
    static List<MetricsFeedback> feedbacks = new ArrayList<>();
    public MetricsFeedbackGenerator(){

    }
    
    
    
    private void setLowerAndUpperBound(boolean lowerBound, boolean upperBound, Metric metric){
        if(lowerBound && upperBound){ //TODO this should be tested in the frontend-layer
            throw new RuntimeException("the input of the lower threshold of the metric (" + metric.toString()
                    + ") is greater than the upper threshold");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

    }

   

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
    public List<MetricsFeedback> generateMetricsCheckerFeedbacks(List<ClassMetricsEntry> metricsMap, MetricSettings
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
                            isThresholdReached(metric, metricSettings, metricValue);
                            generateDefaultSuggestion(metric, customSuggestion, className, metricValue, "");
                            
                        } else {
                            metricValues = ((ClassMetricsMessageMulti) metricForClass).getMetricValues();
                            if(metricValues != null) {
                                for (Map.Entry<String, Integer> entry : metricValues.entrySet()) {
                                    isThresholdReached(metric, metricSettings, entry.getValue());
                                    if(isUpperBound() || isLowerBound()) {
                                        suggestionString = "For method " + MarkdownFormatterUtility.asCodeLine(entry.getKey()) + ":\n";
                                        metricValue = (double) entry.getValue();
                                        generateDefaultSuggestion(metric, customSuggestion, className, metricValue, suggestionString);
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
    public void generateDefaultSuggestion(MetricCheckerEntryHandler.Metric metric, MetricsFeedbackSuggestion customSuggestion, String className, double metricValue,String methodName) {
        if (customSuggestion == null) {
            if (isLowerBound()) {
                feedbacks.add(new MetricsFeedback(className, metric.getDescription(), metric, metricValue, methodName+generateMetricSpecificSuggestionLower(metric)));
            } else if (isUpperBound()) {
                feedbacks.add(new MetricsFeedback(className, metric.getDescription(), metric, metricValue, methodName+generateMetricSpecificSuggestionUpper(metric)));
            }
        } else {
            if (isLowerBound()) {
                feedbacks.add(new MetricsFeedback(className, metric.getDescription(), metric, metricValue, methodName+generateMetricSpecificSuggestionLower(metric)));
            } else if (isUpperBound()) {
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
    public void isThresholdReached(MetricCheckerEntryHandler.Metric metric, MetricSettings metricSettings, double value) {


        if (metric != null) {
            switch (metric) {
                case AMC:
                      setLowerAndUpperBound(
                            value < metricSettings.getAmcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getAmcConfig().getMetricThreshold().getUpperBound(), 
                            metric);
                case CAM:
                      setLowerAndUpperBound(
                            value < metricSettings.getCamConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCamConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CA:
                      setLowerAndUpperBound(
                            value < metricSettings.getCaConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCaConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CBM:
                      setLowerAndUpperBound(
                            value < metricSettings.getCbmConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCbmConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CBO:
                      setLowerAndUpperBound(
                            value < metricSettings.getCboConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCboConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CC:
                      setLowerAndUpperBound(
                            value < metricSettings.getCcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCcConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case CE:
                      setLowerAndUpperBound(
                            value < metricSettings.getCeConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getCeConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case DAM:
                      setLowerAndUpperBound(
                            value < metricSettings.getDamConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getDamConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case DIT:
                      setLowerAndUpperBound(
                            value < metricSettings.getDitConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getDitConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case IC:
                      setLowerAndUpperBound(
                            value < metricSettings.getIcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getIcConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case LCOM:
                      setLowerAndUpperBound(
                            value < metricSettings.getLcomConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getLcomConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case LCOM3:
                      setLowerAndUpperBound(
                            value < metricSettings.getLcom3Config().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getLcom3Config().getMetricThreshold().getUpperBound(),
                            metric);
                case LOC:
                      setLowerAndUpperBound(
                            value < metricSettings.getLocConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getLocConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case MOA:
                      setLowerAndUpperBound(
                            value < metricSettings.getMoaConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getMoaConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case MFA:
                      setLowerAndUpperBound(
                            value < metricSettings.getMfaConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getMfaConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case NOC:
                      setLowerAndUpperBound(
                            value < metricSettings.getNocConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getNocConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case NPM:
                      setLowerAndUpperBound(
                            value < metricSettings.getNpmConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getNpmConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case RFC:
                      setLowerAndUpperBound(
                            value < metricSettings.getRfcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getRfcConfig().getMetricThreshold().getUpperBound(),
                            metric);
                case WMC:
                      setLowerAndUpperBound(
                            value < metricSettings.getWmcConfig().getMetricThreshold().getLowerBound(),
                            value > metricSettings.getWmcConfig().getMetricThreshold().getUpperBound(),
                            metric);
            }
            
        }
        
        throw new IllegalArgumentException("Illegal Metric given.");
    }



}
