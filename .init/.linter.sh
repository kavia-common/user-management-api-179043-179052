#!/bin/bash
cd /home/kavia/workspace/code-generation/user-management-api-179043-179052/user_management_backend
./gradlew checkstyleMain
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

