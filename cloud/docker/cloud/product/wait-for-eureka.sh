#!/bin/sh
set -e

echo "⏳ 等待 Eureka 就绪..."
until wget -qO- http://cloud-eureka:8761/eureka/apps | grep -q '<applications>'; do
  echo "🔄 Eureka 暂未就绪，重试中..."
  sleep 5
done

echo "✅ Eureka 已就绪，启动应用..."
exec java -jar app.jar
