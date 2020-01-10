#Download base image ubuntu 16.04
FROM ubuntu:18.04

#install openjdk
RUN apt -y update
RUN apt -y install vim
RUN apt -y install redis-tools
RUN apt -y install openjdk-8-jdk

#install maven
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN mkdir -p /appl/maven

WORKDIR /appl/maven
COPY apache-maven-3.6.3-bin.tar.gz /appl/maven
RUN gunzip apache-maven-3.6.3-bin.tar.gz
RUN tar -xvf apache-maven-3.6.3-bin.tar

ENV M2_HOME /appl/maven/apache-maven-3.6.3
ENV M2 $M2_HOME/bin

ENV PATH $PATH:$JAVA_HOME/bin:$M2

#create the application folder structure
RUN mkdir -p /appl/redis-client/src/main/java/redis

WORKDIR /appl/redis-client
COPY RedisClient.java /appl/redis-client/src/main/java/redis
COPY pom.xml /appl/redis-client

#execute the container for test
CMD tail -f /dev/null
