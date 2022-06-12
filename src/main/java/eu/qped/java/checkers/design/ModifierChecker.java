package eu.qped.java.checkers.design;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import eu.qped.java.checkers.design.feedback.DesignFeedback;
import eu.qped.java.checkers.design.feedback.DesignFeedbackGenerator;
import eu.qped.java.checkers.design.infos.ExpectedElement;

import java.util.*;

/**
 * Modifier Checker for fields and methods, checking for access and non access modifiers
 * @param <T> FieldDeclaration or MethodDeclaration from JavaParser
 */
class ModifierChecker<T extends Node> {

    private final String CHECKER_TYPE;

    public ModifierChecker(String CHECKER_TYPE) {
        this.CHECKER_TYPE = CHECKER_TYPE;
    }

    /**
     * Go through each specified element, split all keywords into lists and check if:
     * - enough elements exist in the code
     * - find all exact matches
     * - find mistakes in the remaining ones
     * The splitting is done by extracting the relevant piece from the string, shortening the string by the amount removed
     * This is done for all keywords, such that the string is empty by the end
     * The order to split the string is important, as they depend on the previous operation.
     * @param expectedElements expected modifiers in a node
     */
    public List<DesignFeedback> checkModifiers(ClassOrInterfaceDeclaration classDecl, List<ExpectedElement> expectedElements) {
        if(expectedElements.isEmpty()) {
            return new ArrayList<>();
        }
        List<DesignFeedback> modifierFeedback = new ArrayList<>();
        String className = classDecl.getNameAsString();
        List<NodeWithModifiers<T>> presentElements = getAllFieldsOrMethods(classDecl);

        DesignFeedback sizeFb = checkIfLessThanExpectedPresent(className, presentElements, expectedElements);
        if(sizeFb != null) {
            modifierFeedback.add(sizeFb);
        }
        removeExactMatches(presentElements, expectedElements);
        modifierFeedback.addAll(findViolation(className, presentElements, expectedElements));
        return modifierFeedback;
    }

    /**
     * checks if an element with modifiers matches an exact combination of access and non access modifier
     * removes the element if it has an exact match
     * @param presentElements all elements that are present in the current class
     * @param expectedElements all expected elements in the form of ExpectedElementInfo
     */
    private void removeExactMatches(List<NodeWithModifiers<T>> presentElements, List<ExpectedElement> expectedElements) {
        if(expectedElements.isEmpty()) {
            return;
        }

        Iterator<NodeWithModifiers<T>> elemIterator = presentElements.iterator();
        while(elemIterator.hasNext()) {
            NodeWithModifiers<T> presentElement = elemIterator.next();

            Iterator<ExpectedElement> expectedElemIterator = expectedElements.iterator();
            while(expectedElemIterator.hasNext()) {
                ExpectedElement expectedElement = expectedElemIterator.next();
                List<Boolean> matchingResult = getMatchingResult(presentElement, expectedElement);
                if(!matchingResult.contains(false)) {
                    elemIterator.remove();
                    expectedElemIterator.remove();
                    break;
                }
            }
        }
    }

    /**
     * Finds the design violations in the given presentElements and adds them to the design feedback
     * Feedback for:
     * - Name Mismatch
     * - Type Mismatch
     * - Modifier Mismatch
     * - Element missing entirely
     * @param presentElements elements present in the class
     * @param expectedElements expected elements from class info
     */
    private List<DesignFeedback> findViolation(String className, List<NodeWithModifiers<T>> presentElements, List<ExpectedElement> expectedElements) {
        if(expectedElements.isEmpty()) {
            return new ArrayList<>();
        }

        List<DesignFeedback> collectedFeedback = new ArrayList<>();

        Iterator<NodeWithModifiers<T>> presentElemIterator = presentElements.iterator();
        Iterator<ExpectedElement> expectedElemIterator = expectedElements.iterator();

        while(presentElemIterator.hasNext()) {
            if(!expectedElemIterator.hasNext()) {
                return collectedFeedback;
            }

            NodeWithModifiers<T> presentElement = presentElemIterator.next();
            ExpectedElement expectedElement = expectedElemIterator.next();

            List<Boolean> matchingResult = getMatchingResult(presentElement, expectedElement);
            String violationFound = DesignFeedbackGenerator.VIOLATION_CHECKS.get(matchingResult);

            String elementName = getVariableName(presentElement);
            if(CHECKER_TYPE.equals(CheckerUtils.METHOD_CHECKER)) {
                if(!elementName.contains("()")) {
                    elementName += "()";
                }
                if(violationFound.equals(DesignFeedbackGenerator.MISSING_FIELDS)) {
                    violationFound = DesignFeedbackGenerator.MISSING_METHODS;
                }
            }
            DesignFeedback fb = DesignFeedbackGenerator.generateFeedback(className, elementName, violationFound);
            collectedFeedback.add(fb);
        }
        return collectedFeedback;
    }

