#!/bin/sh
mvn clean package && docker build -t de.georghenkel/dadoop .
docker rm -f dadoop || true && docker run -d -p 8080:8080 -p 4848:4848 --name dadoop de.georghenkel/dadoop 
