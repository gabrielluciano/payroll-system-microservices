#!/bin/bash


# Check if a tag is provided
if [ -z "$1" ]; then
  echo "Error: no tag provided"
  echo "Usage: $0 <tag>"
  exit 1
fi

TAG=$1

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

DIRS=(
    "$SCRIPT_DIR/../employee-service"
    "$SCRIPT_DIR/../inss-tax-service"
    "$SCRIPT_DIR/../income-tax-service"
    "$SCRIPT_DIR/../work-attendance-publish-service"
    "$SCRIPT_DIR/../payroll-service"
    "$SCRIPT_DIR/../discovery-server"
    "$SCRIPT_DIR/../api-gateway"
    "$SCRIPT_DIR/../auth-server"
)

IMAGE_NAMES=(
    "employee-service"
    "inss-tax-service"
    "income-tax-service"
    "work-attendance-service"
    "payroll-service"
    "discovery-server"
    "api-gateway"
    "keycloak"
)

# Check if both arrays have the same length
if [ ${#DIRS[@]} -ne ${#IMAGE_NAMES[@]} ]; then
  echo "Error: The number of directories and image names do not match."
  exit 1
fi

for i in "${!DIRS[@]}"; do
  DIR="${DIRS[$i]}"
  IMAGE_NAME="${IMAGE_NAMES[$i]}"
  FULL_IMAGE_NAME="$IMAGE_NAME:$TAG"

  # Check if the directory exists and contains a Dockerfile
  if [ -d "$DIR" ] && [ -f "$DIR/Dockerfile.compose" ]; then
    echo "Building Docker image for $FULL_IMAGE_NAME from $DIR..."

    # Build the Docker image
    docker build -f "$DIR/Dockerfile.compose" -t "gabrielluciano/ps-$FULL_IMAGE_NAME" "$DIR"

    # Check if the build was successful
    if [ $? -eq 0 ]; then
      echo "Successfully built $FULL_IMAGE_NAME"
    else
      echo "Failed to build $FULL_IMAGE_NAME"
    fi
  else
    echo "No Dockerfile found in $DIR, skipping..."
  fi
done
