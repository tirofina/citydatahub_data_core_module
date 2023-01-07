FROM postgres

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update
RUN apt-get -y install postgis


ADD ./init /docker-entrypoint-initdb.d/

## dos line end -> linux line end
RUN sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/01-createdb.sh

EXPOSE 5432