#!/bin/bash

# 使用说明
usage() {
  echo "Usage: ./deploy.sh [base|build|version|modules|stop|clean|logs|purge]"
  echo "  purge   - 清理无用Docker镜像"
  echo "  base     - 启动基础设施（MySQL/LDAP/Eureka）"
  echo "  build    - 强制重建镜像（带版本控制）"
  echo "  version  - 查看当前运行版本"
  echo "  modules  - 启动业务微服务"
  echo "  stop     - 停止所有微服务"
  echo "  clean    - 清理容器（保留基础设施）"
  echo "  logs     - 查看微服务日志"
  exit 1
}

# 检查依赖
check_dependencies() {
  if ! command -v docker-compose &> /dev/null; then
    echo "❌ docker-compose 未安装"
    exit 1
  fi
}

# 获取服务日志
get_service_logs() {
  local service=$1
  echo "🔍 检查日志：docker-compose logs $service"
  docker-compose logs --tail=20 $service
}

# 启动基础设施
base() {
  check_dependencies
  echo "🔌 启动基础设施服务..."

  local compose_cmd="docker-compose up -d"
  if docker-compose up --help | grep -q '\-\-no-pull'; then
    compose_cmd+=" --no-pull"
  fi

  if ! $compose_cmd \
    cloud-mysql \
    cloud-ldap \
    cloud-ldap-admin \
    cloud-eureka; then
    echo "❌ 基础设施启动失败"
    get_service_logs cloud-eureka
    exit 1
  fi

  echo "⏳ 等待基础设施初始化(5秒)..."
  sleep 5

  # 验证Eureka是否真正启动
  if ! docker-compose exec -T cloud-eureka \
    curl -sfS http://cloud-eureka:8761/actuator/health | grep -q 'UP'; then
    echo "❌ Eureka未正常启动"
    get_service_logs cloud-eureka
    exit 1
  fi

  echo "✅ 基础设施已启动"
}

# 构建微服务
build() {
  check_dependencies

  # 强制清理旧镜像（关键修改）
  echo "🧹 清理旧镜像..."
  docker-compose rm -fvs cloud-eureka cloud-auth cloud-gateway cloud-product 2>/dev/null
  docker rmi $(docker images | grep 'cloud-' | awk '{print $3}') 2>/dev/null || true

  # 带版本号的构建（示例使用git commit作为版本）
  VERSION=$(git rev-parse --short HEAD 2>/dev/null || date +%Y%m%d)
  echo "🏗️ 构建微服务镜像 (版本: $VERSION)..."

  services=(
    cloud-eureka
    cloud-auth
    cloud-product
    cloud-gateway
  )

  for service in "${services[@]}"; do
    echo "🔨 构建 $service..."

    # 强制重建并打标签（关键修改）
    if ! docker-compose build --no-cache --build-arg APP_VERSION=$VERSION $service; then
      echo "❌ $service 构建失败"
      exit 1
    fi

    # 为镜像打上版本标签
    docker tag docker-$service:latest docker-$service:$VERSION
  done

  echo "✅ 所有微服务已构建 (版本: $VERSION)"
}


# 新增版本检查命令
version() {
  echo "🔍 服务版本检查："
  docker-compose ps | awk '{print $1}' | xargs -I{} sh -c \
    'echo -n "{} : "; docker inspect --format "{{.Config.Labels.version}}" {} || echo "N/A"'
}

# 启动微服务
modules() {
  check_dependencies

  # 确保Eureka已运行
  if ! docker-compose ps | grep -q "cloud-eureka"; then
    base
  fi

  echo "⏳ 检查Eureka状态..."
  local max_retries=30 interval=3 retry_count=0

  # 使用wget进行健康检查（已验证可用）
  while ! docker-compose exec -T cloud-eureka \
    wget -qO- http://cloud-eureka:8761/actuator/health | grep -q '"status":"UP"'; do
    ((retry_count++))

    # 每3次重试显示最新日志
    if (( retry_count % 3 == 0 )); then
      echo "📜 最近日志："
      docker-compose logs --tail=3 cloud-eureka | awk '{print "    | " $0}'
    fi

    if [ $retry_count -ge $max_retries ]; then
      echo "❌ Eureka健康检查失败"
      echo "可能原因："
      echo "1. 应用启动超时 => 增加等待时间：修改脚本中的max_retries和interval参数"
      echo "2. 资源不足 => 检查：docker stats"
      echo "3. 端口冲突 => 检查：netstat -tulnp | grep 8761"
      get_service_logs cloud-eureka
      exit 1
    fi
    echo "🔄 等待中... ($retry_count/$max_retries)"
    sleep $interval
  done

  echo "✅ Eureka已就绪，状态："
  docker-compose exec -T cloud-eureka wget -qO- http://cloud-eureka:8761/actuator/health | jq . 2>/dev/null || \
    docker-compose exec -T cloud-eureka wget -qO- http://cloud-eureka:8761/actuator/health

  echo "🚀 启动业务微服务..."
  local services=(
    cloud-auth
    cloud-gateway
    cloud-product
  )

  for svc in "${services[@]}"; do
    echo "▸ 启动 $svc..."
    if docker-compose up -d $svc; then
      echo "  ✅ 成功"
    else
      echo "❌ $svc 启动失败"
      get_service_logs $svc
      exit 1
    fi
  done
}


