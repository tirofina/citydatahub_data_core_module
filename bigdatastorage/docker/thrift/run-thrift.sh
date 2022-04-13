#!/bin/sh

echo "Initialize Schema!!"

cp /usr/local/spark/conf/hive-site.xml /usr/local/hive/conf/
cp /usr/local/spark/conf/core-site.xml /usr/local/hadoop/etc/hadoop/
cp /usr/local/spark/conf/hdfs-site.xml /usr/local/hadoop/etc/hadoop/
cp /usr/local/spark/conf/yarn-site.xml /usr/local/hadoop/etc/hadoop/
cp /usr/local/spark/jars/postgresql-42.2.19.jar /usr/local/hive/lib/
cp /usr/local/spark/jars/postgresql-42.2.19.jar /usr/local/hive/jdbc/

/usr/local/hive/bin/schematool -initSchema -dbType postgres 

wait

/usr/local/thrift-server/bin/thrift-server.sh start