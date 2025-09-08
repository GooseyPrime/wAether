#!/bin/bash

# wAether Dependency Management Helper Script
# This script provides utilities for managing dependencies securely

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if we're in the project root
check_project_root() {
    if [[ ! -f "gradlew" || ! -f "settings.gradle.kts" ]]; then
        print_error "This script must be run from the project root directory"
        exit 1
    fi
}

# Make gradlew executable
ensure_gradlew_executable() {
    chmod +x gradlew
}

# Update dependency lock files
update_locks() {
    print_status "Updating dependency lock files..."
    ./gradlew dependencies --write-locks
    print_success "Lock files updated successfully"
}

# Run security scan
security_scan() {
    print_status "Running OWASP dependency vulnerability scan..."
    ./gradlew dependencyCheckAnalyze
    
    local report_path="app/build/reports/dependency-check/dependency-check-report.html"
    if [[ -f "$report_path" ]]; then
        print_success "Security scan completed. Report available at: $report_path"
        
        # Check if there are any vulnerabilities
        if grep -q "vulnerabilities" "$report_path"; then
            print_warning "Vulnerabilities found! Please review the report."
        else
            print_success "No vulnerabilities detected."
        fi
    else
        print_error "Security scan report not found"
    fi
}

# Build project
build_project() {
    print_status "Building project..."
    ./gradlew assembleDebug
    print_success "Build completed successfully"
}

# Run tests
run_tests() {
    print_status "Running unit tests..."
    ./gradlew testDebugUnitTest
    print_success "Tests completed successfully"
}

# Run lint checks
run_lint() {
    print_status "Running lint checks..."
    ./gradlew lintDebug
    print_success "Lint checks completed successfully"
}

# Full dependency update workflow
full_update_workflow() {
    print_status "Starting full dependency update workflow..."
    
    update_locks
    build_project
    run_tests
    run_lint
    security_scan
    
    print_success "Full dependency update workflow completed!"
    print_warning "Don't forget to commit the updated lock files!"
}

# Check for outdated dependencies
check_outdated() {
    print_status "Checking for outdated dependencies..."
    ./gradlew dependencyUpdates
    print_success "Dependency update check completed"
}

# Show help
show_help() {
    echo "wAether Dependency Management Helper"
    echo ""
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  update-locks    Update dependency lock files"
    echo "  security-scan   Run OWASP dependency vulnerability scan"
    echo "  build          Build the project"
    echo "  test           Run unit tests"
    echo "  lint           Run lint checks"
    echo "  full-update    Run complete dependency update workflow"
    echo "  check-outdated Check for outdated dependencies"
    echo "  help           Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 update-locks"
    echo "  $0 security-scan"
    echo "  $0 full-update"
}

# Main script logic
main() {
    check_project_root
    ensure_gradlew_executable
    
    case "${1:-help}" in
        "update-locks")
            update_locks
            ;;
        "security-scan")
            security_scan
            ;;
        "build")
            build_project
            ;;
        "test")
            run_tests
            ;;
        "lint")
            run_lint
            ;;
        "full-update")
            full_update_workflow
            ;;
        "check-outdated")
            check_outdated
            ;;
        "help"|*)
            show_help
            ;;
    esac
}

# Run main function with all arguments
main "$@"