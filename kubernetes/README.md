# Kubernetes Deployment

This folder contains Kubernetes resources YAML files for deploying a Kafka and microservices-based application on Kubernetes. The setup has been tested with MicroK8s on a single-node cluster.

## Prerequisites

Ensure MicroK8s is installed on your Linux machine. You can install it using Snap:

```sh
sudo snap install microk8s --classic
```

## Installation

### Step 1: Enable Microk8s Local Registry

This setup uses the local registry to store Docker images. To use a public registry, modify the image references accordingly.

```sh
microk8s enable registry
```

### Step 2: Enable Metallb Load Balancer

Metallb is a load balancer that can be used to expose services. During installation, you will be prompted to enter an IP range. Use the `hostname -I` or `ifconfig` commands to find the IP of your eth0 network interface, then select a range within this network.

```sh
microk8s enable metallb
```

### Step 3: Enable Helm

Helm is a package manager for Kubernetes. If Helm and Helm3 are not already enabled, enable them:

```sh
microk8s enable helm
microk8s enable helm3
```

### Step 4: Create Directories and Set Permissions

Create directories for Kafka, Prometheus, and Grafana data. Use appropriate permissions for production environments; here, 777 is for demonstration only.

```sh
sudo mkdir -p /data/kafka-data-0 /data/kafka-data-1 /data/kafka-data-2 /data/prometheus-data /data/grafana-data

sudo chmod -R 777 /data/kafka-data-0 /data/kafka-data-1 /data/kafka-data-2 /data/prometheus-data /data/grafana-data
```

### Step 5: Create Persistent Volumes

Create persistent volumes resources and verify their status:

```sh
microk8s kubectl apply -f kubernetes/persistent-volumes
microk8s kubectl get pv
```

### Step 6: Deploying Kafka

To deploy Kafka, use the following command to install the Bitnami/Kafka Helm chart:

```sh
microk8s helm3 install kafka oci://registry-1.docker.io/bitnamicharts/kafka --version 30.1.8 -f kubernetes/helm/kafka-config.yaml 

microk8s kubectl get pods # Check if the pods are running
```

### Step 7: Create a Secret for Kafka Authentication

Retrieve the Bitnami/Kafka helm chart generated password and create a new secret called `kafka-security` with key `sasl-username` equals `user1` and key `sasl-password` equals the generated password for Kafka authentication.

```sh
# Get the password
PASSWORD=$(microk8s kubectl get secret kafka-user-passwords --namespace default -o jsonpath='{.data.client-passwords}' | base64 -d | cut -d , -f 1)

# Create secret
microk8s kubectl create secret generic kafka-security --namespace default \
        --from-literal=sasl-username="user1" \
        --from-literal=sasl-password="$PASSWORD"
```

### Step 8: Deploying NGINX Ingress Controller

To expose http services, deploy the NGINX ingress controller using Helm:

```sh
microk8s helm3 install nginx-ingress oci://ghcr.io/nginxinc/charts/nginx-ingress --version 1.4.1
```

As we've installed MetalLB before, the nginx-ingress-controller service should have been assigned an external IP from the range we provided before. Verify its external IP address using the following command:

```sh
microk8s kubectl get svc nginx-ingress-controller -o jsonpath='{.status.loadBalancer.ingress[0].ip}'
```

### Step 9: Deploying ConfigMaps

Deploy configmaps for your application using the following commands:

```sh
microk8s kubectl apply -f kubernetes/configmaps
```

### Step 10: Deploying Secrets

Create secrets for database credentials and other sensitive data:

```sh
# Create metrics security secret
microk8s kubectl create secret generic metrics-security --namespace default \
        --from-literal=username="service" \
        --from-literal=password="password"

# Create Keycloak admin panel secret
microk8s kubectl create secret generic keycloak-admin --namespace default \
        --from-literal=user="admin" \
        --from-literal=password="password"

# Create Keycloak database credentials secret
microk8s kubectl create secret generic keycloak-db --namespace default \
        --from-literal=user="admin" \
        --from-literal=password="password"

# Create employee service DB secret
microk8s kubectl create secret generic employee-service-db --namespace default \
        --from-literal=user="user" \
        --from-literal=password="password"

# Create inss-tax service DB secret
microk8s kubectl create secret generic inss-tax-service-db --namespace default \
        --from-literal=user="user" \
        --from-literal=password="password"

# Create income-tax service DB secret
microk8s kubectl create secret generic income-tax-service-db --namespace default \
        --from-literal=user="user" \
        --from-literal=password="password"

# Create payroll service DB secret
microk8s kubectl create secret generic payroll-service-db --namespace default \
        --from-literal=user="user" \
        --from-literal=password="password"

# Verify the secrets
microk8s kubectl get secret
```

*PRO Tip*: If you want to grep all secrets and keys from the kubernetes yaml files you can use this command:

```sh
tree -if kubernetes | xargs cat | grep -A 2 secretKeyRef | grep -E "name:|key:"
```

### Step 11: Deploying StatefulSets and Services

Deploy statefulsets for your application using the following command:

```sh
microk8s kubectl apply -f kubernetes/statefulsets

# Verify if all pods are in running status using microk8s kubectl get pods
```

### Step 12: Deploying Deployments

Before we create our deployments, we need to build and push the images to the local registry. Two scripts in the scripts directory helps us with this task

```sh
# Build images with tag 1.0
./scripts/docker-build-k8s.sh 1.0

# Push images with tag 1.0 for local registry
./scripts/docker-push-k8s.sh 1.0
```

After that, we are able to deploy our microservices under the kubernetes/deployments directories

```sh
microk8s kubectl apply -f kubernetes/deployments

# Verify if all pods are in running status using microk8s kubectl get pods
```

### Step 13: Exposing Deployments and Services

Expose deployments and services for your application using the following command:

```sh
microk8s kubectl apply -f kubernetes/services
```

### Step 14: Exposing API Gateway and Keycloak with ingress resources

Expose the API gateway and keycloak with an ingress resource using the following command:

```sh
microk8s kubectl apply -f kubernetes/ingress
```

### Step 15: Configuring the hostnames

The ingress resources use `api` and `keycloak` as hostnames. Since we are in local development, to simulate a dns service, edit your hosts file to resolve these names to the external IP of the ingress controller.

1. Get the external IP address of the NGINX ingress controller:

```sh
microk8s kubectl get svc nginx-ingress-controller -o jsonpath='{.status.loadBalancer.ingress[0].ip}'
```

2. Edit the hosts file to map the `api` and `keycloak` hostnames to the external IP address:

```plaintext
# /etc/hosts
<EXTERNAL_IP> api
<EXTERNAL_IP> keycloak
```

After these steps, you can access the services using `http://api` and `http://keycloak` in your browser.

### Step 16: Create a Keycloak realm

Once the project is running, access Keycloak at `http://keycloak`.

In the Keycloak admin panel, log in with the credentials from the `keycloak-admin` secret and create a realm named `payroll-system`. Go to the realm settings and update the "Frontend URL" to `http://keycloak`.

You can then create a client and test users for the application. Refer to the Keycloak documentation for more details.
