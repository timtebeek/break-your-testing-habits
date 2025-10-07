package com.github.timtebeek.junit5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.github.timtebeek.junit5.Mock.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssertThrowsCatchTest {

    Mock mock = new Mock();

    @Disabled("Can you spot the issue? It's subtle, but broken.")
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

    @Test
    void mockNotProceeded() {
        verify(mock).method();
    }

    private void boom() {
        throw new IllegalArgumentException("boom!");
    }

}
