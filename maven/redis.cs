NOTES:
** Please be patient while the chart is being deployed **
Redis can be accessed via port 6379 on the following DNS names from within your cluster:

my-redis-master.default.svc.cluster.local for read/write operations
my-redis-slave.default.svc.cluster.local for read-only operations


To get your password run:

    export REDIS_PASSWORD=$(kubectl get secret --namespace default my-redis -o jsonpath="{.data.redis-password}" | base64 --decode)

To connect to your Redis server:

1. Run a Redis pod that you can use as a client:

   kubectl run --namespace default my-redis-client --rm --tty -i --restart='Never' \
    --env REDIS_PASSWORD=$REDIS_PASSWORD \
   --image docker.io/bitnami/redis:5.0.7-debian-9-r12 -- bash

2. Connect using the Redis CLI:
   redis-cli -h my-redis-master -a $REDIS_PASSWORD
   redis-cli -h my-redis-slave -a $REDIS_PASSWORD

To connect to your database from outside the cluster execute the following commands:

    kubectl port-forward --namespace default svc/my-redis-master 6379:6379 &
    redis-cli -h 127.0.0.1 -p 6379 -a $REDIS_PASSWORD

======================================================================================================================


ubuntu@ip-172-31-4-214:~/kubernetes/maven$ kubectl get secret --namespace default my-release-redis -o jsonpath="{.data.redis-password}" | base64 --decode
TZsFi2gEAUubuntu@ip-172-31-4-214:~/kubernetes/maven$


ubuntu@ip-172-31-4-214:~/kubernetes/charts/redis-client$ redis-cli -h 127.0.0.1 -p 6379 -a TZsFi2gEAU
127.0.0.1:6379> hgetall user#1
1) "name"
2) "Peter"
3) "job"
4) "politician"
127.0.0.1:6379>

ubuntu@ip-172-31-4-214:~/kubernetes/maven$ kubectl port-forward --namespace default svc/my-release-redis-master 6379:6379
Forwarding from 127.0.0.1:6379 -> 6379
Forwarding from [::1]:6379 -> 6379
Handling connection for 6379
