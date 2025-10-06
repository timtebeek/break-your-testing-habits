import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

# Spring MockMvc

Spring MockMvc has traditionally used Hamcrest matchers for assertions, which required mixing different assertion styles and was less discoverable.
Spring Framework 6.2 introduced `MockMvcTester`, providing full AssertJ integration for more consistent and expressive controller tests.

## Traditional Hamcrest matchers

MockMvc's traditional approach uses `MockMvcResultMatchers` with Hamcrest-style matchers, which feels disconnected from modern assertion libraries.

<Tabs>
<TabItem value="before" label="Before">

```java title="BundleControllerTest.java"
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BundleControllerTest {

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new BundleController()).build();

    @Test
    void getBundle() throws Exception {
        mockMvc.perform(get("/bundle"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.books[0].title").value("Effective Java"));
    }

    @Test
    void boom() throws Exception {
        mockMvc.perform(get("/boom"))
                .andExpect(status().isInternalServerError());
    }
}
```

:::warning

Traditional MockMvc assertions:
- Use Hamcrest-style matchers instead of AssertJ
- Require many static imports from `MockMvcResultMatchers`
- Mix assertion styles when combining with other AssertJ assertions
- Less discoverable API compared to fluent AssertJ chains
- Force checked exception handling with `throws Exception`

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BundleControllerTest.java"
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

class BundleControllerTest {

    private final MockMvcTester mockMvc = MockMvcTester.of(new BundleController());

    @Test
    void getBundle() {
        assertThat(mockMvc.get().uri("/bundle"))
                .hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .hasPathSatisfying("$.books[0].title",
                        title -> assertThat(title).isEqualTo("Effective Java"));
    }

    @Test
    void boom() {
        assertThat(mockMvc.get().uri("/boom"))
                .hasStatus5xxServerError();
    }
}
```

:::tip

Spring Framework 6.2+ provides `MockMvcTester` with full AssertJ integration:
- Consistent AssertJ fluent API across all assertions
- Single static import for `assertThat()`
- Better IDE autocomplete and discoverability
- No checked exceptions to handle
- Cleaner, more readable test code

:::

</TabItem>
</Tabs>

## Bridge approach

For incremental migration, you can use `MockMvcTester` while keeping existing `MockMvcRequestBuilders` imports.

<Tabs>
<TabItem value="before" label="Before">

```java title="BundleControllerTest.java"
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BundleControllerTest {

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new BundleController()).build();

    @Test
    void getBundle() throws Exception {
        mockMvc.perform(get("/bundle"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.books[0].title").value("Effective Java"));
    }

    @Test
    void boom() {
        assertThat(mockMvc.perform(get("/boom")))
                .hasStatus5xxServerError();
    }
}
```

:::info

Starting with traditional MockMvc and Hamcrest matchers.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BundleControllerTest.java"
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class BundleControllerTest {

    private final MockMvcTester mockMvc = MockMvcTester.of(new BundleController());

    @Test
    void getBundle() {
        assertThat(mockMvc.perform(get("/bundle")))
                .hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON)
                .bodyJson()
                .hasPathSatisfying("$.books[0].title",
                        title -> assertThat(title).isEqualTo("Effective Java"));
    }
}
```

:::tip

The bridge approach:
- Replaces `MockMvc` with `MockMvcTester`
- Keeps existing `MockMvcRequestBuilders.get()` calls
- Switches from `andExpect()` to AssertJ's `assertThat()`
- Allows gradual migration of request building code
- Removes the need for `throws Exception`

This is a good intermediate step when migrating large test suites, where you've already invested in custom request builders or utilities based on `MockMvcRequestBuilders`.

:::

</TabItem>
</Tabs>

## JSON path assertions

Testing JSON responses becomes more expressive with AssertJ integration.

<Tabs>
<TabItem value="before" label="Before">

```java title="BundleControllerTest.java"
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Test
void verifyJsonContent() throws Exception {
    mockMvc.perform(get("/bundle"))
            .andExpectAll(
                    jsonPath("$.books[0].title").value("Effective Java"),
                    jsonPath("$.books[0].author").value("Joshua Bloch"),
                    jsonPath("$.books[0].year").value(2001),
                    jsonPath("$.books").isArray(),
                    jsonPath("$.books.length()").value(3));
}
```

:::warning

Hamcrest-style JSON path assertions:
- Each path requires a separate `jsonPath()` call
- Mixing different matcher types (`value()`, `isArray()`)
- Less fluent and harder to chain
- Limited type safety

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BundleControllerTest.java"
import static org.assertj.core.api.Assertions.assertThat;

@Test
void verifyJsonContent() {
    assertThat(mockMvc.get().uri("/bundle"))
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.books[0].title", title -> assertThat(title).isEqualTo("Effective Java"))
            .hasPathSatisfying("$.books[0].author", author -> assertThat(author).isEqualTo("Joshua Bloch"))
            .hasPathSatisfying("$.books[0].year", year -> assertThat(year).isEqualTo(2001))
            .hasPathSatisfying("$.books", books -> assertThat(books).asList().hasSize(3));
}
```

:::tip

AssertJ's JSON path assertions:
- Use familiar AssertJ assertions within `hasPathSatisfying()`
- Chain multiple path assertions fluently
- Leverage full AssertJ API for each extracted value
- Better type handling with `asList()`, `asMap()`, etc.

:::

</TabItem>
</Tabs>

## Status code assertions

Status assertions are more semantic and expressive with `MockMvcTester`.

<Tabs>
<TabItem value="before" label="Before">

```java title="BundleControllerTest.java"
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test
void testStatusCodes() throws Exception {
    mockMvc.perform(get("/ok")).andExpect(status().isOk());
    mockMvc.perform(get("/created")).andExpect(status().isCreated());
    mockMvc.perform(get("/not-found")).andExpect(status().isNotFound());
    mockMvc.perform(get("/error")).andExpect(status().isInternalServerError());
}
```

:::info

Traditional status matchers work but require the `status()` wrapper.

:::

</TabItem>
<TabItem value="after" label="After">

```java title="BundleControllerTest.java"
import static org.assertj.core.api.Assertions.assertThat;

@Test
void testStatusCodes() {
    assertThat(mockMvc.get().uri("/ok")).hasStatusOk();
    assertThat(mockMvc.get().uri("/created")).hasStatus2xxSuccessful();
    assertThat(mockMvc.get().uri("/not-found")).hasStatus4xxClientError();
    assertThat(mockMvc.get().uri("/error")).hasStatus5xxServerError();
}
```

:::tip

AssertJ status assertions provide:
- Direct methods like `hasStatusOk()`, `hasStatus2xxSuccessful()`
- Better readability without the `status()` wrapper
- Consistent with other AssertJ assertions
- Support for status ranges (2xx, 4xx, 5xx)

:::

</TabItem>
</Tabs>
