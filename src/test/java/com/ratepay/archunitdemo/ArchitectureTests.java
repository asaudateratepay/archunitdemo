package com.ratepay.archunitdemo;

import com.ratepay.archunitdemo.utils.Generated;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.ProxyRules.no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with;

@AnalyzeClasses(packages = "com.ratepay.archunitdemo", importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitectureTests {


    /**
     * Make sure that database access is only allowed through services, in order to enforce transactions control and
     * overall best practices
     */
    @ArchTest
    void controllers_cannot_access_classes_from_persistence(JavaClasses classes) {
        classes().that()
                .resideInAPackage("..web..")
                .should().onlyDependOnClassesThat()
                .resideOutsideOfPackage("..persistence..")
                .because("controllers must retrieve data from services")
                .check(classes);
    }

    /**
     * Make sure that all database-related stuff belong to packages called persistence , or any sub-packages
     *
     */
    @ArchTest
    void repositories_and_entities_must_be_defined_in_persistence_package(JavaClasses classes) {
        classes().that()
                .areAssignableTo(CrudRepository.class)
                .or()
                .areAnnotatedWith(Entity.class)
                .should()
                .resideInAPackage("..persistence..")
                .because("database-related classes belong to persistence packages")
                .check(classes);
    }


    /**
     * Make sure that toString methods are either annotated with lombok's @Generated annotation
     * or with our custom @Generated annotation (com.ratepay.archunitdemo.utils.Generated)
     *
     * This is useful because in those cases Jacoco doesn't scan the method for coverage,
     * therefore Sonar also does not complain about those.
     */
    @ArchTest
    void toString_needs_to_be_annotated_with_generated(JavaClasses classes) {
        methods().that()
                .haveName("toString")
                .and().arePublic()
                .and().haveRawReturnType(String.class)
                .and().haveRawParameterTypes(noArguments())
                .should()
                .beAnnotatedWith(Generated.class)
                .orShould()
                .beAnnotatedWith(lombok.Generated.class)
                .because("sonar will skip checking it for coverage")
                .check(classes);
    }

    @ArchTest
    final ArchRule no_access_to_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;


    @ArchTest
    final ArchRule no_use_of_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;


    @ArchTest
    final ArchRule classes_should_not_depend_on_upper_package = NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;


    @ArchTest
    final ArchRule no_inside_call_should_be_done_to_transactional_methods =
            no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(Transactional.class);


    private static DescribedPredicate<List<JavaClass>> noArguments() {
        return new DescribedPredicate<>("no arguments") {
            @Override
            public boolean test(List<JavaClass> javaClasses) {
                return javaClasses.isEmpty();
            }
        };
    }

}
