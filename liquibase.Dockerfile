FROM liquibase/liquibase:latest

COPY src/main/resources/db /liquibase/changelog

WORKDIR /liquibase/changelog