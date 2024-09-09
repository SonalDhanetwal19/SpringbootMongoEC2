#!/bin/bash

docker-compose up --force-recreate --build

sleep 5

docker exec mongo1 /scripts/rs-init.sh