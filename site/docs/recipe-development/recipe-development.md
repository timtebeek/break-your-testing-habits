# Recipe development

- Explain how migration is implemented with mix of Error Prone Support Refaster and OpenRewrite recipes
- Exercise for new refaster rule converting `assert object != null, "message"` to `assertThat(object).as("message").isNotNull()`
- Exercise for new refaster rule converting `assertTrue(str != null && str.equals("Foo"))` to `assertThat(obj).isNotNull().isEqualTo("Foo")`

## TODO Link to ./recipes
