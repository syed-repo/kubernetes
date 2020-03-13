FROM ubuntu:18.04

RUN apt update

RUN apt -y install git
RUN apt -y install python

RUN apt update
RUN apt -y install python-pip
RUN apt -y install vim
RUN apt -y install cmake

RUN mkdir -p /home/pycoll
WORKDIR /home/pycoll
RUN git clone https://github.com/tyjhart/flowanalyzer

RUN pip install --upgrade setuptools
RUN pip install --upgrade pip
RUN pip install --upgrade elasticsearch
RUN pip install --upgrade elasticsearch-curator
RUN pip install --upgrade IPy

RUN cp $(pwd)/flowanalyzer/Python/netflow_options_default.py $(pwd)/flowanalyzer/Python/netflow_options.py
COPY netflow_v9.py /home/pycoll/flowanalyzer/Python/

WORKDIR /home/pycoll
RUN git clone https://github.com/kosho/nfgen

WORKDIR /home/pycoll/nfgen
RUN cmake ./CMakeLists.txt
RUN make

WORKDIR /home/pycoll/flowanalyzer/Python

CMD tail -f /dev/null
