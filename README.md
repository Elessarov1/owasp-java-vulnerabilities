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
  - [A03:2025 Software Supply Chain Failures](#a032025-software-supply-chain-failures)
  - [A04:2025 Cryptographic Failures](#a042025-cryptographic-failures)
  - [A05:2025 Injection](#a052025-injection)
  - [A06:2025 Insecure Design](#a062025-insecure-design)
  - [A07:2025 Authentication Failures](#a072025-authentication-failures)
- [Static Analysis with Semgrep](#static-analysis-with-semgrep)
  - [Local Semgrep Usage](#local-semgrep-usage)
  - [Reports](#reports)
  - [GitHub Code Scanning](#github-code-scanning)
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
* GitHub Code Scanning3

## Project Structure

* `src/main/java` — vulnerable and fixed Java examples
* `http` — ready-to-run HTTP requests
* `docs` — general secure code review notes
* `semgrep/rules` — custom Semgrep SAST rules
* `.github/workflows` — GitHub Actions workflows
* `semgrep.bat` — Windows helper script for local Semgrep scans
* `Makefile` — Linux / WSL / macOS helper commands

## Examples

| OWASP Category                                 | Status    | Example                                   | Demo page                                                |
|------------------------------------------------|-----------|-------------------------------------------|----------------------------------------------------------|
| A01:2025 Broken Access Control                 | Completed | IDOR with document access                 | http://localhost:8080/owasp-java-vulnerabilities/a01/    |
| A02:2025 Security Misconfiguration             | Completed | Stack trace and internal details exposure | http://localhost:8080/owasp-java-vulnerabilities/a02/    |
| A03:2025 Software Supply Chain Failures        | Completed | Unsafe template interpolation             | http://localhost:8080/owasp-java-vulnerabilities/a03/    |
| A04:2025 Cryptographic Failures                | Completed | Weak password hashing                     | http://localhost:8080/owasp-java-vulnerabilities/a04/    |
| A05:2025 Injection                             | Completed | SQL Injection with JDBC                   | http://localhost:8080/owasp-java-vulnerabilities/a05/    |
| A06:2025 Insecure Design                       | Completed | Missing order workflow validation         | http://localhost:8080/owasp-java-vulnerabilities/a06/    |
| A07:2025 Authentication Failures               | Completed | Missing brute-force protection            | http://localhost:8080/owasp-java-vulnerabilities/a07/    |
| A08:2025 Software or Data Integrity Failures   | Planned   | Unsigned config/plugin update             | —                                                        |
| A09:2025 Security Logging & Alerting Failures  | Planned   | Missing audit log                         | —                                                        |
| A10:2025 Mishandling of Exceptional Conditions | Planned   | Leaking stack traces and internal errors  | —                                                        |

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

### A03:2025 Software Supply Chain Failures

The third completed module demonstrates a software supply chain failure caused by an outdated dependency combined with unsafe API usage.

It shows how user-controlled input can be evaluated as a template expression when passed to a powerful interpolation API.

The fixed version avoids evaluating arbitrary user-controlled templates. It uses predefined server-side templates and substitutes only known placeholder values.

Covered topics:

* Vulnerable third-party dependencies
* Unsafe template interpolation
* User-controlled input flowing into dangerous APIs
* SCA and dependency review
* Dependabot alerts and dependency updates
* Secure use of predefined templates

### A04:2025 Cryptographic Failures

The fourth completed module demonstrates a cryptographic failure caused by weak password hashing.

It shows why fast unsalted hashes such as SHA-256 are not suitable for password storage and how to fix the issue by using BCrypt with a cost parameter and a random salt.

The fixed version stores a BCrypt password hash. The hash string includes the algorithm marker, cost parameter, salt, and hash value.

Covered topics:

* Weak password hashing
* Fast hashes vs password hashing algorithms
* Salt and work factor
* BCrypt password storage
* Password hash verification
* Negative SAST data-flow signal: password parameter flowing into SHA-256 hashing

### A05:2025 Injection

The fifth completed module demonstrates SQL injection in a JDBC user search.

The vulnerable version formats a user-controlled username directly into a SQL query and executes it
through `Statement`. An attacker can alter the `WHERE` clause and retrieve records outside the
intended search.

The fixed version keeps the SQL structure constant and binds the username through a
`PreparedStatement` parameter.

Covered topics:

* SQL injection through string-formatted queries
* JDBC `Statement` and `PreparedStatement`
* Parameter binding
* Query structure vs query data
* Exploit verification with HTTP requests
* SAST detection of formatted SQL passed to JDBC

### A06:2025 Insecure Design

The sixth completed module demonstrates an insecure order workflow with no model of valid state
transitions.

The vulnerable version accepts any syntactically valid order status selected by the client and
writes it directly. This allows impossible business transitions such as changing a newly created
order directly from `CREATED` to `REFUNDED`.

The fixed version uses an explicit server-side state machine. It checks the current order status,
allows only defined transitions, and rejects invalid workflow changes without modifying persistent
state.

Covered topics:

* Business logic and workflow vulnerabilities
* Syntactic validation vs business rule validation
* Server-side state machines
* Explicit allowed state transitions
* HTTP `409 Conflict` for invalid workflow changes
* Atomic check-and-update operations
* SAST review signals and the limits of automated design analysis

### A07:2025 Authentication Failures

The seventh completed module demonstrates an authentication endpoint that does not restrict
repeated password attempts.

The vulnerable version verifies every password candidate without tracking previous failures.
An automated attacker can continue a password spraying sequence until a valid password is found.

The fixed version applies temporary account-based and client-based throttling before password
verification. After three failed attempts, further requests receive `429 Too Many Requests` with
a `Retry-After` header. Both versions use the same BCrypt credential verification so the example
stays focused on authentication attempt limiting rather than password storage.

Covered topics:

* Brute-force and password spraying attacks
* Account-based and client-based attempt limiting
* Temporary cooldowns and account lockout tradeoffs
* Generic authentication error messages
* Username enumeration through response behavior and timing
* HTTP `429 Too Many Requests` and `Retry-After`
* SAST review signals for missing authentication throttling

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
| A03:2025 Software Supply Chain Failures | Detect HTTP input flowing into unsafe template interpolation |
| A04:2025 Cryptographic Failures | Detect password parameters flowing into SHA-256 based password hashing |
| A05:2025 Injection | Detect dynamically formatted SQL executed through JDBC Statement |
| A06:2025 Insecure Design | Flag state updates without explicit workflow transition checks for review |
| A07:2025 Authentication Failures | Flag credential verification without login attempt limiting |

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

### Dependency Analysis

Dependency vulnerabilities are handled separately from custom Semgrep code rules.

This project uses:

* GitHub Dependabot alerts for vulnerable dependencies;
* Dependabot version updates for dependency upgrade pull requests;
* Dependency Review Action for pull request dependency checks.

Semgrep is used for code-level review signals, while Dependabot/SCA is used for dependency-level vulnerability detection.

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
