# 基础镜像（Alpine）
FROM eclipse-temurin:21-jdk-alpine

# 作者信息
LABEL maintainer="ylanhds@gmail.com"

# 替换为阿里云源（适用于 Alpine）
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
# 安装常用工具（供其他微服务使用）
RUN apk add --no-cache wget net-tools iproute2
# 设置时区
ENV TZ=Asia/Shanghai

# 挂载目录
VOLUME /home/cloud

# 创建目录并设置工作路径
RUN mkdir -p /home/cloud
WORKDIR /home/cloud

# 复制 JAR 文件和等待脚本
COPY ./jar/cloud-eureka-1.0.0.jar cloud-eureka.jar


CMD ["java", "-jar", "cloud-eureka.jar"]
