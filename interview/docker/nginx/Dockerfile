# 基础镜像
FROM nginx:latest

# 作者信息
LABEL maintainer="ylanhds@gmail.com"

# 创建目录
RUN mkdir -p /home/interview/projects/frontend
WORKDIR /home/interview/projects/frontend

# 复制配置文件
COPY ./nginx/conf/nginx.conf /etc/nginx/nginx.conf

# 复制前端文件（注意路径基于构建上下文）
COPY ./nginx/html/dist /home/interview/projects/frontend

# 暴露端口
EXPOSE 80