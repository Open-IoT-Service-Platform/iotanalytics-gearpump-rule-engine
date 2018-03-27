FROM jamesdbloom/docker-java8-maven

RUN apt-get update -qq && apt-get install -y build-essential python-setuptools

RUN easy_install poster
RUN easy_install requests
RUN easy_install pip

ADD . /app

WORKDIR /app

RUN mvn clean install  -DskipTests
