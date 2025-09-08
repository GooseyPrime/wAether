# Contributing to wAether

Thank you for your interest in contributing to wAether! This document outlines the process for contributing to the project and explains our issue triage and labeling system.

## Issue Tracking and Triage Process

### Creating Issues

We use GitHub issue templates to ensure consistent and complete issue reports. When creating an issue, please:

1. **Choose the appropriate template**:
   - **Bug Report**: For reporting bugs or unexpected behavior
   - **Feature Request**: For suggesting new features or enhancements
   - **Documentation**: For documentation issues or improvements

2. **Fill out all required fields** in the template completely and accurately

3. **Use clear, descriptive titles** that start with the appropriate prefix:
   - `[BUG]` for bug reports
   - `[FEATURE]` for feature requests  
   - `[DOCS]` for documentation issues

### Issue Triage Process

Our issue triage process follows these steps:

#### 1. Initial Triage (within 48 hours)
- New issues are automatically labeled with `needs-triage`
- Maintainers review the issue for completeness and validity
- Issues are labeled with appropriate **type**, **component**, and **platform** labels
- Priority is assessed and **priority** labels are applied
- Status is updated to `needs-investigation` or `ready-for-work`

#### 2. Investigation Phase
Issues labeled `needs-investigation` require:
- Technical feasibility assessment
- Impact analysis
- Resource requirement estimation
- Dependencies identification

#### 3. Ready for Work
Issues labeled `ready-for-work` have:
- Clear acceptance criteria
- Defined scope
- No blocking dependencies
- Assigned priority level

#### 4. In Progress
- Issues being actively worked on are labeled `in-progress`
- Assignee should provide regular updates
- Issues blocking progress are labeled `blocked`

## Label System

### Label Categories

#### Type Labels (Required)
- `bug` - Something isn't working correctly
- `enhancement` - New feature or improvement request
- `documentation` - Documentation related issues
- `question` - Requests for clarification or help

#### Priority Labels (Required after triage)
- `priority/critical` - Security issues, app crashes, data loss
- `priority/high` - Major functionality broken, significant UX issues
- `priority/medium` - Minor bugs, nice-to-have features
- `priority/low` - Cosmetic issues, far-future enhancements

#### Component Labels (Recommended)
- `component/watch-face` - Watch face display and rendering
- `component/sensors` - Sensor data collection (magnetometer, location, etc.)
- `component/data` - Data processing, APIs, Firebase integration
- `component/ui` - User interface and user experience
- `component/build` - Build system, dependencies, CI/CD

#### Platform Labels
- `platform/wear-os` - Wear OS specific issues
- `platform/android` - General Android platform issues

#### Status Labels (Managed by maintainers)
- `needs-triage` - New issue requiring triage
- `needs-investigation` - Requires technical investigation
- `ready-for-work` - Ready for development
- `in-progress` - Actively being worked on
- `blocked` - Blocked by dependencies

#### Special Labels
- `good-first-issue` - Suitable for new contributors
- `help-wanted` - Seeking community contributions
- `duplicate` - Duplicate of existing issue
- `wontfix` - Will not be addressed
- `invalid` - Invalid or incomplete issue

### Label Application Guidelines

#### For Issue Reporters
- Do not manually apply labels (except during template selection)
- Provide complete information to help with proper labeling
- Update issues with new information as needed

#### For Maintainers
1. **Always apply** at least one type label and one priority label
2. **Add component labels** to help with assignment and filtering
3. **Update status labels** as work progresses
4. **Use platform labels** when issues are specific to Wear OS or Android
5. **Apply special labels** to encourage community participation

## Issue Assignment Process

### Self-Assignment
- Contributors can self-assign issues labeled `ready-for-work`
- Comment on the issue before starting work
- Limit concurrent assignments based on issue complexity

### Maintainer Assignment
- Critical and high-priority issues may be directly assigned
- Core maintainers handle `component/build` and security issues
- New contributors are guided to `good-first-issue` items

## Communication Guidelines

### Issue Comments
- Keep comments focused and constructive
- Provide regular updates on assigned issues
- Ask questions if requirements are unclear
- Use @mentions for specific attention

### Issue Lifecycle
1. **Open** → `needs-triage`
2. **Triaged** → `needs-investigation` or `ready-for-work`
3. **Investigated** → `ready-for-work` or `blocked`
4. **Assigned** → `in-progress`
5. **Completed** → **Closed** with reference to fixing PR

## Quality Standards

### Issue Quality
- Complete template information
- Clear reproduction steps (for bugs)
- Specific acceptance criteria (for features)
- Relevant context and screenshots

### Code Quality
- Follow existing code style and patterns
- Include appropriate tests
- Update documentation as needed
- Ensure CI/CD passes

## Getting Help

- **Questions**: Use [GitHub Discussions](https://github.com/GooseyPrime/wAether/discussions)
- **Security Issues**: Report privately via [Security Advisories](https://github.com/GooseyPrime/wAether/security/advisories/new)
- **Documentation**: Check existing docs and contribute improvements

## Recognition

Contributors are recognized through:
- Attribution in release notes
- Contributor acknowledgments
- Issue and PR history

Thank you for helping make wAether better for everyone!