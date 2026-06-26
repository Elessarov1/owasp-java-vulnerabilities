# OWASP Java Code Review Lab

A practical OWASP-based Java security lab with vulnerable and fixed examples.

This project is designed for Java developers who want to understand application security from the inside:
through code, HTTP requests, tests, static analysis findings, and secure fixes.

The lab follows the OWASP Top 10:2025 categories and focuses on practical secure code review scenarios in Java.

## Goals

* Learn OWASP Top 10 vulnerabilities from a Java developer's perspective
* Practice secure code review
* Compare vulnerable and fixed implementations
* Understand exploit scenarios and mitigations
* Learn how SAST findings are produced and triaged
* Prepare for AppSec interviews

## Tech Stack

* Java 21
* Jakarta Servlet API
* Gradle
* Tomcat
* JUnit 5
* Semgrep
* GitHub Actions
* GitHub Code Scanning

## Project Structure

* `src/main/java` — vulnerable and fixed Java examples
* `http` — ready-to-run HTTP requests
* `docs` — general secure code review notes
* `videos` — English video scripts and outlines
* `semgrep/rules` — custom Semgrep SAST rules
* `.github/workflows` — GitHub Actions workflows
* `semgrep.bat` — Windows helper script for local Semgrep scans
* `Makefile` — Linux / WSL / macOS helper commands

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

### A02:2025 Security Misconfiguration

The second completed module demonstrates a security misconfiguration caused by verbose error handling.

It shows how a vulnerable debug endpoint can expose internal implementation details when an error occurs, including exception types, exception messages, database URLs, internal table names, file system paths, feature flags, and stack traces.

The fixed version logs detailed errors on the server side and returns only a generic error message to the client.

Covered topics:

* Verbose error responses
* Stack trace exposure
* Internal configuration leakage
* Safe error handling
* Server-side logging
* Generic client-facing error messages

## Static Analysis with Semgrep

This project includes custom Semgrep rules for educational SAST demonstrations.

The goal is to show how insecure coding patterns can be detected before running the application.

The repository intentionally contains vulnerable examples, so Semgrep findings are expected.

### Current Custom Rules

| OWASP case | Rule | What it detects |
|------------|------|-----------------|
| A01:2025 Broken Access Control | `owasp-java-vulnerabilities.a01.object-lookup-by-request-controlled-id` | Object lookup by id inside a servlet. This requires review for ownership, tenant, or permission checks. |
| A02:2025 Security Misconfiguration | `owasp-java-vulnerabilities.a02.exception-details-used` | Direct usage of exception details such as `getMessage()`, `getClass().getName()`, or `getCause()` inside catch blocks. |
| A02:2025 Security Misconfiguration | `owasp-java-vulnerabilities.a02.print-stack-trace` | Usage of `printStackTrace(...)` in application code. |

### Why Findings Are Expected

This is an educational repository.

Vulnerable code is intentionally stored in the project to demonstrate:

* how the vulnerability works;
* how it can be exploited;
* how to fix it;
* how a SAST tool can detect suspicious code patterns;
* how findings should be reviewed and triaged.

A Semgrep finding does not always mean that the code is exploitable.

In this project, findings should be treated as secure code review signals.

## Local Semgrep Usage

Semgrep is executed through Docker, so a local Semgrep installation is not required.

### Windows PowerShell

Run scan:

```powershell
.\semgrep.bat scan
```

Validate rules:

```powershell
.\semgrep.bat validate
```

Generate JSON report:

```powershell
.\semgrep.bat json
```

Generate SARIF report:

```powershell
.\semgrep.bat sarif
```

Run strict mode:

```powershell
.\semgrep.bat strict
```

Strict mode fails when findings are detected.

For this educational project, strict mode should not be used as the default scan mode because vulnerable examples are intentionally present.

### Linux / WSL / macOS

Run scan:

```bash
make semgrep
```

Validate rules:

```bash
make semgrep-validate
```

Generate JSON report:

```bash
make semgrep-json
```

Generate SARIF report:

```bash
make semgrep-sarif
```

