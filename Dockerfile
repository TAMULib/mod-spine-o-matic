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
ENV SPRING_DATASOURCE_PLATFORM='h2'
ENV SPRING_DATASOURCE_URL='jdbc:h2:./mod_spine_o_matic;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE'
ENV SPRING_DATASOURCE_DRIVERCLASSNAME='org.h2.Driver'
ENV SPRING_DATASOURCE_USERNAME='folio_admin'
ENV SPRING_DATASOURCE_PASSWORD='folio_admin'
ENV SPRING_H2_CONSOLE_ENABLED='true'
ENV SPRING_JPA_DATABASE_PLATFORM='org.hibernate.dialect.H2Dialect'
ENV SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT='org.hibernate.dialect.H2Dialect'
ENV OKAPI_TENANT='tamu'
ENV OKAPI_URL='https://folio-okapi-q2.library.tamu.edu'
ENV OKAPI_CREDENTIALS_USERNAME='tamu_admin'
ENV OKAPI_CREDENTIALS_PASSOWRD='admin'
ENV ACTIVE_PROCESSOR_COUNT='4'

#expose port
EXPOSE ${SERVER_PORT}

#run java command
CMD java -XX:ActiveProcessorCount=${ACTIVE_PROCESSOR_COUNT} -jar ./mod-spine-o-matic.jar \
  --logging.level.org.tamu=${LOGGING_LEVEL_TAMU} --server.port=${SERVER_PORT} --spring.datasource.platform=${SPRING_DATASOURCE_PLATFORM} \
  --spring.datasource.url=${SPRING_DATASOURCE_URL} --spring.datasource.driverClassName=${SPRING_DATASOURCE_DRIVERCLASSNAME} \
  --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
  --spring.h2.console.enabled=${SPRING_H2_CONSOLE_ENABLED} --spring.jpa.database-platform=${SPRING_JPA_DATABASE_PLATFORM} \
  --spring.jpa.properties.hibernate.dialect=${SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT} \
  --okapi.tenant=${TENANT_DEFAULT_TENANT} --okapi.url=${OKAPI_URL}
