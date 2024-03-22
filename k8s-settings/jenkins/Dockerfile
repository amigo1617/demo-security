#custom kaniko warmer
FROM busybox:musl AS busybox
FROM gcr.io/kaniko-project/warmer:v1.19.2
COPY --from=busybox /bin /bin
ENV PATH $PATH:/bin


#Docker-was-test
FROM tomcat:9.0.83-jdk11-temurin-focal
USER root
RUN apt-get install curl
WORKDIR /usr/local/tomcat/conf
COPY ./server.xml .
WORKDIR /usr/local/tomcat/webapps
ARG WAR_PATH
COPY ./${WAR_PATH} .


#Docker-was
ARG BASE_IMAGE
FROM ${BASE_IMAGE}
WORKDIR /opt/wildfly-26/standalone/deployments
ARG COPY_TARGET
COPY ./${COPY_TARGET} .
USER 0
RUN chown -R 1000:1000 /opt/wildfly-26/standalone/deployments
USER 1000
WORKDIR /ksbiz
ARG COPY_KSBIZ
COPY ./${COPY_KSBIZ} .


#Docker-web
ARG BASE_IMAGE
FROM ${BASE_IMAGE}
COPY ./ucm-odr-pfom/src/main/resources/static /app/web/ucm-odr-pfom/static
COPY ./ucm-vuc-oper/src/main/resources/static /app/web/ucm-vuc-oper/static
RUN chown 33:33 /app -R
