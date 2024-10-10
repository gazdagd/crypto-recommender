# Crypto Recommender

### Built With
![Postgres][postgres-shield] ![Java][java-shield] ![Spring][spring-shield] <br>
![Apache Maven][maven-shield] ![Redis][redis-shield] ![Docker][docker-shield]

[postgres-shield]: https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white
[java-shield]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white
[spring-shield]: https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white
[maven-shield]: https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white
[redis-shield]: https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white
[docker-shield]: https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white

## Getting Started

The application can be built and run locally following the next steps.

### Prerequisites

The following software should be installed on your system
* java 21 or higher
* maven 3
* docker

### Building

1. Clone the repo
   ```sh
   git clone git@github.com:gazdagd/crypto-recommender.git
   ```
2. Build with maven
   ```sh
   cd crypto-recommender
   mvn clean install
   ```

### Testing
To be able to run the integration tests locally docker daemon must be run because it uses [Testcontainers](https://testcontainers.com/).

To run the tests:
```sh
   mvn verify
   ```

## Running application

There are two options to run the application locally. Both options are require to run docker daemon.

1. Run with docker
   ```sh
   docker compose up -d
   ```
2. Run from IDE - optimal for debugging
   In the integration-test module you can find the TestCryptoRecommenderServiceApplication class which contains a runnable main method. It will also use Testcontainers to run dependencies.
   <br><br>
3. Verify the application is running
   ```sh
   curl localhost:8080/actuator/health/readiness
   ```
## Exposed endpoints

You can find formal documentation of the REST API in openapi format in [rest-api](https://github.com/gazdagd/crypto-recommender/blob/master/rest-api/src/main/resources/openapi.yaml) module.
It is used to generate controllers. After running the application you can reach the docs [here](http://localhost:8080/swagger-ui/index.html#/).

## Technical overview

When the application starts it will read all csv files on the configured location. Parse every file and creates an application event
from every line in the files. 

The calculator logic is listening on these events and re-calculates global and daily aggregates of min/max/newest/oldest and normalized range.

With this the rest calls can be handled quickly since the requested data is pre-calculated and persisted. Also it makes possible to change data source easily 
for example to a kafka topic where new prices are coming in and the listener just maps the kafka events into the same application event. 

Crypto names are not hard-coded so it will automatically handle new cryptos.

The application will scale well with big data sets because it stores just aggregates and evaluate data eagerly.

### Containerization

The application has been containerized by using google's [jib maven plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin).
It will generate nice layered image for our application for minimal effort. 

This generated image is being used in the docker compose file to run the application locally. Also it could be used to create a kubernetes deployment.

### Rate limiting

Usually I would prefer to let nginx or some kind of gateway to handle rate limiting. It would be a simpler solution and can be centralized regarding more services.

To handle this on the application level I chose to use [bucket4j](https://github.com/bucket4j/bucket4j) with [redis](https://redis.io/) cache. In the response header
With using persistent cache the statelessness of the application remained so it is safe to run multiple instances of the application.

I included X-Rate-Limit-Remaining to indicate how many calls are allowed and X-Rate-Limit-Retry-After-Seconds
to let the client knows when it can call again. Every endpoint has its own limiting.
