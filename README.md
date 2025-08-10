# Serial Validator
Sample project of a Spring Boot application that performs like a serial validator.

## How to run locally

To run the serial validator locally, the only thing required is to run:
`mvn spring-boot:run`

## How to run on Kubernetes
To run the project on Kubernetes locally (using Minikube), follow these steps:
- Run `mvn spring-boot:package` to build the JAR file;
- Run `docker build -t serial-validator:latest .` to build the docker image;
- Apply the deployment manifest `minikube kubectl -- apply -f .\deployment\k8s\deployment.yaml` to create pods.
- Apply the service manifest `minikube kubectl -- apply -f .\deployment\k8s\service.yaml` to expose the app.
- Run `minikube service serial-validator` to expose and access the service via Minikube.

> Note: I used **minikube** to run the kubernetes cluster locally, so the commands are going to refers to minikube for
> the sake of the interview :)

## Terraform notes
I created a simple Terraform configuration to deploy an ECS cluster using EC2 instances.
This is a minimal example intended to demonstrate the basic approach rather than a production-ready setup.
- The entire configuration is contained in a single Terraform file for simplicity.
- For real-world projects, I would organize Terraform code into multiple files/modules and maintain it in a dedicated repository.
- I included it here within the same repo to keep the demo self-contained, though I’m aware this is not ideal for larger or more complex projects.
> I just wanted to highlight this because pushing everything in the same repository kinda makes me feel bad :)