# 停止服务
stop() {
  check_dependencies
  echo "🛑 停止微服务..."
  if ! docker-compose stop \
    cloud-eureka \
    cloud-auth \
    cloud-gateway \
    cloud-product; then
    echo "❌ 停止服务时出错"
    exit 1
  fi
}

# 清理容器
clean() {
  check_dependencies
  echo "🧹 清理微服务容器..."

  if ! docker-compose rm -fvs \
    cloud-eureka \
    cloud-auth \
    cloud-gateway \
    cloud-product; then
    echo "❌ 清理容器时出错"
    exit 1
  fi

  echo "✅ 已清理微服务，保留以下基础设施:"
  docker-compose ps -a | grep -E 'cloud-mysql|cloud-ldap'
}

# 查看日志
logs() {
  check_dependencies
  echo "📜 微服务日志:"
  docker-compose logs -f \
    cloud-eureka \
    cloud-auth \
    cloud-gateway \
    cloud-product
}

# 新增镜像清理功能
purge() {
  echo "🧹 开始清理无用镜像..."

  # 安全清理悬空镜像（tag为<none>的）
  echo "▸ 清理悬空镜像..."
  dangling_images=$(docker images -f "dangling=true" -q)
  if [ -z "$dangling_images" ]; then
    echo "   ✅ 无悬空镜像可清理"
  else
    docker rmi $dangling_images 2>/dev/null | while read line; do echo "   🗑️ $line"; done
  fi

  # 清理未被使用的业务镜像（按名称匹配）
  echo "▸ 清理业务镜像..."
  for img in $(docker images --format "{{.Repository}}:{{.Tag}}" | grep 'cloud-'); do
    # 检查是否有容器在使用该镜像
    if ! docker ps -a --filter "ancestor=$img" --format "{{.ID}}" | grep -q .; then
      echo "   🗑️ 删除 $img"
      docker rmi $img >/dev/null
    else
      echo "   🔒 $img 正在使用中（跳过）"
    fi
  done

  # 显示清理后空间状态
  echo "📊 存储空间状态："
  docker system df --format "table {{.Type}}\t{{.TotalCount}}\t{{.Size}}\t{{.Reclaimable}}"
}


# 单独封装 Nginx 操作
nginx() {
  check_dependencies
  cd "$(dirname "$0")" || { echo "无法切换到脚本目录"; exit 1; }

  local version=$(git rev-parse --short HEAD 2>/dev/null || date +%Y%m%d)
  local image_name=cloud-nginx
  local full_image=${image_name}:${version}

  echo "正在执行 nginx 操作 (版本: $version)"

  # 检查前端是否已构建
  if [ ! -d "./nginx/html/dist" ]; then
    echo "❌ 错误：前端 dist 目录不存在，请先构建前端项目"
    echo "建议执行："
    echo "  cd html && npm install && npm run build"
    exit 1
  fi

  # 检查 Dockerfile 是否存在
  if [ ! -f "./nginx/Dockerfile" ]; then
    echo "❌ 错误：Dockerfile 文件不存在，请确认路径：./nginx/Dockerfile"
    exit 1
  fi

  echo "正在停止 nginx 操作......."
  docker-compose stop cloud-nginx

  # 清理旧资源（仅针对 nginx）
  echo "▸ 清理旧版 nginx 资源..."
  # 1. 停止并移除当前运行的 nginx 容器
  docker-compose rm -fsv cloud-nginx 2>/dev/null || true

  # 2. 删除所有 nginx 相关镜像（按名称过滤）
  docker images --format "{{.Repository}}:{{.Tag}}" | grep "^cloud-nginx" | xargs -r docker rmi 2>/dev/null || true
  # 3. 清理 dangling 镜像（可选）
  docker image prune -f 2>/dev/null || true
  # 构建（关键修改：使用项目根目录作为构建上下文）
  echo "▸ 构建 ${full_image}..."
  if ! docker build -t $full_image -f ./nginx/Dockerfile ./; then
    echo "❌ Nginx 构建失败"
    exit 1
  fi

  # 启动容器
  docker tag $full_image $image_name:latest
  if ! docker-compose up -d $image_name; then
    echo "❌ 启动 ${image_name} 失败"
    get_service_logs $image_name
    exit 1
  fi

  echo "✅ Nginx 已成功构建并启动（版本: $version）"
}





# 主逻辑
case "$1" in
  "purge")   purge;;
  "base")    base;;
  "build")   build;;
  "modules") modules;;
  "stop")    stop;;
  "clean")   clean;;
  "logs")    logs;;
  "nginx")   nginx;;
  *)         usage;;
esac