# 2.4 Spark Settings

## 2.4.1 Thrift Server Deployment
Move packed `tar.gz` file to FileSystem of your server and unzip the complessed file which directory you have defined as THRIFT_HOME
  ```
  tar -xvzf /thrift-server/thrift-server-$THRIFT_SERVER_VERSION.tar.gz -C /usr/local
  ln -s /usr/local/thrift-server-$THRIFT_SERVER_VERSION /usr/local/thrift-server
  cp /usr/local/thrift-server/libs/geohiker-*.jar /usr/local/spark/jars/
  ```
## 2.4.2 Download Jars
Before you start Thrift Server, ensure that the jars described below must be deployed in `$SPARK_HOME/jars` directory
### 2.4.2.1 HBase
- Spark does not have HBase-related libraries by default, so the necessary jar files must be downloaded.
  - It can be downloaded in package form, but it is recommended that only the necessary jar files are downloaded using wget because dependency conflicts can cause problems that may not work properly.
  ``` 
  wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/apache/hbase/hbase-common/1.1.1/hbase-common-1.1.1.jar"
  wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/apache/hbase/hbase-client/1.1.1/hbase-client-1.1.1.jar"
  wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/apache/hbase/hbase-server/1.1.1/hbase-server-1.1.1.jar"
  wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/apache/hbase/hbase-protocol/1.1.1/hbase-protocol-1.1.1.jar"
  wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/apache/hive/hive-hbase-handler/2.3.7/hive-hbase-handler-2.3.7.jar" 
  ```

### 2.4.2.2 JTS & Sedona & jts2geojson
In order to support spatial operations in spark, the following jars must be deployed.
```
# JTS
wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/locationtech/jts/jts-core/1.18.1/jts-core-1.18.1.jar"

# Sedona
wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/apache/sedona/sedona-core-3.0_2.12/1.0.0-incubating/sedona-core-3.0_2.12-1.0.0-incubating.jar"

wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/apache/sedona/sedona-sql-3.0_2.12/1.0.0-incubating/sedona-sql-3.0_2.12-1.0.0-incubating.jar"

# jts2geojson
wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/wololo/jts2geojson/0.16.1/jts2geojson-0.16.1.jar"
```

### 2.4.2.3 Copy Geohiker Jar Files into Spark Jars Directory

In order to support spatial operations in spark, the following jars must be deployed in spark.

```
# Geohiker
cp $THRIFT_HOME/libs/geohiker-*.jar /usr/local/spark/jars/ 
```

## 2.4.3 Work with Hadoop & Hive

In order to store data into HDFS and execute query in yarn executors, the xml files below must be located in  `$SPARK_HOME/conf` directory

```
core-site.xml
hdfs-site.xml
yarn-site.xml
```

To use hive metastore DB set above, `hive-site.xml` written previously also must be located in `$SPARK_HOME/conf` directory.

## 2.4.4 prepare running thrift server
copy run-thrift.sh to `/` in your thrift server envireonment, execute next commend
```
yum -y update
yum install -y epel-release
yum -y install supervisor

chmod a+x run-thrift.sh
```