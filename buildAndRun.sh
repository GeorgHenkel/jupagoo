#!/bin/sh
mvn clean package && docker build -t georghenkel/jupagoo .
docker rm -f jupagoo || true && docker run -d -p 8080:8080 -p 4848:4848 --name jupagoo georghenkel/jupagoo 
