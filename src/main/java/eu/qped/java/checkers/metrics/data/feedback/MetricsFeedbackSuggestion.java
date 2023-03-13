package eu.qped.java.checkers.metrics.data.feedback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class MetricsFeedbackSuggestion {

    private String suggestionLowerBoundExceeded;
    private String suggestionUpperBoundExceeded;

    public MetricsFeedbackSuggestion(String suggestionLowerBoundExceeded, String suggestionUpperBoundExceeded) {
        this.suggestionLowerBoundExceeded = suggestionLowerBoundExceeded;
        this.suggestionUpperBoundExceeded = suggestionUpperBoundExceeded;
    }

    public String getSuggestionLowerBoundExceeded() {
        return suggestionLowerBoundExceeded;
    }

    public void setSuggestionLowerBoundExceeded(String suggestionLowerBoundExceeded) {
        this.suggestionLowerBoundExceeded = suggestionLowerBoundExceeded;
    }

    public String getSuggestionUpperBoundExceeded() {
        return suggestionUpperBoundExceeded;
    }

    public void setSuggestionUpperBoundExceeded(String suggestionUpperBoundExceeded) {
        this.suggestionUpperBoundExceeded = suggestionUpperBoundExceeded;
    }
}