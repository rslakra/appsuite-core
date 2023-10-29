#!/bin/bash
# Author: Rohtash Lakra
clear
VERSION="0.0"
# Build Version Function
function buildVersion() {
  GIT_COMMIT_COUNT=$(git rev-list HEAD --count)
  if [ $? -ne 0 ]; then
    VERSION="${VERSION}.0"
  else
    VERSION="${VERSION}.${GIT_COMMIT_COUNT}"
  fi
  SNAPSHOT="${SNAPSHOT:-$!}"
  if [[ ! -z ${SNAPSHOT} ]]; then
      VERSION="${VERSION}-SNAPSHOT"
  fi

  echo "${VERSION}";
}

echo
#JAVA_VERSION=11
#export JAVA_HOME=$(/usr/libexec/java_home -v $JAVA_VERSION)
echo "${JAVA_HOME}"
echo
#mvn clean install -DskipTests=true
SNAPSHOT_VERSION=$(buildVersion SNAPSHOT)
RELEASE_VERSION=$(buildVersion)
#echo "RELEASE_VERSION: ${RELEASE_VERSION}, SNAPSHOT_VERSION: ${SNAPSHOT_VERSION}"
#mvn clean install -DskipTests=true -DprojectVersion=$RELEASE_VERSION
mvn clean install -Drevision=$SNAPSHOT_VERSION
mvn install -Drevision=$RELEASE_VERSION -DskipTests=true
#mvn clean install -DskipTests=true -DprojectVersion=$(./makeVersion.sh SNAPSHOT)
echo
