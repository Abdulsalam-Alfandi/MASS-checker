package eu.qped.java.checkers.metrics.settings;

import eu.qped.framework.qf.QfObjectBase;
import eu.qped.java.checkers.metrics.MetricsChecker;
import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler.Metric;
import eu.qped.java.checkers.metrics.data.feedback.MetricsFeedbackSuggestion;
import lombok.*;

import java.util.HashMap;

/**
 * Configuration for metric: Class modeling design settings for {@link MetricsChecker}.
 * The fields are modeling the threshold for the corresponding metric, custom suggestions and other settings for the checker.
 *
 * @author Jannik Seus
 */
@Getter
@Setter
@AllArgsConstructor
@Builder

public class MetricSettings extends QfObjectBase {

    @Getter(AccessLevel.NONE)
    private boolean includeCallsToJdk;

    @Getter(AccessLevel.NONE)
    private boolean includeOnlyPublicClasses;

    private HashMap<Metric, MetricsFeedbackSuggestion> customSuggestions;

    /**
     * Configuration for metric: Average method Complexity
     */
    private MetricConfig amcConfig;

    /**
     * Configuration for metric: Coupled classes: classes being used by this class (afferent)
     */
    private MetricConfig caConfig;


    /**
     * Configuration for metric: Cohesion Among methods of Class
     */
    private MetricConfig camConfig;

    /**
     * Configuration for metric: Coupling Between methods
     */
    private MetricConfig cbmConfig;

    /**
     * Configuration for metric: Coupling between object classes
     */
    private MetricConfig cboConfig;

    /**
     * Configuration for metric: Signatures of methods and values of McCabe Cyclomatic Complexity
     */
    private MetricConfig ccConfig;

    /**
     * Configuration for metric: Coupled classes: classes that use this class (efferent)
     */
    private MetricConfig ceConfig;

    /**
     * Configuration for metric: Data Access metric
     */
    private MetricConfig damConfig;

    /**
     * Configuration for metric: Depth of inheritance tree
     */
    private MetricConfig ditConfig;


    /**
     * Configuration for metric: Inheritance Coupling
     */
    private MetricConfig icConfig;

    /**
     * Configuration for metric: Lack of cohesion in methods
     */
    private MetricConfig lcomConfig;

    /**
     * Configuration for metric: Lack of cohesion in methods - Henderson-Sellers definition
     */
    private MetricConfig lcom3Config;

    /**
     * Configuration for metric: Line of codes per class (minimum and maximum thresholds)
     */
    private MetricConfig locConfig;

    /**
     * Configuration for metric: measure of Aggregation
     */
    private MetricConfig moaConfig;

    /**
     * Configuration for metric: measure of Functional Abstraction
     */
    private MetricConfig mfaConfig;

    /**
     * Configuration for metric: Number of children
     */
    private MetricConfig nocConfig;

    /**
     * Configuration for metric: Number of public methods
     */
    private MetricConfig npmConfig;


    /**
     * Configuration for metric: Response for a Class
     */
    private MetricConfig rfcConfig;

    /**
     * Configuration for metric: Weighted methods per class
     */
    private MetricConfig wmcConfig;

    public MetricSettings(){
    }


    public boolean areCallsToToJdkIncluded() {
        return includeCallsToJdk;
    }

    public void includeCallsToJdk(boolean includeCallsToJdk) {
        this.includeCallsToJdk = includeCallsToJdk;
    }

    public boolean areOnlyPublicClassesIncluded() {
        return includeOnlyPublicClasses;
    }

    public void includeOnlyPublicClasses(boolean includeNonPublicClasses) {
        this.includeOnlyPublicClasses = includeNonPublicClasses;
    }

    public MetricConfig getCamConfig() {
        return camConfig;
    }
}
