ElasticSearch
==============
https://www.elastic.co/guide/en/logstash/current/plugins-codecs-netflow.html

https://github.com/elastic/beats

Netflow is part of their "Basic" Subscription

https://www.elastic.co/subscriptions

Netflow is covered by a proprietary license

https://github.com/elastic/elasticsearch/blob/0d8aa7527e242fbda9d84867ab8bc955758eebce/licenses/ELASTIC-LICENSE.txt


/var/log/elasticsearch/elasticsearch.log
/var/log/logstash/logstash-plain.log

JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
sudo journalctl --unit elasticsearch

curl -XGET http://localhost:9200/_status


http://YOURDOMAIN.com:5601

curl http://localhost:9200/_cat/indices?v
curl --silent http://localhost:9200/_cat/indices?v | grep logstash-2020.03.23 | tr -s ' \t' ',' | cut -d ',' -f 7

Dockerfile for Logstash

apt install openjdk-8-jre apt-transport-https wget
apt-get install -y gnupg2
wget -qO - https://artifacts.elastic.co/GPG-KEY-elasticsearch | apt-key add -

deb https://artifacts.elastic.co/packages/6.x/apt stable main

/etc/logstash/conf.d/logstash-netconf.conf

/usr/share/logstash/bin/logstash "--path.settings" "/etc/logstash"

curl -X GET "localhost:9200/logstash-2020.03.23/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match_all": {}
  },
  "size": 1,
  "sort": [
    {
      "@timestamp": {
        "order": "desc"
      }
    }
  ]
}
'
$ sudo apt install elasticsearch kibana
$ sudo systemctl restart kibana
$ sudo systemctl restart elasticsearch

/etc/kibana/kibana.yml


========================================================================================================

sudo apt -y update
sudo apt -y install openjdk-8-jdk
sudo apt -y install apt-transport-https wget
sudo apt -y install gnupg2

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
wget -qO - https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add –

create a file elastic.list with contents with the following contents 
deb https://artifacts.elastic.co/packages/6.x/apt stable main

cp elastic.list /etc/apt/sources.list.d/elastic.list
sudo apt -y update

sudo apt install elasticsearch kibana

update the kibana configuration, /etc/kibana/kibana.yml
•	server.host: "0.0.0.0"
•	elasticsearch.hosts: ["http://localhost:9200"]

update the elasticsearch configuration, /etc/elasticsearch/elasticsearch.yml
•	network.host: 0.0.0.0

sudo systemctl restart kibana
sudo systemctl restart elasticsearch

Elastic search should be accessible now:
curl  http://localhost:9200/_cat/indices?v

Kibana should be accessible on http://<ip>:5601

Install Logstash
sudo apt -y install logstash

Specify the input and output in the configuration file (example logstash-netconf.conf  with below contents) 
and place it in conf.d folder, 3.15.198.138 is the host running Elasticsearch

input {
  udp {
    port  => 2055
    codec => netflow
  }
}
output {
  elasticsearch { hosts => ["3.15.198.138:9200"] }
}

cp logstash-netconf.conf /etc/logstash/conf.d/logstash-netconf.conf

sudo systemctl restart logstash
================================================================================================================


