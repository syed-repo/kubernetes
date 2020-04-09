helm delete netflow --purge

helm install --name=netflow -f ./values.yaml .
python netflow_v9.py -log=debug

ssalahud/pycoll-app

wget https://www.apache.org/dyn/closer.cgi?path=/kafka/0.11.0.0/kafka_2.11-0.11.0.0.tgz
tar -xzf kafka_2.11-0.11.0.0.tgz
cd kafka_2.11-0.11.0.0
Kafka uses ZooKeeper so you need to first start a ZooKeeper server if already you don't have one

bin/zookeeper-server-start.sh config/zookeeper.properties
start the Kafka server

bin/kafka-server-start.sh config/server.properties

apt install default-jdk



apt install curl
curl -O https://dl.google.com/go/go1.10.3.linux-amd64.tar.gz
tar xvf go1.10.3.linux-amd64.tar.gz
chown -R root:root ./go
mkdir work
export GOROOT=/home/verizon/go
export GOPATH=/home/verizon/work
export PATH=$PATH:$GOROOT/bin:$GOPATH/bin

cd vflow
make build


root@vflow-6bf66b7758-jgnpp:/home/verizon/vflow/vflow# ./vflow --verbose -ipfix-enabled=false -netflow5-enabled=false -sflow-enabled=false -netflow9-workers=1
[vflow] 2020/03/13 10:35:14 open /etc/vflow/vflow.conf: no such file or directory
[vflow] 2020/03/13 10:35:14 the full logging enabled
[vflow] 2020/03/13 10:35:14 options.go:208: Welcome to vFlow v.0.7.0 Apache License 2.0
[vflow] 2020/03/13 10:35:14 options.go:209: Copyright (C) 2018 Verizon. github.com/VerizonDigital/vflow
[vflow] 2020/03/13 10:35:14 stats.go:121: starting stats web server ...
[vflow] 2020/03/13 10:35:14 sflow.go:91: sflow has been disabled
[vflow] 2020/03/13 10:35:14 ipfix.go:95: ipfix has been disabled
[vflow] 2020/03/13 10:35:14 netflow_v9.go:112: netflow v9 is running (UDP: listening on [::]:4729 workers#: 1)
[vflow] 2020/03/13 10:35:14 netflow_v5.go:88: netflowv5 has been disabled
[vflow] 2020/03/13 10:35:14 sarama.go:81: open /etc/vflow/mq.conf: no such file or directory
[vflow] 2020/03/13 10:35:14 netflow_v9.go:126: kafka: client has run out of available brokers to talk to (Is your cluster reachable?)
root@vflow-6bf66b7758-jgnpp:/home/verizon/vflow/vflow#

helm install --name=vflow -f ./values.yaml .

kubectl cp vflow-876df76dd-xpf85:/home/verizon/vflow/vflow/netflow_v9.go .




