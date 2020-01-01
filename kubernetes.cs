ubuntu@ip-172-31-4-214:~/kubernetes/flask$ kubectl port-forward flask-ffb4d5bf4-h5hb5  5000:5000
Forwarding from 127.0.0.1:5000 -> 5000
Forwarding from [::1]:5000 -> 5000
Handling connection for 5000

kubectl delete -n kube-system deployments.apps metrics-server

kubectl edit deployment/helloworld-deployment

kubectl create -f helloworld-deployment.yaml && watch -n1 kubectl get pods
