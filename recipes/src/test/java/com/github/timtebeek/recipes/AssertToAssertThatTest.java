package com.github.timtebeek.recipes;

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class AssertToAssertThatTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new AssertToAssertThatRecipes());
    }

    @DocumentExample
    @Test
    void assertNull() {
        rewriteRun(
          java(
            """
              class Test {
                  void test(Object obj) {
                      assert obj == null;
                  }
              }
              """,
            """
              import org.assertj.core.api.Assertions;

              class Test {
                  void test(Object obj) {
                      Assertions.assertThat(obj).isNull();
                  }
              }
              """

          )
        );
    }

    @Test
    void assertNullWithMessage() {
        rewriteRun(
          java(
            """
              class Test {
                  void test(Object obj) {
                      assert obj == null : "Object should be null";
                  }
              }
              """,
            """
              import org.assertj.core.api.Assertions;

              class Test {
                  void test(Object obj) {
                      Assertions.assertThat(obj).as("Object should be null").isNull();
                  }
              }
              """
          )
        );
    }

    // TODO Additional tests for other assert statements
}
