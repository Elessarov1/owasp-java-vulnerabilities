# OWASP Java Code Review Lab

A practical OWASP-based Java security lab with vulnerable and fixed examples.

This project is designed for Java developers who want to understand application security from the inside:
through code, HTTP requests, static analysis findings, and secure fixes.

The lab follows the OWASP Top 10:2025 categories and focuses on practical secure code review scenarios in Java.

## Navigation

- [Goals](#goals)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Examples](#examples)
- [Completed Modules](#completed-modules)
    - [A01:2025 Broken Access Control](#a012025-broken-access-control)
    - [A02:2025 Security Misconfiguration](#a022025-security-misconfiguration)
- [Static Analysis with Semgrep](#static-analysis-with-semgrep)
    - [Local Semgrep Usage](#local-semgrep-usage)
    - [Reports](#reports)
    - [GitHub Code Scanning](#github-code-scanning)
- [Finding Triage](#finding-triage)
- [Suggested Learning Flow](#suggested-learning-flow)
- [Disclaimer](#disclaimer)

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

It shows how a user can access another user's document by changing the request parameter and how to fix the issue by enforcing ownership checks on the server side.

Covered topics:

* Insecure Direct Object Reference
* Server-side authorization
* Trusted authentication context
* Ownership checks
* 404 vs 403 behavior
* Negative authorization scenarios

### A02:2025 Security Misconfiguration

The second completed module demonstrates a security misconfiguration caused by verbose error handling.

It shows how a vulnerable debug endpoint can expose internal implementation details when an error occurs, including exception details, internal configuration values, and stack traces.

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

Custom rules are stored in:

```text
semgrep/rules
```

Current rule coverage:

| OWASP case | Purpose |
|------------|---------|
| A01:2025 Broken Access Control | Detect suspicious object lookup by id inside servlet code |
| A02:2025 Security Misconfiguration | Detect unsafe exception handling patterns |

The detailed finding messages are available directly in Semgrep output and GitHub Code Scanning reports.

### Local Semgrep Usage

Semgrep is executed through Docker, so a local Semgrep installation is not required.

Windows PowerShell:

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

Linux / WSL / macOS:

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

### Reports

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

### GitHub Code Scanning

This project uses GitHub Actions to run Semgrep and upload the SARIF report to GitHub Code Scanning.

Workflow file:

```text
.github/workflows/semgrep.yml
```

The workflow runs on:

* push to `main` or `master`
* pull requests
* manual `workflow_dispatch`

After the workflow runs, findings can be viewed in GitHub:

```text
Repository → Security → Code scanning
```

The pipeline does not fail on Semgrep findings because this repository intentionally contains vulnerable code for educational purposes.

## Finding Triage

SAST findings require review.

A finding can be classified as:

| Status | Meaning |
|--------|---------|
| True Positive | The finding points to real vulnerable code |
| False Positive | The rule matched code that is safe after review |
| Educational Expected Finding | The finding is intentionally kept to demonstrate the vulnerability |
| Accepted Risk | The issue is known and intentionally accepted for a documented reason |
| Needs Rule Improvement | The rule is too broad, too narrow, or produces too much noise |

For this project, Semgrep findings should be treated as secure code review signals.

Detailed explanations are intentionally kept in the source code comments, Semgrep rule messages, and generated reports instead of being duplicated in this README.

## Suggested Learning Flow

For each module:

1. Read the vulnerable implementation.
2. Run the HTTP requests from the `http` directory.
3. Observe the insecure behavior.
4. Run Semgrep locally.
5. Review Semgrep findings.
6. Compare vulnerable and fixed implementations.
7. Study the mitigation.
8. Check how the same finding appears in GitHub Code Scanning.

## Disclaimer

This project is for educational purposes only.

The vulnerable examples are intentionally insecure and must not be used in production.
