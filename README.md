# kubernetes
Kubernetes

metrics server

https://stackoverflow.com/questions/54106725/docker-kubernetes-mac-autoscaler-unable-to-find-metrics 
    1. Delete any previous instance of metrics-server from your Kubernetes instance with kubectl delete -n kube-system deployments.apps metrics-server
    2. Clone metrics-server with git clone https://github.com/kubernetes-incubator/metrics-server.git
    3. Edit the file deploy/1.8+/metrics-server-deployment.yaml to override the default command by adding a command section that didn't exist before. The new section will instruct metrics-server to allow for an insecure communications session (don't verify the certs involved). Do this only for Docker, and not for production deployments of metrics-server:
       containers:
       - name: metrics-server
           image: k8s.gcr.io/metrics-server-amd64:v0.3.1
           command:
             - /metrics-server
             - --kubelet-insecure-tls
    4. Add metrics-server to your Kubernetes instance with kubectl create -f deploy/1.8+(if errors with the .yaml, write this instead: kubectl apply -f deploy/1.8+)
