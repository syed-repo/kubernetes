ElasticSeearch
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
