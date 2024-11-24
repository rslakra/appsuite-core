#Author: Rohtash Lakra
# --------------------------------------------------------------
# Generic Variables
# --------------------------------------------------------------
TIMESTAMP:=$(date +%s)
DATE_TIMESTAMP:=$(date '+%Y-%m-%d')
ROOT_DIR:=${PWD}
# --------------------------------------------------------------
# MODIFY FROM HERE
# --------------------------------------------------------------
# App Static Variables
PROJECT_OWNER=Rohtash
PROJECT_NAME=appsuite-core
DOCKER_CONTAINER_NAME=appsuite-core
DOCKER_REPOSITORY:=${PROJECT_OWNER}/${PROJECT__NAME}

# App Dynamic Variables
ifndef APP_NAME
APP_NAME=appsuite-core
endif
#
# ifndef HOST_PORT
# HOST_PORT=8082
# endif
#
# ifndef APP_PORT
# APP_PORT=8082
# endif
#
ifndef DOCKER_APP_TAG
DOCKER_APP_TAG=latest
#DOCKER_APP_TAG=$(date +%Y%m%d%H%M%S)
endif

# Variables that depend on git
ifndef GIT_BRANCH
GIT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
endif
#GIT_BRANCH_SLUG=$(subst /,-,$(GIT_BRANCH))

ifndef GIT_BUILD_NUMBER
GIT_BUILD_NUMBER=99999
endif

ifndef GIT_COMMIT
GIT_COMMIT=$(git rev-parse HEAD)
endif
GIT_SHORT_COMMIT=${GIT_COMMIT:0:8}

# Makefile configs

# .PHONY defines parts of the makefile that are not dependant on any specific file
# This is most often used to store functions
.PHONY: help

# Defines the default target that `make` will to try to make, or in the case of a phony target, execute the specified
# commands. This target is executed whenever we just type `make`
.DEFAULT_GOAL: help


all: help



# The @ makes sure that the command itself isn't echoed in the terminal
# Put it first so that "make" without argument is like "make help".
# Catch-all target: route all unknown targets
define find.functions
	@# @fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -e 's/\\$$//' | sed -e 's/##//'
    @printf "%-25s %s\n" "Target" "Description"
    @printf "%-25s %s\n" "----------------" "----------------"
    @make -pqR : 2>/dev/null \
        | awk -v RS= -F: '/^# File/,/^# Finished Make data base/ {if ($$1 !~ "^[#.]") {print $$1}}' \
        | sort \
        | egrep -v -e '^[^[:alnum:]]' -e '^$@$$' \
        | xargs -I _ sh -c 'printf "%-25s " _; make _ -nB | (grep -i "^# Help:" || echo "") | tail -1 | sed "s/^# Help: //g"'
endef

# A hidden target
.hidden:

help:
	@echo
	@echo 'The following commands can be used:'
	$(call find.functions)
	@echo


#Builds Docker Image
docker-build-image: ## Builds Docker Image
	@# Help: Builds the docker image of an app
	@echo "Building Docker Image ..."
	#docker build --progress=plain -t ${DOCKER_REPOSITORY}:${DOCKER_APP_TAG} --target ${PROJECT_NAME} .
	docker build -t ${DOCKER_CONTAINER_NAME}:${DOCKER_APP_TAG} .

# Runs the Docker Container
docker-run-container: ## Runs Docker Container
	@# Help: Runs the docker container of an app as background service
	@echo "Running Docker Container ..."
	#docker run --name ${DOCKER_CONTAINER_NAME} --rm -p ${HOST_PORT}:${APP_PORT} -d -e APP_NAME=${APP_NAME} ${DOCKER_APP_TAG}
	docker run --name ${DOCKER_CONTAINER_NAME} --rm -p ${HOST_PORT}:${APP_PORT} -d ${PROJECT_NAME}:${DOCKER_APP_TAG}

# Builds Docker Image and Run it.
docker-build-all: ## Builds Docker Image and Runs it
	@# Help: Builds the docker image of an app and runs it
	@echo "Building Docker Image and Running it ..."
	docker-build-image
	docker-run-container

# Shows Docker Container Logs
docker-log-container: ## Shows Docker Container Logs
	@# Help: Shows the docker container's log of an app
	@echo "Showing Docker Container Logs [${DOCKER_CONTAINER_NAME}] ..."
	docker logs -f ${DOCKER_CONTAINER_NAME}

# Executes the bash shell in the specified container, giving you a basic shell prompt.
# To exit back out of the container, type exit then press ENTER:
docker-exec-container: ## Executes the 'bash' shell in the container
	@# Help: Executes the 'bash' shell in the container, giving you a basic shell prompt.
	@echo "Executing Docker Container [${DOCKER_CONTAINER_NAME}] ..."
	docker exec -it ${DOCKER_CONTAINER_NAME} bash

# Stops Docker Container
docker-stop-container: ## Stops Docker Container
	@# Help: Stops the docker container of an app
	@echo "Stopping Docker Container [${DOCKER_CONTAINER_NAME}] ..."
	docker stop ${DOCKER_CONTAINER_NAME}

# In this context, the *.project pattern means "anything that has the .project extension"
clean: ## Remove build and cache files
clean:
	@# Help: Cleans up the docker container, and image of an app
	@echo "Cleaning up ..."
	docker-stop-container
	rm -rf $(VENU)
	rm -rf $(REMOVE_FILES) *.project
	find . -name '*.py[co]' -delete
	find . -type f -name '*.py[co]' -delete

