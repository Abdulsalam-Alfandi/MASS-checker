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
        formatted.setRelatedLocation(feedback.getRelatedLocation());
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
                    if (h.getType() == HintType.TEXT) {
                        formattedHints.add(Hint.builder()
                                .content(String.valueOf(new Text(h.getContent())))
                                .type(HintType.TEXT)
                                .build());
                    } else if (h.getType() == HintType.CODE_EXAMPLE) {
                        formattedHints.add(Hint.builder()
                                .content(String.valueOf(new CodeBlock(h.getContent(), "java")))
                                .type(HintType.CODE_EXAMPLE)
                                .build());
                    }
                }
        );
        return formattedHints;
    }
}
