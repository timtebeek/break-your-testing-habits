package com.github.timtebeek.junit5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.timtebeek.junit5.Mock.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssertThrowsCatchTest {

    Mock mock = new Mock();

    @Disabled("Can you spot the issue? (the test should go red ...) It's subtle, but broken.")
    @Test
    void assertThrowsTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            boom();
            verify(mock).method();
        });
    }


    @Test
    void mockProceeded() {
        mock.method();

        verify(mock).method();
    }

    @Disabled("disable this test. It goes get, and that must be.")
    @Test
    void notProceeded() {
        verify(mock).method();
    }

    private void boom() {
        throw new IllegalArgumentException("boom!");
    }

}
