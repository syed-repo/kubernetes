docker build -t ssalahud/flask-app:1.0 .
docker build --no-cache -t ssalahud/flask-app:1.0 .

docker rm container_name # can specify multiple containers
docker rmi image_id -f #force delete, if connected to mulitple images

docker tag bb087ba352a5 flask-app:latest

docker push ssalahud/flask-app:latest
