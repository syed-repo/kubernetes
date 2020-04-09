FROM ubuntu:18.04

RUN apt -y update
RUN apt -y install vim
RUN apt -y install openjdk-8-jdk
RUN apt -y install apt-transport-https wget
RUN apt -y install gnupg2

#install maven
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN wget -qO - https://artifacts.elastic.co/GPG-KEY-elasticsearch | apt-key add -

COPY elastic.list /etc/apt/sources.list.d/elastic.list
RUN apt -y update

RUN apt -y install logstash

COPY logstash-netconf.conf /etc/logstash/conf.d/logstash-netconf.conf

CMD tail -f /dev/null
