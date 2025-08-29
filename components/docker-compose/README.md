# docker-compose 说明


3. 创建专用监控用户（推荐）

   docker exec -it mysql mysql -uroot -p5kY9BaCxEWbsaQ9Swr

   -- 删除可能存在的旧用户
   DROP USER IF EXISTS 'exporter'@'%';

   -- 创建新监控用户
   CREATE USER 'exporter'@'%' IDENTIFIED BY '5kY9BaCxEWbsaQ9Swr';
   GRANT PROCESS, REPLICATION CLIENT, SELECT ON *.* TO 'exporter'@'%';
   FLUSH PRIVILEGES;

4. 检查网络连通性

docker run --rm --network=mynetwork mysql:8.0.29 mysql -h mysql -uexporter -p5kY9BaCxEWbsaQ9Swr -e "SHOW DATABASES;"

yapi

docker network create myYaPiNetwork


