FROM liquibase/liquibase:latest

COPY src/main/resources/db /liquibase/db

WORKDIR /liquibase