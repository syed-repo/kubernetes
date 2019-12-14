#Download base image ubuntu 16.04
FROM ubuntu:18.04

RUN apt update
RUN apt -y install python3

#install pip3 and flask
RUN apt -y install python3-pip
RUN pip3 install flask

RUN mkdir -p /appl/flask
COPY main.py  /opt/flask

#execute the flask application on boot
ENTRYPOINT ["python3.6","/appl/flask/main.py"]
#CMD tail -f /dev/null
