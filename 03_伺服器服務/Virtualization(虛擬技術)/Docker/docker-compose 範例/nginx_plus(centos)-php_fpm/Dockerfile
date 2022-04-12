FROM centos/systemd
COPY . .

RUN yum install wget -y
RUN yum install vim -y
RUN yum install GeoIP  GeoIP-data -y

COPY ./setup_info/nginx-repo.crt /etc/ssl/nginx/nginx-repo.crt
COPY ./setup_info/nginx-repo.key /etc/ssl/nginx/nginx-repo.key

RUN touch /etc/ssl/nginx/nginx-repo.crt
RUN touch /etc/ssl/nginx/nginx-repo.key

RUN wget -P /etc/yum.repos.d git@cs.nginx.com/static/files/nginx-plus-7.4.repo

RUN yum install nginx-plus -y
RUN yum install nginx-plus-module-geoip -y
RUN yum install nginx-plus-module-image-filter -y
RUN yum install nginx-plus-module-subs-filter -y

RUN cp -r /etc/nginx /etc/nginx_bak
RUN rm -rf /etc/nginx/*
CMD ["nginx", "-g", "daemon off;"]
