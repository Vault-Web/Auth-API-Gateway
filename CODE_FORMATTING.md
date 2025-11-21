# Code Formatting Guide

## Overview

This project enforces consistent code formatting using **Spotless** with **Google Java Format**. All Java code must adhere to the Google Java Style Guide to maintain code quality and consistency across contributions.

## Automated Formatting Checks

### CI/CD Enforcement

Every pull request automatically runs a formatting check in the CI pipeline. **Pull requests will fail if code is not properly formatted.**

The CI workflow includes:
```bash
./mvnw spotless:check
```

This ensures that all merged code follows the established formatting standards.

## Local Development

### Check Formatting

To check if your code is properly formatted:

```bash
cd apigateway
./mvnw spotless:check
```

**On Windows:**
```powershell
cd apigateway
.\mvnw.cmd spotless:check
```

### Apply Formatting

To automatically format all Java files:

```bash
cd apigateway
./mvnw spotless:apply
```

**On Windows:**
```powershell
cd apigateway
.\mvnw.cmd spotless:apply
```

### Pre-commit Best Practice

**Always run `mvnw spotless:apply` before committing your changes** to avoid CI failures.

## What Gets Formatted

The Spotless configuration formats:

- ✅ All Java source files (`src/main/java/**/*.java`)
- ✅ All Java test files (`src/test/java/**/*.java`)

### Formatting Rules Applied

1. **Google Java Format** - Standard Google style formatting
2. **Import Organization** - Imports ordered as: `java`, `javax`, `org`, `com`, `vaultweb`
3. **Unused Import Removal** - Automatically removes unused imports
4. **Trailing Whitespace** - Removes trailing whitespace from lines
5. **End with Newline** - Ensures files end with a newline character

## References

- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Spotless Maven Plugin](https://github.com/diffplug/spotless/tree/main/plugin-maven)
- [Google Java Format](https://github.com/google/google-java-format)