java 	-Dspring.profiles.active=dev \
	-Dserver.port=8080 \
	-jar data-manager-1.0.0-SNAPSHOT.jar > /dev/null 2>&1 &
tail -f logs/datacore.log
