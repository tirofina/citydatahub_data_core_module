#!/bin/bash
java 	-Dspring.profiles.active=dev \
	-Dserver.port=8443 \
	-Dserver.http.port=8080 \
	-Djava.net.preferIPv4Stack=true \
	-jar ingest-interface-1.0.0-SNAPSHOT.jar > /dev/null 2>&1 &
tail -f logs/datacore.log
