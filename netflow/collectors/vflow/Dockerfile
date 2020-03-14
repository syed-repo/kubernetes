FROM ubuntu:18.04

RUN apt update

RUN apt -y install git
RUN apt -y install python

RUN apt update
RUN apt -y install python-pip
RUN apt -y install vim
RUN apt -y install wget
RUN apt -y install cmake
RUN apt -y install default-jdk
RUN apt -y install curl

RUN mkdir -p /home/verizon
WORKDIR /home/verizon

# If the application need to be installed as an package
# RUN wget https://github.com/VerizonDigital/vflow/releases/download/v0.4.1/vflow-0.4.1-amd64.deb
# RUN dpkg -i vflow-0.4.1-amd64.deb 

# Install GoLang, requirement for vflow
RUN curl -O https://dl.google.com/go/go1.10.3.linux-amd64.tar.gz
RUN tar xvf go1.10.3.linux-amd64.tar.gz
RUN chown -R root:root ./go
RUN mkdir -p /home/verizon/work

ENV GOROOT=/home/verizon/go
ENV GOPATH=/home/verizon/work
ENV PATH=$PATH:$GOROOT/bin:$GOPATH/bin

# Install vflow
RUN git clone https://github.com/VerizonDigital/vflow
WORKDIR /home/verizon/vflow
COPY netflow_v9.go /home/verizon/vflow/vflow
RUN make build

RUN mkdir -p /etc/vflow
COPY vflow.conf /etc/vflow
COPY mq.conf /etc/vflow
COPY ipfix.elements /etc/vflow

# If kafka is required.. currently the kafka releated code is commented
# RUN wget http://www-us.apache.org/dist/kafka/2.4.0/kafka_2.13-2.4.0.tgz
# RUN tar xzf kafka_2.13-2.4.0.tgz

WORKDIR /home/verizon
RUN git clone https://github.com/kosho/nfgen

WORKDIR /home/verizon/nfgen
RUN cmake ./CMakeLists.txt
RUN make

WORKDIR /home/verizon

CMD tail -f /dev/null
