# OWASP Java Code Review Lab

A practical OWASP-based Java security lab with vulnerable and fixed examples.

This project is designed for Java developers who want to understand application security from the inside:
through code, HTTP requests, tests, and secure fixes.

The lab follows the OWASP Top 10:2025 categories and focuses on practical secure code review scenarios in Java.

## Goals

* Learn OWASP Top 10 vulnerabilities from a Java developer's perspective
* Practice secure code review
* Compare vulnerable and fixed implementations
* Understand exploit scenarios and mitigations
* Prepare for AppSec interviews

## Tech Stack

* Java 21
* Jakarta Servlet API
* Gradle
* Tomcat
* JUnit 5

## Project Structure

* `src/main/java` — vulnerable and fixed Java examples
* `http` — ready-to-run HTTP requests
* `docs` — general secure code review notes
* `videos` — English video scripts and outlines

## Examples

| OWASP Category                                 | Status      | Example                                   |
|------------------------------------------------|-------------|-------------------------------------------|
| A01:2025 Broken Access Control                 | Completed   | IDOR with document access                 |
| A02:2025 Security Misconfiguration             | Completed   | Stack trace and internal details exposure |
| A03:2025 Software Supply Chain Failures        | In progress | Dependency with known CVE                 |
| A04:2025 Cryptographic Failures                | Planned     | Weak password hashing                     |
| A05:2025 Injection                             | Planned     | SQL Injection with JDBC                   |
| A06:2025 Insecure Design                       | Planned     | Missing idempotency in payment flow       |
| A07:2025 Authentication Failures               | Planned     | Weak remember-me token                    |
| A08:2025 Software or Data Integrity Failures   | Planned     | Unsigned config/plugin update             |
| A09:2025 Security Logging & Alerting Failures  | Planned     | Missing audit log                         |
| A10:2025 Mishandling of Exceptional Conditions | Planned     | Leaking stack traces and internal errors  |

## Completed Modules

### A01:2025 Broken Access Control

The first completed module demonstrates an IDOR vulnerability in a document access flow.

It shows how a user can access another user's document by changing the `documentId` request parameter, and how to fix the issue by enforcing ownership checks on the server side.

Covered topics:

* Insecure Direct Object Reference
* Server-side authorization
* Trusted authentication context
* Ownership checks
* 404 vs 403 behavior
* Negative authorization tests

## Disclaimer

This project is for educational purposes only.

The vulnerable examples are intentionally insecure and must not be used in production.

### A02:2025 Security Misconfiguration

The second completed module demonstrates a security misconfiguration caused by verbose error handling.

It shows how a vulnerable debug endpoint can expose internal implementation details when an error occurs, including exception types, exception messages, database URLs, internal table names, file system paths, feature flags, and stack traces.

The fixed version logs detailed errors on the server side and returns only a generic error message to the client.

Covered topics:

- Verbose error responses
- Stack trace exposure
- Internal configuration leakage
- Safe error handling
- Server-side logging
- Generic client-facing error messages
