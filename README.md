# Pristine Island - Upgrade - Back-end Tech Challenge

## Getting Started
The project can either be run locally or using docker

### Running using docker

## Prerequisites
- Docker Installed and configured 
- Docker swarm mode enabled
 
## Running
- On a terminal run the script 
````
./run_docker.sh
````
Tt will build the project, generate an image from the source and deploy into the swarm with a postgres image.                    
- Once completed open a browser and navigate to (http://${docker_vm_ip}:8080/swagger-ui.html) for a list of API 
documentation

### Running locally 

## Prerequisites
- Maven 3.5.2
- Postgres 9.4 running locally with user : postgres, pass : postgres 
- java 1.8

## Running 
- On a terminal run : 
````
mvn clean spring-boot:run
````
## Tests 
- To run all tests
````
mvn clean test
````

