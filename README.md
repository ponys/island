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

#Back-end Tech Challenge Exercise
An underwater volcano formed a new small island in the Pacific Ocean last month. All the conditions on the island seems perfect and it was
decided to open it up for the general public to experience the pristine uncharted territory.
The island is big enough to host a single campsite so everybody is very excited to visit. In order to regulate the number of people on the island, it
was decided to come up with an online web application to manage the reservations. You are responsible for design and development of a REST
API service that will manage the campsite reservations.

To streamline the reservations a few constraints need to be in place -
- The campsite will be free for all.
- The campsite can be reserved for max 3 days.
- The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
- Reservations can be cancelled anytime.
- For sake of simplicity assume the check-in & check-out time is 12:00 AM

#### System Requirements
The users will need to find out when the campsite is available. So the system should expose an API to provide information of the
availability of the campsite for a given date range with the default being 1 month.
Provide an end point for reserving the campsite. The user will provide his/her email & full name at the time of reserving the campsite
along with intended arrival date and departure date. Return a unique booking identifier back to the caller if the reservation is successful.
The unique booking identifier can be used to modify or cancel the reservation later on. Provide appropriate end point(s) to allow
modification/cancellation of an existing reservation

Due to the popularity of the island, there is a high likelihood of multiple users attempting to reserve the campsite for the same/overlapping
date(s). Demonstrate with appropriate test cases that the system can gracefully handle concurrent requests to reserve the campsite.
Provide appropriate error messages to the caller to indicate the error cases.

In general, the system should be able to handle large volume of requests for getting the campsite availability.
There are no restrictions on how reservations are stored as as long as system constraints are not violated.