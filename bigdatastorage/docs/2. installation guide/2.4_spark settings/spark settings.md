# 2.4 Spark Settings

## 2.4.1 Thrift Server Deployment
Move packed `tar.gz` file to FileSystem of your server and unzip the complessed file which directory you have defined as THRIFT_HOME
  ```
  tar -xvzf /thrift-server/thrift-server-$THRIFT_SERVER_VERSION.tar.gz -C /usr/local
  ln -s /usr/local/thrift-server-$THRIFT_SERVER_VERSION /usr/local/thrift-server
  ```
## 2.4.2 Download Jars
Before you start Thrift Server, ensure that the jars described below must be deployed in `$SPARK_HOME/jars` directory

### 2.4.2.1 jts2geojson
In order to support spatial operations in spark, the following jars must be deployed.
```
# jts2geojson
wget -P $SPARK_HOME/jars/ "https://repo1.maven.org/maven2/org/wololo/jts2geojson/0.16.1/jts2geojson-0.16.1.jar"
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