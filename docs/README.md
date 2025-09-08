# wAether Documentation Index

This directory contains comprehensive documentation for the wAether Android Wear OS application.

## Documentation Structure

### Root Level
- [`README_ENV.md`](../README_ENV.md) - Environment variables configuration guide
- [`.env.template`](../.env.template) - Environment variables template file

### Deployment Documentation (`docs/deployment/`)
- [`deployment-guide.md`](deployment/deployment-guide.md) - Complete deployment procedures
- [`api-integration.md`](deployment/api-integration.md) - External API setup and configuration

### Production Documentation (`docs/production/`)
- [`operations-guide.md`](production/operations-guide.md) - Production operations and monitoring
- [`troubleshooting.md`](production/troubleshooting.md) - Issue resolution procedures
- [`firebase-security-rules.md`](production/firebase-security-rules.md) - Firebase security configuration

### Operations Data (`docs/ops/`)
- [`environment-variables.csv`](ops/environment-variables.csv) - Environment variables reference export

## Quick Start

1. **Environment Setup**: Start with [`README_ENV.md`](../README_ENV.md)
2. **Deployment**: Follow [`deployment-guide.md`](deployment/deployment-guide.md)
3. **Production Operations**: Reference [`operations-guide.md`](production/operations-guide.md)
4. **Troubleshooting**: Use [`troubleshooting.md`](production/troubleshooting.md) for issue resolution

## Documentation Standards

All documentation follows the project's standards defined in [`copilot-instructions.md`](../copilot-instructions.md):

- **Change Log Format**: ISO-8601 timestamps with UTC and local time
- **Environment Variables**: Documented at repo root with versioned exports in `docs/ops/`
- **Security**: No secrets or sensitive data in documentation
- **Consistency**: Follow existing patterns and terminology

## Maintenance

This documentation is actively maintained and should be updated:
- **After major releases**: Update deployment procedures
- **After infrastructure changes**: Update operations guides
- **After security changes**: Update Firebase rules and security procedures
- **Monthly**: Review and validate all procedures

## Change Log

- `Updated: 2025-09-08T13:45:00-04:00 / 2025-09-08T17:45:00Z — Initial comprehensive deployment and operations documentation`

---

For questions about this documentation, please contact the development team or create an issue in the repository.