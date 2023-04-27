FROM postgres

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get -y upgrade && \
    apt-get -y install \
            postgis \
            dos2unix && \
    apt-get -y autoremove && \
    apt-get autoclean

WORKDIR /docker-entrypoint-initdb.d

ADD ./init .

RUN dos2unix 01-createdb.sh
