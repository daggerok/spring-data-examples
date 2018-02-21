#docker rm -f -v oracle
#docker run --rm -d --shm-size=1g -p 1521:1521 -p 8080:8080 --name oracle alexeiled/docker-oracle-xe-11g
#docker rm -f -v rabbitmq
#docker run --rm -d -p 4369:4369 -p 5671:5671 -p 5672:5672 --name rabbitmq rabbitmq
#docker rm -f -v mongo
#docker run --rm -d -p 27017:27017 --name mongo mongo
docker rm -f -v redis
docker run --rm -d -p 6379:6379 --name redis redis
docker ps
