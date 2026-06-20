# OWASP Java Code Review Lab

A practical OWASP-based Java security lab with vulnerable and fixed examples.

This project is designed for Java developers who want to understand application security from the inside:
through code, HTTP requests, tests, and secure fixes.

## Goals

- Learn OWASP Top 10 vulnerabilities from a Java developer's perspective
- Practice secure code review
- Compare vulnerable and fixed implementations
- Understand exploit scenarios and mitigations
- Prepare for AppSec interviews

## Tech Stack

- Java 21
- Jakarta Servlet API
- Gradle
- Tomcat
- JUnit 5

## Project Structure

- `src/main/java` — vulnerable and fixed Java examples
- `http` — ready-to-run HTTP requests
- `docs` — general secure code review notes
- `videos` — English video scripts and outlines

## Examples

| OWASP Category | Status | Example |
|---|---:|---|
| A01 Broken Access Control | In progress | IDOR with document access |
| A02 Cryptographic Failures | Planned | Weak password hashing |
| A03 Injection | Planned | SQL Injection with JDBC |
| A04 Insecure Design | Planned | Missing idempotency in payment flow |
| A05 Security Misconfiguration | Planned | Debug endpoint and permissive CORS |
| A06 Vulnerable Components | Planned | Dependency with known CVE |
| A07 Identification and Authentication Failures | Planned | Weak remember-me token |
| A08 Software and Data Integrity Failures | Planned | Unsigned config/plugin update |
| A09 Security Logging and Monitoring Failures | Planned | Missing audit log |
| A10 Server-Side Request Forgery | Planned | URL fetcher SSRF |

## Disclaimer

This project is for educational purposes only.

The vulnerable examples are intentionally insecure and must not be used in production.