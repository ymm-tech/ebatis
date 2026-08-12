# Repository Guidelines

## Project Structure & Modules

This is a Java 8 Maven multi-module Elasticsearch ORM. The root `pom.xml` coordinates:

- `ebatis-core/`: annotations, builders, mapper execution, and domain types.
- `ebatis-spring/`: Spring integration and mapper scanning.
- `ebatis-spring-boot/`: Boot auto-configuration and starter.
- `ebatis-web/`: web-layer integration.
- `ebatis-sample/`: runnable application and Elasticsearch-backed examples.

Use `*/src/main/java`, `*/src/main/resources`, and `*/src/test/java`; tests should mirror production packages. Keep framework behavior in `ebatis-core` unless it requires an integration module.

## Design Principles & Architecture

Ebatis presents annotated Mapper interfaces as the application-facing API, keeping Elasticsearch client calls and DSL construction out of business code. Runtime proxies turn annotations and parameter metadata into query/request factories, execute through routed cluster sessions, then extract typed responses. Preserve this pipeline when adding operations: **annotation → metadata → builder/request factory → executor → response extraction**. Extend existing factories, providers, routers, and interceptors instead of adding operation-specific shortcuts. Keep Spring, Boot, and web concerns outside `ebatis-core`, and maintain equivalent synchronous/asynchronous behavior where both forms exist.

## Build, Test, and Development Commands

Run from the repository root with Maven and JDK 8 or later:

```bash
mvn clean verify
mvn -pl ebatis-core test
mvn -pl ebatis-sample -am spring-boot:run
mvn -pl ebatis-sample -am test
```

`clean verify` builds all modules and runs tests. Use the core command for fast feedback. Sample tests read `ebatis-sample/src/main/resources/application.yaml`; start a compatible local Elasticsearch service first.

## Coding Style & Naming Conventions

Use four-space indentation, UTF-8, and Java 8-compatible APIs. Classes/interfaces use `PascalCase`, methods/fields `camelCase`, constants `UPPER_SNAKE_CASE`, and packages stay under `io.manbang.ebatis`. Use descriptive names such as `DefaultScoreSort` or `TermQueryBuilderFactory`.

Reuse domain abstractions rather than duplicating request construction. Lombok is available; follow adjacent usage. No formatter is configured; match local style. `qodana.yaml` configures JDK 8 static analysis.

## Testing Guidelines

Tests use JUnit 4 and names ending in `Test`. Add focused regression coverage for behavior changes. Keep builder/domain tests in `ebatis-core`; put live Elasticsearch scenarios in `ebatis-sample`. Never hard-code credentials or external endpoints.

## Commit & Pull Request Guidelines

Recent commits favor scoped Conventional Commit subjects, such as `feat(core): add PointInTime support` and `fix(ebatis-core): correct scroll keep-alive`. Use a short, imperative subject.

Pull requests should explain the change, affected modules, tests run, and linked issue. Include request/response examples or screenshots for public behavior or configuration changes. Exclude `target/` files and unrelated formatting.
