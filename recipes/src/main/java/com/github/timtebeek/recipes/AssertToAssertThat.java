package com.github.timtebeek.recipes;

import com.google.errorprone.refaster.ImportPolicy;
import com.google.errorprone.refaster.annotation.AfterTemplate;
import com.google.errorprone.refaster.annotation.BeforeTemplate;
import com.google.errorprone.refaster.annotation.UseImportPolicy;
import org.openrewrite.java.template.RecipeDescriptor;

import static org.assertj.core.api.Assertions.assertThat;

@RecipeDescriptor(
        name = "Assert to AssertJ",
        description = "Convert `assert` statements to AssertJ assertions."
)
public class AssertToAssertThat {

    @RecipeDescriptor(
            name = "Convert `assert obj == null` to AssertJ",
            description = "Convert `assert obj == null` statements to `assertThat(obj).isNull()` from AssertJ."
    )
    static class AssertThatIsNull {
        @BeforeTemplate
        void before(Object actual) {
            assert actual == null;
        }

        @AfterTemplate
        @UseImportPolicy(ImportPolicy.STATIC_IMPORT_ALWAYS)
        void after(Object actual) {
            assertThat(actual).isNull();
        }
    }

    // TODO Add recipe descriptor
    static class AssertThatIsNullWithMessage {
        @BeforeTemplate
        void before(Object actual, String message) {
            assert actual == null : message;
        }

        @AfterTemplate
            // TODO Add static import policy
        void after(Object actual, String message) {
            assertThat(actual).as(message).isNull();
        }
    }

    static class AssertThatIsNotNull {
        // TODO Add annotation
        void before(Object actual) {
            assert actual != null;
        }

        // TODO Add `after` method
    }

    static class AssertThatIsNotNullWithMessage {
        // TODO Add `before` and `after` methods
    }

    static class AssertThatIsEqualTo {
        // TODO Add `before` and `after` methods
    }

    static class AssertThatIsEqualToWithMessage {
        // TODO Add `before` and `after` methods
    }

    static class AssertThatIsNotEqualTo {
        // TODO Add `before` and `after` methods
    }

    static class AssertThatIsNotEqualToWithMessage {
        // TODO Add `before` and `after` methods
    }

    static class AssertThatIsSameAs {
        // TODO Add `before` and `after` methods
    }

    static class AssertThatIsSameAsWithMessage {
        // TODO Add `before` and `after` methods
    }

    static class AssertThatIsNotSameAs {
        // TODO Add `before` and `after` methods
    }

    static class AssertThatIsNotSameAsWithMessage {
        // TODO Add `before` and `after` methods
    }

}
