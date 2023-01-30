package eu.qped.java.checkers.metrics.data.feedback;

import eu.qped.java.checkers.metrics.ckjm.MetricCheckerEntryHandler;

/**
 * Enumeration that contains suggestions for every metric when the lower or upper boundary was exceeded.
 *
 * @author Jannik Seus
 */
public enum DefaultMetricSuggestion { //TODO will be replaced by field 'default' in settings json file
    AMC("The Average Method Complexity's value is too low: \n Increase your average method size, e.g. by joining multiple methods with mostly the same functionalities from over-engineering.",
            "The Average Method Complexity's value is too high: \n"+"Decrease your average method size, e.g. by delegating functionalities to other newly created methods."),
    CA("The Afferent Coupling's value is too low: \n This class is used by too few other classes. Is this class even necessary? Can you implement this class's functionalities into already existing classes?",
            "The Afferent Coupling's value is too high: \n"+"This class is used by too many other classes. Can you outsource some functionalities into already existing or new classes?"),
    CAM("The Cohesion Among Methods Of Class's value is too low: \n"+"This class and their methods are or are close to being un-cohesive. Assimilate methods in your class in order to increase readability and decrease complexity at once.",
            "The Cohesion Among Methods Of Class's value is too high: \n"+"This class and their methods are too cohesive. Your implemented methods are too close to being the same methods."),
    CBM("The Coupling Between Method's value is too low: \n"+"The methods in this class are not or are hardly coupled, which means they have (close to) no interdependencies. Is this reasonable for your class?",
            "The Coupling Between Method's value is too high: \n"+"The methods in this class are coupled to high, which means too many interdependencies, coordination and information flow between them. Try to minimize these dependencies between your methods."),
    CBO("The Coupling Between Object Classes's value is too low: \n"+"The sum of all class couplings in this class is (close to) zero, which means they have (close to) no interdependencies to other classes. Is this reasonable for your class? Also, refer to afferent/efferent coupling metric.",
            "The Coupling Between Object Classes's value is too high: \n"+"The sum of all class couplings in this class is too high, which means too many interdependencies, coordination and information flow between them. Try to minimize these dependencies from this class to other classes. Also, refer to afferent/efferent coupling metric."),
    CC("The McCabe's Cyclomatic Complexity's value is too low: \n"+"This method in the given class has very few different paths to take. It would be allowed to increase its complexity.",
            "The McCabe's Cyclomatic Complexity's value is too high: \n"+"This method in the given class is too complex, too many paths are taken (ite-statements). Try to decrease the complexity by delegating functionalities into other methods or classes."),
    CE("The Efferent Coupling's value is too low: \n"+"This class is using too few other classes. Can some functionalities be implemented into other classes and be used?",
            "The Efferent Coupling's value is too high: \n"+"This class is using too many other classes. Can some functionalities be joined by other classes or even be implemented in this specific class?"),
    DAM("The Data Access Metric's value is too low: \n"+"This class contains very few private (protected) attributes compared to to the total number of attributes. Try to encapsulate your class (make fields private, only access them by methods contained in this specific class if possible).",
            "The Data Access Metric's value is too high: \n"+"This class contains many private (protected) attributes compared to to the total number of attributes. Encapsulation is important, but sometimes over-engineering. Is this reasonable?"),
    DIT("The Depth Of Inheritance Tree's value is too low: \n"+"This class has very few superclasses or only one superclass (Object.java). Is inheritance a valid option? ",
            "The Depth Of Inheritance Tree's value is too high: \n"+"This class has many superclasses. Is this much inheritance possible over-engineering? Do certain subclasses have too similar or too few functionalities?"),
    IC("The Inheritance Coupling's value is too low: \n"+"This class is coupled to few or no parent classes. Overriding parent methods could be a suitable option here.",
            "The Inheritance Coupling's value is too high: \n"+"This class is coupled to many parent classes. Overriding parent methods makes sense, but is not always necessary."),
    LCOM("The Lack Of Cohesion In Methods's value is too low: \n"+"The modularisation of this class is too low. Too many methods operate on different attributes.",
            "The Lack Of Cohesion In Methods's value is too high: \n"+"The modularisation of this class is quite high. You could think about the necessity if your class is very small."),
    LCOM3("The Henderson-Sellers' Lack Of Cohesion In Methods's value is too low: \n"+"The modularisation of this class is too low. Too many methods operate on different attributes.",
            "The Henderson-Sellers' Lack Of Cohesion In Methods's value is too high: \n"+"The modularisation of this class is quite high. You could think about the necessity if your class is very small."),
    LOC("The Lines Of Code's value is too low: \n"+"This class contains very few lines of code. Is it even necessary to put these functionalities into a separate class?",
            "The Lines Of Code's value is too high: \n"+"This class contains too many lines of code, it could be considered as a \"God Class\". Try to keep only the main functionality in this class, others should be implemented into other (new) classes."),
    MFA("The Measure Of Functional Abstraction's value is too low: \n"+"The functional abstraction of this class ist quite low. If possible, try to let his class inherit some methods.",
            "The Measure Of Functional Abstraction's value is too high: \n"+"The functional abstraction of this class is very high. Consider refactoring this class into an abstract class if this is not yet the case."),
    MOA("The Measure Of Aggregation's value is too low: \n"+"This class contains too few class fields. In order to increase class aggregation, also increase the number of fields or merge this class into another.",
            "The Measure Of Aggregation's value is too high: \n"+"This class contains too many class fields. Try to inline fields or extract functionalities into other classes."),
    NOC("The Number Of Children's value is too low: \n"+"This class has very few or no immediate descendants. Would extending this class be reasonable?",
            "The Number Of Children's value is too high: \n"+"This class has too much immediate descendants. Consider using multiple inheritance, i.e. creating subclasses of a subclass."),
    NPM("The Number Of Public Methods For A Class's value is too low: \n"+"This class uses few or no public methods. Is this intended?",
            "The Number Of Public Methods For A Class's value is too high: \n"+"This class uses mostly public methods. Try to decrease their visibility to force the information hiding principle."),
    RFC("The Response For A Class's value is too low: \n"+"This class has too few or zero (in-)directly executable methods. Is this class even necessary then?",
            "The Response For A Class's value is too high: \n"+"This class is able to (in-)directly execute too many methods. This is a typical smell for a god class. Does your class have one main functionality? Can some functionalities be extracted into other existing or new classes?"),
    WMC("The Weighted Methods Per Class's value is too low: \n"+"This class contains too few or zero methods. Is this class even necessary then?",
            "The Weighted Methods Per Class's value is too high: \n"+"This class contains too many methods. This is a typical smell for a god class. Does your class have one main functionality? Can some functionalities be extracted into other existing or new classes?");

    private final String lowerBoundReachedSuggestion;
    private final String upperBoundReachedSuggestion;


    DefaultMetricSuggestion(String lowerBoundReachedSuggestion, String upperBoundReachedSuggestion) {
        this.lowerBoundReachedSuggestion = lowerBoundReachedSuggestion;
        this.upperBoundReachedSuggestion = upperBoundReachedSuggestion;
    }

    public String getLowerBoundReachedSuggestion() {
        return lowerBoundReachedSuggestion;
    }

    public String getUpperBoundReachedSuggestion() {
        return upperBoundReachedSuggestion;
    }
}