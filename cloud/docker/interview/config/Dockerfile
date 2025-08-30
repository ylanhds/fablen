# 基础镜像（Alpine）
FROM eclipse-temurin:21-jdk-alpine

# 构建参数（必须放在FROM之后，其他指令之前）
ARG APP_VERSION=latest
ARG APP_NAME=cloud-config

# 元数据标签
LABEL maintainer="ylanhds@gmail.com" \
      version="${APP_VERSION}" \
      application="${APP_NAME}"

# 替换为阿里云源（适用于 Alpine）
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 设置时区和环境变量
ENV TZ=Asia/Shanghai \
    APP_VERSION=${APP_VERSION} \
    APP_HOME=/home/cloud

# 安装curl用于健康检查（Alpine镜像使用apk）
RUN apk add --no-cache curl

# 创建目录并设置工作路径
RUN mkdir -p ${APP_HOME}
WORKDIR ${APP_HOME}

# 复制文件（使用分层COPY提高构建效率）
COPY ./jar/${APP_NAME}-*.jar app.jar
COPY wait-for-eureka.sh .

# 权限设置
RUN chmod +x wait-for-eureka.sh && \
    chown 1000:1000 ${APP_HOME}

# 健康检查（使用curl替代wget）
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -sf http://localhost:8080/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["sh", "./wait-for-eureka.sh"]
CMD ["java", "-jar", "app.jar", "--app.version=${APP_VERSION}"]