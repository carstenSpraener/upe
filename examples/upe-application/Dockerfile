FROM eclipse-temurin:17.0.6_10-jdk-focal

ENV TZ=Europe/Berlin
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /opt/upe-application

COPY ./build/install/upe-application /opt/upe-application
CMD ["bin/upe-application"]
