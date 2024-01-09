#kaniko warmer
FROM busybox:musl AS busybox
FROM gcr.io/kaniko-project/warmer:v1.19.2
COPY --from=busybox /bin /bin
ENV PATH $PATH:/bin


#FROM tomcat:9.0.83-jdk11-temurin-focal
#USER root
#RUN apt-get install curl
#WORKDIR /usr/local/tomcat/conf
#COPY ./server.xml .
#WORKDIR /usr/local/tomcat/webapps
#ARG WAR_PATH
#COPY ./${WAR_PATH} .
