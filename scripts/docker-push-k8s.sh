#!/bin/bash

# Check if a tag is provided
if [ -z "$1" ]; then
  echo "Error: no tag provided"
  echo "Usage: $0 <tag>"
  exit 1
fi

TAG=$1

IMAGE_NAMES=(
    "employee-service"
    "inss-tax-service"
    "income-tax-service"
    "work-attendance-service"
    "payroll-service"
    "api-gateway"
    "keycloak"
)

for i in "${!IMAGE_NAMES[@]}"; do
  IMAGE_NAME="${IMAGE_NAMES[$i]}"
  FULL_IMAGE_NAME="$IMAGE_NAME:$TAG"
  echo "Pushing Docker image $FULL_IMAGE_NAME"

  # Push the Docker image
  docker push "localhost:32000/ps-k8s-$FULL_IMAGE_NAME"

  # Check if the push was successful
  if [ $? -eq 0 ]; then
    echo "Successfully push $FULL_IMAGE_NAME"
  else
    echo "Failed to push $FULL_IMAGE_NAME"
  fi
done
