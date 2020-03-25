#!/bin/bash
counter=1
echo "Metrics Server Information" > ./metrics.dat
while [ $counter -lt 1000 ]
do
    op=`kubectl top pod | grep jcoll`
    dt=`date`
    echo "$dt $op" >> ./metrics.dat
    echo "$dt $op"
    sleep 0.5
done
echo "Script completed"
