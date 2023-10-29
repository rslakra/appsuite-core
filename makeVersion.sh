#!/bin/bash
# Author: Rohtash Lakra
echo
# Environment Settings
GIT_REPO_NAME=$(basename `git rev-parse --show-toplevel`)
GIT_BRANCH_NAME=$(git rev-parse --abbrev-ref HEAD)
GIT_REVISION=$(git rev-parse HEAD)
GIT_SHORT_REVISION=$(git rev-parse --short HEAD)
GIT_COMMIT_COUNT=$(git rev-list HEAD --count)
DATE_TIME=$( date +%Y%m%d.%H%M%S )
#echo "GIT_REPO_NAME: ${GIT_REPO_NAME}, GIT_BRANCH_NAME: ${GIT_BRANCH_NAME}, GIT_REVISION: ${GIT_REVISION}, GIT_SHORT_REVISION: ${GIT_SHORT_REVISION}, GIT_COMMIT_COUNT: ${GIT_COMMIT_COUNT}, DATE_TIME: ${DATE_TIME}"
GIT_REPO="${GIT_REPO:-${GIT_REPO_NAME}}"
GIT_BRANCH="${GIT_BRANCH:-${GIT_BRANCH_NAME}}"
#GIT_COMMIT="${GIT_COMMIT:-${GIT_REVISION}}"
GIT_COMMIT="${GIT_COMMIT:-${GIT_SHORT_REVISION}}"
SNAPSHOT="${SNAPSHOT:-$1}"
GIT_BRANCH_HASH="${GIT_BRANCH:0:16}"
GIT_REVISION_HASH="${GIT_COMMIT:0:16}"
PULL_REQUEST="${PULL_REQUEST}"
#echo "GIT_REPO: ${GIT_REPO}, GIT_BRANCH: ${GIT_BRANCH}, GIT_COMMIT: ${GIT_COMMIT}, GIT_BRANCH_HASH: ${GIT_BRANCH_HASH}, GIT_REVISION_HASH: ${GIT_REVISION_HASH}, PULL_REQUEST: ${PULL_REQUEST}"
#
# Version Restrictions
# - Max Length: 75 Characters
# - Per Component: 16 Characters
# - Alphabets [A-Za-z] Only - No Illegal Characters
#
VERSION="1.0.${GIT_COMMIT_COUNT}"

if [[ ! -z ${SNAPSHOT} ]]; then
    # For pull-request, no need to append date but add a .0 to the VERSION to make sure pull requests are always less than master
#    VERSION="${VERSION}-SNAPSHOT_${GIT_BRANCH_HASH}_${GIT_REVISION_HASH}_${DATE_TIME}"
    VERSION="${VERSION}-SNAPSHOT"
#else
#    SHORT_BRANCH_NAME="`echo _${FULL_BRANCH_NAME:0:9} | sed 's/^_master//'`"
#    VERSION="${VERSION}_${GIT_BRANCH_HASH}_${GIT_REVISION_HASH}_${DATE_TIME}"
fi
#VERSION="${VERSION}_${GIT_BRANCH_HASH}_${GIT_REVISION_HASH}_${DATE_TIME}"
#echo
#VERSION_LENGTH=`echo ${#VERSION}`
#echo "VERSION: ${VERSION}, VERSION_LENGTH: ${VERSION_LENGTH}"
echo "${VERSION}"
#echo

