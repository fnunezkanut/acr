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

### Flyway and setting up a local development database

To install, run, setup, populate a local postgres12 db on OSX:
 
```
brew install postgresql@12
brew services start postgresql@12
brew services list
pssql postgres
CREATE ROLE acr LOGIN PASSWORD 'acr';
GRANT ALL ON ALL TABLES IN SCHEMA public TO acr;
\q
````

Now that we have postgres (Same version as RDS) running, lets flyway migrate
```
cd ~/github/acr/
./gradlew flywayMigrate -i -Pflyway.url="jdbc:postgresql://localhost:5432/postgres"
```

### Running "testcontainer" db repo integration test

In order to actually excercise the code which interacts with progress, an "integrationTest" module exists in this project.

When run it downloads a docker postgres testcontainer (same version as RDS in "production") and does a flyway migration on it, followed by unit tests excercising the db connection code against real db, its fairly fast especially on subsequent runs, docker is required.

```
cd ~/github/acr/
./gradlew clean build integrationTest
```

### Documentation

TODO