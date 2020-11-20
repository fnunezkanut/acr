# ACR - Academic Course Registration demo

Sample spring boot service to model an Academic Course Registration system


### Requirements
* JDK 1.8
* docker (needed for testcontainers db integration tests)
* gradle (gradle wrapper is included)

### Developing locally

TODO


### Building with gradle

```
cd ~/github/acr/
./gradlew cb
```
This cleans up project, builds it and runs jacoco coverage report

### Packaging up and running locally in docker

```
cd ~/github/acr/
./gradlew cb
docker build --no-cache -t acr:latest -f Dockerfile
docker run -d --rm -p 8080:8080 acr:latest
curl "http://localhost:8080/v1/version"
#docker ps -a
```

### Documentation

TODO