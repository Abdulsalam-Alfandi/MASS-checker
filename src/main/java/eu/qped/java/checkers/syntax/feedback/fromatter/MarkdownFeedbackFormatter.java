package eu.qped.java.checkers.syntax.feedback.fromatter;

import eu.qped.framework.feedback.Feedback;
import eu.qped.framework.feedback.hint.Hint;
import eu.qped.framework.feedback.hint.HintType;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.code.CodeBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MarkdownFeedbackFormatter implements IFeedbackFormatter {


    @Override
    public List<Feedback> format(List<Feedback> feedbacks) {
        return feedbacks.stream().map(this::formatInMarkdown).collect(Collectors.toList());
    }

    private Feedback formatInMarkdown(Feedback feedback) {
        Feedback formatted = Feedback.builder().build();
        formatted.setTechnicalCause(feedback.getTechnicalCause());
        formatted.setReadableCause(feedback.getReadableCause());
        formatted.setType(feedback.getType());
        formatted.setErrorLocation(feedback.getErrorLocation());
        if (feedback.getHints() == null) feedback.setHints(Collections.emptyList());
        List<Hint> formattedHints = formatHints(feedback.getHints());
        formatted.setHints(formattedHints);
        formatted.setReference(feedback.getReference());
        return formatted;
    }

    private List<Hint> formatHints(List<Hint> hints) {
        List<Hint> formattedHints = new ArrayList<>();
        hints.forEach(
                h -> {
                    var tempHint = Hint.builder().build();
                    if (h.getType() == HintType.TEXT) {
                        tempHint.setContent(String.valueOf(new Text(h.getContent())));
                    } else if (h.getType() == HintType.CODE_EXAMPLE) {
                        tempHint.setContent(
                                String.valueOf(new CodeBlock(h.getContent(), "java"))
                        );
                    }
                    tempHint.setType(h.getType());
                    formattedHints.add(tempHint);
                }
        );
        return formattedHints;
    }
}
