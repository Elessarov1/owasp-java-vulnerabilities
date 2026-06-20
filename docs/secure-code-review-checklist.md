# Java Secure Code Review Checklist

This checklist helps review Java web application code from an application security perspective.

## Authentication

Check:

- How is the current user identified?
- Is the authentication mechanism trusted?
- Is the application trusting user-controlled headers, parameters, or body fields?
- Are tokens, sessions, or credentials properly validated?
- Are expired or invalid credentials rejected?

Suspicious patterns:

```java
request.getParameter("userId")
request.getHeader("X-User-Id")
```

These values are user-controlled unless they are set by a trusted internal component.

## Authorization

Check:

- Does the endpoint access sensitive objects?
- Does the request contain object identifiers such as `documentId`, `orderId`, `invoiceId`, `paymentId`, or `tenantId`?
- Is access checked against the current user?
- Is tenant or organization scope enforced?
- Are negative authorization tests present?

Suspicious pattern:

```java
repository.findById(id)
```

Safer patterns:

```java
repository.findByIdAndOwnerId(id, currentUser.id())
repository.findByIdAndTenantId(id, currentUser.tenantId())
```

## Input Validation

Check:

- Are request parameters validated?
- Are numeric values parsed safely?
- Are enum values restricted to known values?
- Are file names, paths, URLs, and hostnames validated?
- Are allowlists used where possible?

## Injection

Check:

- Is SQL built using string concatenation?
- Are user-controlled values passed to commands?
- Are user inputs used inside templates, expressions, or queries?
- Are prepared statements used for SQL?

Suspicious pattern:

```java
"SELECT * FROM users WHERE name = '" + username + "'"
```

Safer pattern:

```java
PreparedStatement statement = connection.prepareStatement(
    "SELECT * FROM users WHERE name = ?"
);
```

## SSRF

Check:

- Does the backend make HTTP requests to user-controlled URLs?
- Are private IP ranges blocked?
- Are redirects restricted?
- Is DNS rebinding considered?
- Is there an allowlist of trusted hosts?

Suspicious pattern:

```java
new URL(request.getParameter("url")).openConnection()
```

## Cryptography

Check:

- Are passwords hashed using a password hashing algorithm?
- Are weak hashes such as MD5, SHA-1, or raw SHA-256 avoided for password storage?
- Are secrets hardcoded?
- Is sensitive data encrypted when required?
- Are random values generated using secure randomness?

Suspicious pattern for password storage:

```java
MessageDigest.getInstance("SHA-256")
```

Safer password storage options:

```text
BCrypt
Argon2
PBKDF2
```

## Logging and Monitoring

Check:

- Are security-sensitive actions logged?
- Are authentication and authorization failures logged?
- Are logs structured enough for investigation?
- Are secrets excluded from logs?
- Is there a correlation id or request id?

Security-sensitive events:

- login failure;
- access denied;
- password change;
- payment status change;
- permission change;
- tenant configuration change.

## Error Handling

Check:

- Are stack traces hidden from users?
- Are internal details not exposed in error messages?
- Are errors logged server-side?
- Are security failures handled consistently?

## General Questions

For every endpoint, ask:

1. Who can call this endpoint?
2. What object does this endpoint access?
3. Where does the current user come from?
4. Is the object access scoped to the current user or tenant?
5. What happens if another user changes the object id?
6. Is there a negative test for this case?