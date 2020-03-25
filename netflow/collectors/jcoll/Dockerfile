#Download base image ubuntu 16.04
FROM ubuntu:18.04

#install openjdk
RUN apt -y update
RUN apt -y install vim
RUN apt -y install redis-tools
RUN apt -y install openjdk-8-jdk

RUN apt-get update && apt-get install --no-install-recommends -y \
  python python-dev python-setuptools python-pip \
  python3 python3-dev python3-setuptools python3-pip \
  gcc git openssh-client less curl \
  libxtst-dev libxext-dev libxrender-dev libfreetype6-dev \
  libfontconfig1 libgtk2.0-0 libxslt1.1 libxxf86vm1 \
  && rm -rf /var/lib/apt/lists/*

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
RUN mkdir -p /appl/collector

WORKDIR /appl/
COPY collector /appl/collector

EXPOSE 5555/udp

#execute the container for test
CMD tail -f /dev/null
