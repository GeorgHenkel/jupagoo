#!/bin/sh
mvn clean package && docker build -t georghenkel/jupagoo .
docker rm -f jupagoo || true && docker run -d -p 8080:9080 --name jupagoo georghenkel/jupagoo 
