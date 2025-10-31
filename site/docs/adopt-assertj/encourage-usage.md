---
sidebar_position: 6
---
# Encourage usage

After you've migrated to AssertJ once, you'll want to ensure that all new tests also use AssertJ.
On top of that you'll want to nudge your team to use the most idiomatic AssertJ assertions available.
By automating enforcement you can educate your team and ensure consistency, without relying on code reviews.

Automated enforcement of these rules can be done with OpenRewrite and Error Prone Support, each with their own strengths.

## Error Prone Support

The Error Prone Support project contains static analysis rules for AssertJ, as well as various other libraries,
which can hook into Google's Error Prone to provide compile-time checks.
There's a little bit of [setup required to get it working](https://error-prone.picnic.tech/#-getting-started),
but once it's in place it will run as part of your build, with little impact on build time.

The benefit of compile time checks is that you get immediate feedback in your IDE, and it's near impossible to ignore.
A drawback is that Error Prone Support only converts statements one to one, and does not handle formatting.

## OpenRewrite

OpenRewrite includes Error Prone Support's AssertJ rules, but also has a more powerful refactoring engine.
This allows it to handle more complex refactorings, like introducing method chaining, or converting Hamcrest imports.
The installation instructions are the same as seen under [Migrate to AssertJ](./migrate-to-assertj).

A slight drawback of OpenRewrite is that it can take a little while to run, so it's slower to provide you with feedback.

## Integrate with GitHub Actions

You can integrate your recipe runs with GitHub Actions to automatically check pull requests for violations,
and even comment on pull requests with suggestions to fix the violations found.

[Langchain4j](https://docs.langchain4j.dev/) has a nice example of this setup in their repository for Maven; see these two workflow files.

1. The first one runs the OpenRewrite checks on pull requests.
https://github.com/langchain4j/langchain4j/blob/1.7.1/.github/workflows/receive-pr.yml#L1-L62
2. The second one comments on the pull request with the results.
https://github.com/langchain4j/langchain4j/blob/1.7.1/.github/workflows/comment-pr.yml#L1-L58

Collectively, these ensure that any pull request will use AssertJ to its fullest extent.

## Roll out at scale

If you want to roll this out at scale, consider using [Moderne](https://moderne.ai/),
which runs OpenRewrite recipes at scale across many repositories.
