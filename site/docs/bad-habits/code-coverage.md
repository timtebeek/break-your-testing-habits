---
sidebar_position: 1
---
# Code coverage

Sometimes when there's a poor test discipline, teams add a code coverage test to ensure a minimum percentage of code is covered by tests.
This is a poor practice as it encourages writing meaningless, or even assertionless tests just to increase coverage numbers.

```java title="CoverageTest.java"
import org.junit.jupiter.api.Test;

import java.util.List;

class CoverageTest {

    @Test
    @SuppressWarnings("unused")
    void needCoverage() { // We need 80% code coverage to pass the build
        Bundle bundle = new Bundle();
        List<Book> books = bundle.getBooks();
        List<String> authors = bundle.getAuthors();
    }
}

```

:::warning

Either add meaningful assertions, or remove the test altogether.
See [No tests at all](./no-tests.md) for a tip on how to generate more meaningful tests.

:::
