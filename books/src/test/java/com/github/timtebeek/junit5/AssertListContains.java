package com.github.timtebeek.junit5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssertListContains {

    List<String> list = List.of("a", "b", "c");

    @Test
    void assertListContains() {
        assertTrue(list.contains("b"));
    }

    @Disabled
    @Test
    void assertListContainsFails() {
        assertTrue(list.contains("E"));
    }
}
