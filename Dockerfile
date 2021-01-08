# build base image
FROM maven:3-openjdk-11-slim as maven

# copy pom.xml
COPY ./pom.xml ./pom.xml

# copy src files
COPY ./src ./src

# build
RUN mvn package

# final base image
FROM openjdk:11-jre-slim

# set deployment directory
WORKDIR /mod-spine-o-matic

# copy over the built artifact from the maven image
COPY --from=maven /target/mod-spine-o-matic*.jar ./mod-spine-o-matic.jar

#Settings
ENV LOGGING_LEVEL_TAMU='INFO'
ENV SERVER_PORT='9000'
ENV OKAPI_TENANT='tamu'
ENV OKAPI_URL='https://folio-okapi-q2.library.tamu.edu'
ENV OKAPI_CREDENTIALS_USERNAME='tamu_admin'
ENV OKAPI_CREDENTIALS_PASSWORD='admin'
ENV ACTIVE_PROCESSOR_COUNT='4'

#expose port
EXPOSE ${SERVER_PORT}

#run java command
CMD java -XX:ActiveProcessorCount=${ACTIVE_PROCESSOR_COUNT} -jar ./mod-spine-o-matic.jar \
  --logging.level.org.tamu=${LOGGING_LEVEL_TAMU} --server.port=${SERVER_PORT} \
  --okapi.tenant=${OKAPI_TENANT} --okapi.url=${OKAPI_URL} \
  --okapi.credentials.username=${OKAPI_CREDENTIALS_USERNAME} --okapi.credentials.password=${OKAPI_CREDENTIALS_PASSWORD}
