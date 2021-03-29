#!/bin/sh
mvn clean package && docker build -t org.example/mongodb-example .
docker rm -f mongodb-example || true && docker run -d -p 8080:8080 -p 4848:4848 --name mongodb-example org.example/mongodb-example 
