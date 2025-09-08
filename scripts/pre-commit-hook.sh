#!/bin/bash
# Pre-commit hook script for wAether project
# This script runs code quality checks before each commit
# 
# To install this hook, run:
# cp scripts/pre-commit-hook.sh .git/hooks/pre-commit
# chmod +x .git/hooks/pre-commit

echo "Running pre-commit code quality checks..."

# Check if gradlew exists and is executable
if [ ! -x "./gradlew" ]; then
    echo "Error: gradlew not found or not executable"
    exit 1
fi

# Run ktlint check
echo "Running ktlint..."
./gradlew ktlintCheck
if [ $? -ne 0 ]; then
    echo "❌ ktlint check failed. Run './gradlew ktlintFormat' to fix formatting issues."
    exit 1
fi

# Run detekt
echo "Running detekt..."
./gradlew detekt
if [ $? -ne 0 ]; then
    echo "❌ detekt analysis failed. Please fix the issues reported above."
    exit 1
fi

echo "✅ All code quality checks passed!"
exit 0