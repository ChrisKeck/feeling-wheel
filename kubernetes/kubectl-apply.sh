#!/bin/bash
# Files are ordered in proper order with needed wait for the dependent custom resource definitions to get initialized.
# Usage: bash kubectl-apply.sh

kubectl apply -f registry/
kubectl label namespace default istio-injection=enabled
kubectl apply -f efwgateway/
kubectl apply -f efwservice/
kubectl apply -f messagebroker/
kubectl apply -f console/
