# Pristine Island 

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
It will build the project, generate an image from the source and deploy into the swarm with a postgres image.                    
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
- Once completed open a browser and navigate to (http://${docker_vm_ip}:8080/swagger-ui.html) for a list of API 
documentation

## Tests 
- To run all tests
````
mvn clean test
````

## Design and Implementation notes 

Bookings are stored in database with a list of dates representing the reserved dates, storing in 
that way, allows to use a DB constraint for the restriction of an unique reservation for a particular day.
In that way, if two or more reservations for the same day happens concurrently DB will complains preventing 
to have bookings with same days, without the needs of adding synchronization blocks on creation/update code.

Appropriated functional test cases are provided for concurrency at com.plugli.functional.controller.

Availability for a desired range is a simple calculation, that only require a set of reads against the DB, to lookup for 
reserved dates in the range.
