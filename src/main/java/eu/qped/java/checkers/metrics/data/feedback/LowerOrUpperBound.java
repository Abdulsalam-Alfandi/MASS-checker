package eu.qped.java.checkers.metrics.data.feedback;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public class LowerOrUpperBound {
    /**
     * @param lowerBound the lower threshold of the metric not to be exceeded
     * @param upperBound the upper threshold of the metric not to be exceeded
     */

    boolean lowerBound;
    boolean upperBound;
    boolean valid;

    public LowerOrUpperBound(boolean lowerBound, boolean upperBound, MetricCheckerEntryHandler.Metric metric)  {
        if(lowerBound && upperBound){ //TODO this should be tested in the frontend-layer
            throw new RuntimeException("the input of the lower threshold of the metric (" + metric.toString()
                    + ") is greater than the upper threshold");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

    }

    /**
     * Generates a suggestion for the student depending on the exceeding of am already calculated value of a metric.
     *
     * @param metric     the given metric
     * @return a nicely formatted suggestion as String.
     */
    public String generateDefaultSuggestion(MetricCheckerEntryHandler.Metric metric, MetricsFeedbackSuggestion customSuggestion) {

        if(customSuggestion == null) {
            if (lowerBound) {
                return generateMetricSpecificSuggestionLower(metric);
            }
            else if(upperBound){
                return generateMetricSpecificSuggestionUpper(metric) ;
            } else {
                return "";
            }

        } else {
            if (lowerBound) {
                return generateMetricSpecificSuggestionLower(metric)+ customSuggestion.getSuggestionLowerBoundExceeded();
            }
            else if(upperBound){
                return generateMetricSpecificSuggestionUpper(metric) + customSuggestion.getSuggestionUpperBoundExceeded();
            } else {
                return "";
            }

        }

    }
    /**
     * Generates a suggestion for the student depending on the exceeding of lower or upper bound
     * of already calculated value of the given metric.
     *
     * @param metric        the given metric
     * @param exceededLower true if lower bound was exceeded, false if upper bound was exceeded
     * @return a metric and boundary specific suggestion
     */
    public static String generateMetricSpecificSuggestion(MetricCheckerEntryHandler.Metric metric, boolean exceededLower) {
        if (exceededLower) {
            return generateMetricSpecificSuggestionLower(metric);
        } else {
            return generateMetricSpecificSuggestionUpper(metric);
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





}
