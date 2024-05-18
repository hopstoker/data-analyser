#!/bin/bash

gradle clean bootJar
docker build -t hopstoker/data-analyser .  --platform=linux/amd64
docker run -p 8080:8080 hopstoker/data-analyser
