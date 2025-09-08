#!/bin/bash
# Setup script for wAether development environment
# This script sets up code quality tools and git hooks

echo "Setting up wAether development environment..."

# Check if we're in the project root
if [ ! -f "gradlew" ]; then
    echo "Error: Please run this script from the project root directory"
    exit 1
fi

# Make gradlew executable
chmod +x gradlew

echo "✅ Made gradlew executable"

# Install git pre-commit hook (optional)
read -p "Do you want to install the pre-commit hook for automatic code quality checks? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    if [ -f ".git/hooks/pre-commit" ]; then
        echo "⚠️  Pre-commit hook already exists. Backing up to pre-commit.backup"
        mv .git/hooks/pre-commit .git/hooks/pre-commit.backup
    fi
    
    cp scripts/pre-commit-hook.sh .git/hooks/pre-commit
    chmod +x .git/hooks/pre-commit
    echo "✅ Pre-commit hook installed"
else
    echo "⏭️  Skipping pre-commit hook installation"
fi

# Run initial code quality check
echo ""
echo "Running initial code quality check..."
./gradlew codeQuality

if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 Setup complete! Your development environment is ready."
    echo ""
    echo "Available commands:"
    echo "  ./gradlew formatKotlin  - Auto-format Kotlin code"
    echo "  ./gradlew lintKotlin    - Check code style and run analysis"
    echo "  ./gradlew codeQuality   - Run all code quality checks"
    echo ""
    echo "See README.md for more information about code quality tools."
else
    echo ""
    echo "⚠️  Initial code quality check failed. Please review the output above."
    echo "You may need to run './gradlew formatKotlin' to fix formatting issues."
fi