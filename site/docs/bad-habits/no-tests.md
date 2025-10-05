# No tests

From time to time you'll encounter a codebase with no tests at all.
If you've inherited such a codebase, it's worth adding at least a few tests to cover the most critical functionality.
Even if these tests are not perfect, they at least provide some safety net against regressions.

Also, pour some salt into the coffee of whoever was responsible.

```java title="Missing.java"
class Missing {

    // TODO Add meaningful tests

}
```

:::tip

Tools like [Diffblue Cover](https://www.diffblue.com/opensource) can help you to automatically generate unit tests for Java codebases with no tests.
Coding assistants like [Claude Code](https://claude.com/solutions/coding) and [GitHub Copilot](https://github.com/copilot) are also useful to help you write tests faster.

:::
