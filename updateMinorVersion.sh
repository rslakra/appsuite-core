#!/bin/bash
# Author: Rohtash Lakra
clear
echo
JAVA_VERSION=11
export JAVA_HOME=$(/usr/libexec/java_home -v $JAVA_VERSION)
echo "${JAVA_HOME}"
echo
mvn --batch-mode build-helper:parse-version versions:set -DnewVersion=${parsedVersion.majorVersion}.${parsedVersion.nextMinorVersion}.0-SNAPSHOT versions:commit
echo

