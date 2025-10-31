---
sidebar_position: 2
---
# Plain `assert`

The built-in `assert` keyword in Java is a very basic mechanism for checking assumptions in code.
It only supports boolean expressions and an optional message.
It is not as expressive or flexible as modern testing frameworks like JUnit or AssertJ.

```java title="PlainAssert.java"
public class PlainAssert {
    public static void main(String[] args) {
        // Only supports booleans; supports a message
        assert args != null : "args should not be null";
        assert args.length > 0 : "args should not be empty";

        // Should not throw ArrayIndexOutOfBoundsException
        System.out.println("args[0] = " + args[0]);
    }
}
```

The `assert` keyword can be enabled or disabled at runtime using the `-ea` (enable assertions) or `-da` (disable assertions) flags.

:::danger

By default, assertions are disabled, which can lead to tests that appear to pass but do not actually perform any checks.

```bash
# Volkswagen mode by default, or when -da is used
java books/src/test/java/com/github/timtebeek/plain/PlainAssert.java
java -da books/src/test/java/com/github/timtebeek/plain/PlainAssert.java
```

:::

:::info

Opt in to execute assertions with `-ea`. Note how we go from `ArrayIndexOutOfBoundsException` to `AssertionError`.
```bash
# Execute assertions
java -ea books/src/test/java/com/github/timtebeek/plain/PlainAssert.java
```

:::
