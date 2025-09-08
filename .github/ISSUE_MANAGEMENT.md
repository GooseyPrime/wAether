# wAether GitHub Issue Management

This document outlines the issue tracking, labeling, and triage practices for the wAether project.

## Issue Templates

We provide structured issue templates to ensure consistent and actionable issue reports:

- **Bug Report** (`bug_report.yml`) - For reporting bugs and technical issues
- **Feature Request** (`feature_request.yml`) - For suggesting new features or enhancements
- **Documentation** (`documentation.yml`) - For documentation issues and improvements

## Labels

### Type Labels
- `bug` - Something isn't working correctly
- `enhancement` - New feature or request
- `documentation` - Improvements or additions to documentation
- `question` - Further information is requested
- `maintenance` - Code maintenance, refactoring, or technical debt

### Priority Labels
- `priority: critical` - Blocking issues that prevent app usage
- `priority: high` - Important issues that significantly impact functionality
- `priority: medium` - Moderate issues that should be addressed soon
- `priority: low` - Minor issues or nice-to-have improvements

### Component Labels
- `component: weather` - Weather data display and processing
- `component: space-weather` - Space weather monitoring and alerts
- `component: mood-tracking` - Mood logging and correlation features
- `component: watch-face` - Watch face rendering and design
- `component: sensors` - Device sensors and data collection
- `component: ui` - User interface and user experience
- `component: performance` - Performance optimization
- `component: accessibility` - Accessibility improvements

### Platform Labels
- `platform: wear-os` - Wear OS specific issues
- `platform: android` - Android platform issues
- `platform: emulator` - Android emulator specific issues

### Status Labels
- `status: needs-triage` - New issues that need initial review
- `status: confirmed` - Confirmed bugs or accepted feature requests
- `status: in-progress` - Currently being worked on
- `status: blocked` - Cannot proceed due to external dependencies
- `status: duplicate` - Duplicate of another issue
- `status: wontfix` - Will not be implemented or fixed
- `status: help-wanted` - Community contributions welcome
- `status: good-first-issue` - Good for newcomers

## Triage Process

### Initial Triage (within 48 hours)
1. **Review completeness** - Ensure issue has sufficient information
2. **Add type label** - Categorize as bug, enhancement, or documentation
3. **Add component label** - Identify which part of the app is affected
4. **Add platform label** - Specify the target platform if relevant
5. **Set initial priority** - Based on impact and severity
6. **Change status** from `needs-triage` to `confirmed` or request more information

### Priority Assignment Guidelines

#### Critical Priority
- App crashes or fails to start
- Data loss or corruption
- Security vulnerabilities
- Complete feature failure

#### High Priority
- Significant functionality broken
- Performance severely degraded
- Major usability issues
- Blocking API changes

#### Medium Priority
- Minor functionality issues
- Performance improvements
- UI/UX enhancements
- Non-critical feature requests

#### Low Priority
- Code cleanup
- Minor UI tweaks
- Nice-to-have features
- Documentation improvements

### Component Assignment

Issues should be tagged with the primary component affected:
- Use `component: weather` for weather data display, API integration
- Use `component: space-weather` for solar flares, geomagnetic data
- Use `component: mood-tracking` for mood logging, correlations
- Use `component: watch-face` for watch face rendering, complications
- Use `component: sensors` for magnetometer, location, device sensors
- Use `component: ui` for general interface issues
- Use `component: performance` for optimization needs
- Use `component: accessibility` for accessibility compliance

### Review Cycles

- **Weekly triage** - Review all `needs-triage` issues
- **Sprint planning** - Prioritize `confirmed` issues for development
- **Release planning** - Ensure critical/high priority issues are addressed

## Issue Lifecycle

1. **Created** - Issue submitted with `needs-triage` label
2. **Triaged** - Labels applied, priority set, status updated
3. **Confirmed** - Issue validated and ready for development
4. **In Progress** - Developer assigned, work started
5. **Review** - Pull request submitted for review
6. **Closed** - Issue resolved or declined

## Community Guidelines

- Be respectful and constructive in all discussions
- Provide clear, actionable feedback
- Test thoroughly before reporting bugs
- Search existing issues before creating new ones
- Use appropriate templates and provide complete information
- Follow up on requests for additional information

## Automation

- Issues automatically receive `needs-triage` label when created
- Stale issues are automatically marked after 60 days of inactivity
- Pull requests automatically close linked issues when merged