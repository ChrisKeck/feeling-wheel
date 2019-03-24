# JHipster generated Docker-Compose configuration

## Usage

Launch all your infrastructure by running: `docker-compose up -d`.

## Configured Docker services

### Service registry and configuration server:
- [JHipster Registry](http://localhost:8761)

### Applications and dependencies:
- efwgateway (gateway application)
- efwgateway's postgresql database
- efwgateway's elasticsearch search engine
- efwservice (microservice application)
- efwservice's postgresql database
- efwservice's elasticsearch search engine

### Additional Services:

- Kafka
- Zookeeper
- [JHipster Console](http://localhost:5601)
- [Zipkin](http://localhost:9411)
