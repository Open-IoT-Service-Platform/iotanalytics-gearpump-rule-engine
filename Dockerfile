FROM jamesdbloom/docker-java8-maven as build

RUN apt-get update -qq && apt-get install -y build-essential python-setuptools
ADD . /app
WORKDIR /app
RUN mvn clean install

FROM python:2.7.14-alpine

RUN easy_install poster
RUN easy_install requests
COPY --from=build /app/target/gearpump-rule-engine-*-jar-with-dependencies.jar /app/app.jar
COPY deployer/src /app/deployer/src
ENV RULE_ENGINE_PACKAGE_NAME /app/app.jar
WORKDIR /app/deployer/src
