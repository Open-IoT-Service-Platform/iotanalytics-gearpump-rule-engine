FROM jamesdbloom/docker-java8-maven

RUN apt-get update -qq && apt-get install -y build-essential python-setuptools

ADD pom.xml /app/pom.xml
ADD src /app/src

WORKDIR /app

RUN mvn clean install  -DskipTests




FROM ubuntu:xenial
RUN apt-get update -qq && apt-get upgrade -y

RUN apt-get install -y curl unzip openjdk-8-jre-headless python-setuptools

RUN curl --location --retry 3 --insecure https://github.com/gearpump/gearpump/releases/download/0.8.0/gearpump-2.11-0.8.0.zip -o tmp.zip && unzip -q tmp.zip && rm tmp.zip && chmod +x gearpump-2.11-0.8.0/bin/*

ADD gear.conf gearpump-2.11-0.8.0/conf/gear.conf

RUN sed -i -e "s/gearpump.root.logger=RollingFileAppender/gearpump.root.logger=RollingFileAppender,console/g" gearpump-2.11-0.8.0/conf/log4j.properties
RUN sed -i -e "s/gearpump.application.logger=ApplicationLogAppender/gearpump.application.logger=ApplicationLogAppender,console/g" gearpump-2.11-0.8.0/conf/log4j.properties

EXPOSE 8090

RUN easy_install poster
RUN easy_install requests
RUN easy_install pip
RUN pip install kafka-python

ADD . /app
COPY --from=0 /app/ /app
RUN chmod +x /app/bootstrap.sh

WORKDIR /app/
# CMD ["bash", "bootstrap.sh"]
CMD bash