Run strict mode:

```bash
make semgrep-strict
```

### Report Files

Local reports are generated in:

```text
build/semgrep/
```

Expected files:

```text
build/semgrep/semgrep.json
build/semgrep/semgrep.sarif
```

These files are local build artifacts and should not be committed.

## Expected Semgrep Findings

At the current stage, Semgrep should detect findings in vulnerable examples.

### A01:2025 Broken Access Control

Expected finding:

```text
owasp-java-vulnerabilities.a01.object-lookup-by-request-controlled-id
```

Expected vulnerable location:

```text
VulnerableDocumentServlet
```

Reason:

```text
The servlet performs object lookup by id. If the id comes from the HTTP request,
the code must enforce ownership, tenant, or permission checks before returning the object.
```

In the vulnerable implementation, the document is loaded only by id and returned to the client without checking that the document belongs to the current user.

### A02:2025 Security Misconfiguration

Expected findings:

```text
owasp-java-vulnerabilities.a02.exception-details-used
owasp-java-vulnerabilities.a02.print-stack-trace
```

Expected vulnerable location:

```text
VulnerableDebugErrorServlet
```

Reason:

```text
The vulnerable implementation uses exception class name, exception message,
and stack trace data to build an HTTP error response.
```

This can expose internal implementation details to a client.

## Finding Triage

SAST findings require review.

A finding can be classified as:

| Status | Meaning |
|--------|---------|
| True Positive | The finding points to real vulnerable code. |
| False Positive | The rule matched code that is safe after review. |
| Educational Expected Finding | The finding is intentionally kept to demonstrate the vulnerability. |
| Accepted Risk | The issue is known and intentionally accepted for a documented reason. |
| Needs Rule Improvement | The rule is too broad, too narrow, or produces too much noise. |

### A01 Triage Notes

The A01 Semgrep rule is a review rule.

It detects object lookup by id inside servlet code.

This pattern is not always vulnerable by itself.

The finding becomes security-relevant when:

* the id is controlled by the client;
* the object is loaded only by id;
* the object is returned to the client;
* there is no ownership, tenant, or permission check.

Safe code should enforce checks such as:

```java
document.ownerId() == currentUser.id()
```

or use a repository/service method that includes authorization context:

```java
findDocumentByIdAndOwnerId(documentId, currentUser.id())
```

### A02 Triage Notes

The A02 Semgrep rules detect unsafe exception handling patterns.

Examples:

```java
exception.getMessage()
exception.getClass().getName()
exception.getCause()
exception.printStackTrace(...)
```

These calls are not always vulnerabilities by themselves.

They become security-relevant when exception details are returned to a client, exposed through a debug endpoint, or included in a public API error response.

In this project, the A02 findings are true positives because exception details are included in the HTTP response body.

## GitHub Actions and Code Scanning

This project uses GitHub Actions to run Semgrep and upload the SARIF report to GitHub Code Scanning.

Workflow file:

```text
.github/workflows/semgrep.yml
```

The workflow runs on:

* push to `main` or `master`
* pull requests
* manual `workflow_dispatch`

The pipeline generates a SARIF report and uploads it to GitHub Code Scanning.

The pipeline does not fail on Semgrep findings because this repository intentionally contains vulnerable code for educational purposes.

After the workflow runs, findings can be viewed in GitHub:

```text
Repository → Security → Code scanning
```

Findings can be reviewed and dismissed in GitHub Code Scanning as:

* false positive;
* won't fix;
* used in tests;
* expected educational finding;
* issue to fix.

## Suggested Learning Flow

For each module:

1. Read the vulnerable implementation.
2. Run the HTTP requests from the `http` directory.
3. Observe the insecure behavior.
4. Run tests.
5. Run Semgrep locally.
6. Review Semgrep findings.
7. Compare vulnerable and fixed implementations.
8. Study the mitigation.
9. Check how the same finding appears in GitHub Code Scanning.

## Disclaimer

This project is for educational purposes only.

The vulnerable examples are intentionally insecure and must not be used in production.