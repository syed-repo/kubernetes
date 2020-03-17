#!/bin/sh
counter=1
echo "Metrics Server Information" > /home/ubuntu/kubernetes/metrics.dat
while [ $counter -lt 1000 ]
do
    op=`kubectl top pod | grep netflow`
    dt=`date`
    echo "$dt $op" >> /home/ubuntu/kubernetes/metrics.dat
    echo "$dt $op"
    sleep 0.5
done
