package eu.qped.java.checkers.metrics.data.feedback;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler;

public class LowerAndUpperBound {

    boolean lowerBound;
    boolean upperBound;

    LowerAndUpperBound(boolean lowerBound, boolean upperBound, MetricCheckerEntryHandler.Metric metric){
        if(lowerBound && upperBound){ //TODO this should be tested in the frontend-layer
            throw new RuntimeException("the input of the lower threshold of the metric (" + metric.toString()
                    + ") is greater than the upper threshold");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

    }

    public boolean isLowerBound() {
        return lowerBound;
    }

    public boolean isUpperBound() {
        return upperBound;
    }
}
