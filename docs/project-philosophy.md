# Project Philosophy

This project is an educational Java security lab based on OWASP vulnerability categories.

The main idea is to help Java developers understand security issues from the inside: through code, HTTP requests, debugging, and mitigation examples.

## Principles

### 1. Code-first learning

Each example should start with real Java code.

The goal is not only to describe a vulnerability, but to show how it appears in backend code.

### 2. Vulnerable and fixed implementations

Each lesson should contain two versions:

- vulnerable implementation;
- fixed implementation.

This makes the difference visible and easy to understand.

### 3. Keep examples focused

Each example should focus on one security issue.

Unrelated technical details should be moved to common helpers.

For example, parameter parsing and JSON serialization should not distract from an access control example.

### 4. Developer-friendly explanations

The project is written for developers.

The explanation should answer:

- What is the vulnerability?
- Where is it in the code?
- How can it be exploited?
- Why does it happen?
- How can it be fixed?
- How can we detect it during code review?

### 5. No production use

The vulnerable examples are intentionally insecure.

They must never be copied into production systems.