    /**
     * Checks if more or equal elements are there compared to the expected amount
     * @param expectedElements expected keywords, gives the size of the expected elements
     */
    private DesignFeedback checkIfLessThanExpectedPresent(String className, List<NodeWithModifiers<T>> presentElements,
                                                          List<ExpectedElement> expectedElements) {

        if(expectedElements.size() > presentElements.size()) {
            String violation;
            if(CHECKER_TYPE.equals(CheckerUtils.FIELD_CHECKER)) {
                violation = DesignFeedbackGenerator.MISSING_FIELDS;
            } else {
                violation = DesignFeedbackGenerator.MISSING_METHODS;
            }
            return DesignFeedbackGenerator.generateFeedback(className, "", violation);
        }
        return null;
    }

    /**
     * Get all names of the element. Fields can usually have more than one variale in a declaration
     * but as we unrolled them previously, every field can only have one variable.
     * @param element the element to get the name from
     * @return name of the element
     */
    private String getVariableName(NodeWithModifiers<T> element) {
        String elementName;

        if(CHECKER_TYPE.equals(CheckerUtils.FIELD_CHECKER)) {
            elementName = ((FieldDeclaration) element).getVariable(0).getNameAsString();
        } else {
            elementName = ((MethodDeclaration) element).getNameAsString();
        }

        return elementName;
    }

    /**
     * Check the type of the element.
     * @param elem the element to check
     * @param expectedType expected field / return type
     * @return true if exact match
     */
    private boolean isElementTypeMatch(NodeWithModifiers<T> elem, String expectedType) {
        if(expectedType.isBlank()) {
            return false;
        }

        if(expectedType.equals(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }

        String presentType;
        if(CHECKER_TYPE.equals(CheckerUtils.FIELD_CHECKER)) {
            FieldDeclaration fieldElement = (FieldDeclaration) elem;
            presentType = fieldElement.getElementType().asString();
        } else {
            MethodDeclaration methodElement = (MethodDeclaration) elem;
            presentType = methodElement.getType().asString();
        }
        return presentType.equalsIgnoreCase(expectedType);
    }

    /**
     * True if either the expectedElementName is * (optional) or if the expected and actual names match up
     * @param elem element to check
     * @param expectedElementName expected element name, can either be * or the name
     * @return true if exact match
     */
    private boolean isElementNameMatch(NodeWithModifiers<T> elem, String expectedElementName) {
        if(expectedElementName.equals(CheckerUtils.OPTIONAL_KEYWORD)) {
            return true;
        }
        String elementName = getVariableName(elem);
        return expectedElementName.equals(elementName);
    }


    /**
     * Gets all fields or methods, specified by CHECKER_TYPE
     * @return a list of all needed elements
     */
    private List<NodeWithModifiers<T>> getAllFieldsOrMethods(ClassOrInterfaceDeclaration classDecl) {
        List<NodeWithModifiers<T>> elementsWithModifiers = new ArrayList<>();

        if(CHECKER_TYPE.equals(CheckerUtils.FIELD_CHECKER)) {
            List<FieldDeclaration> foundFields = classDecl.findAll(FieldDeclaration.class);
            unrollVariableDeclarations(foundFields);
            foundFields.forEach(fd -> elementsWithModifiers.add((NodeWithModifiers<T>) fd));
        } else {
            classDecl.findAll(MethodDeclaration.class).forEach(md -> elementsWithModifiers.add((NodeWithModifiers<T>) md));
        }

        return elementsWithModifiers;
    }

    /**
     * Fields can possess multiple variable declarations in one statement. The checker does not recognize these
     * as separate fields, such that we need to unroll them into separate fields first before we can check them
     * for correctness.
     * @param elements fields to unroll
     */
    private void unrollVariableDeclarations(List<FieldDeclaration> elements) {
        final int MAX_ALLOWED_VARIABLES = 1;
        ListIterator<FieldDeclaration> elemIterator = elements.listIterator();

        while(elemIterator.hasNext()) {
            FieldDeclaration fd = elemIterator.next();
            if(fd.getVariables().size() > MAX_ALLOWED_VARIABLES) {
                elemIterator.remove();
                for (VariableDeclarator variable: fd.getVariables()) {
                    FieldDeclaration field = new FieldDeclaration(fd.getModifiers(), variable);
                    elemIterator.add(field);
                }
            }
        }
    }

    /**
     * Check for matches for each individual part of the element
     * @param presentElement present element to check keywords for
     * @param expectedElement expected element to check keywords against
     * @return list of all matches of the format: (access, non access, type, name)
     */
    private List<Boolean> getMatchingResult(NodeWithModifiers<T> presentElement, ExpectedElement expectedElement) {
        List<Boolean> matching = new ArrayList<>();
        boolean accessMatch = CheckerUtils.isAccessMatch(presentElement.getAccessSpecifier().asString(), expectedElement.getAccessModifier());
        boolean nonAccessMatch = CheckerUtils.isNonAccessMatch(presentElement.getModifiers(), expectedElement.getNonAccessModifiers());
        boolean typeMatch = isElementTypeMatch(presentElement, expectedElement.getType());
        boolean nameMatch = isElementNameMatch(presentElement, expectedElement.getName());
        matching.add(accessMatch);
        matching.add(nonAccessMatch);
        matching.add(typeMatch);
        matching.add(nameMatch);
        return matching;
    }
}
