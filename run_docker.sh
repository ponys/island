#!/bin/bash

docker build -t island-app:1.0 .
docker stack deploy -c docker-compose.yml islandstack