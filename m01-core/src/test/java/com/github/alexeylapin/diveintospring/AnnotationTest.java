package com.github.alexeylapin.diveintospring;

import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class AnnotationTest {

    @Test
    void name() {
        MergedAnnotations mergedAnnotations = MergedAnnotations.search(MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
                .from(ProductA.class);

        boolean description = mergedAnnotations.isPresent("Description");
        System.out.println(description);

        boolean description2 = mergedAnnotations.isPresent(Description.class);
        System.out.println(description2);

        MergedAnnotation<Description> descriptionMergedAnnotation = mergedAnnotations.get(Description.class);
        Description synthesize = descriptionMergedAnnotation.synthesize();
        String valueValue = descriptionMergedAnnotation.getString("value");
        String detailsValue = descriptionMergedAnnotation.getString("details");
    }

    @Test
    void name2() {
        MergedAnnotations mergedAnnotations = MergedAnnotations.search(MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
                .from(ProductB.class);

        boolean description2 = mergedAnnotations.isPresent(Description.class);
        System.out.println(description2);

        MergedAnnotation<Description> descriptionMergedAnnotation = mergedAnnotations.get(Description.class);
        Description synthesize = descriptionMergedAnnotation.synthesize();
        String valueValue = descriptionMergedAnnotation.getString("value");
        System.out.println(valueValue);
        String detailsValue = descriptionMergedAnnotation.getString("details");
        System.out.println(detailsValue);
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Description {

        @AliasFor("details")
        String value() default "";

        @AliasFor("value")
        String details() default "";

    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Description(details = "Gift")
    @interface Gift {

        String name();

    }

    @Description(details = "Gift")
    public static class ProductA extends AbstractProduct {

    }

    @Gift(name = "Toy")
    public static class ProductB extends AbstractProduct {

    }

    public abstract static class AbstractProduct {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
