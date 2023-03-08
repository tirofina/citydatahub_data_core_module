# 2.1 PreInstallation
- If you clone git project, you can project build
  ```
  chmod +x gradlew
  ./gradlew build
  ./gradlew makeTar
  ``` 
# Create Distribution File
- After the job is done, you can check `tar.gz` file in:
```
build/dist
docker/thrift/thrift-server
```
#README
- you can choose 3 way to run thrift server
  1. using `docker/docker-compose.yml`
  2. using `docker/thrift/Dockerfile`
  3. manually install (recommend adapt on your setting)
  
this document guide 3. manually install. but If you can use Docker, recommend 1. using `docker/docker-compose.yml` or 2. using `docker/thrift/Dockerfile`
All the steps in the installation guide except thrift server.md can be found in docker-compose.yml or Dockerfile.

# using 1. using `docker/docker-compose.yml` : 
- install preconfigured thrift server and hadoop(container name "hadoop"), metastore is postgres(container name "7x_postgres").
- follow up document 'run on docker.md' and 'run-thrift server.md'

# using 2. using `docker/thrift/Dockerfile` : 
- install preconfigured thrift server

- setup container with set image tag and container name
  ```
  docker build docker/thrift/. -t <image tag>
  docker run -d -p 12378:10000 -v docker/conf/hive/hive-site.xml:/usr/local/spark/conf/hive-site.xml --name <container name> <image tag>
  ```
- installed thrift server only.
- follow up last document 'run-thrift server.md'

# using 3. manually install : 
- follow up next step and next document

## 2.1.1 OS
- Current version of Bigdata Storage is capable to CentOS 7. If the version above the version, capability check is required.

## 2.1.2 Install Java
- Install Java 8 version with consideration to Spark compatibility
  ```
  # centos7
  yum install -y java-1.8.0-openjdk
  yum install -y net-tools
  yum install -y wget
  ```

## 2.1.3 Install Hadoop, Hive, Spark
 - Version to download (Verified version as of January 04, 2023)
     - Spark: 3.0.1
     - HADOOP_PROFILE: 2.7(Required for Spark Download)
     - Hive: 2.3.7
     - Hadoop: 3.0.0

- Download and unzip Hive, Hadoop, Spark to desired path 
  - If a service is already installed, skip it
  - If CDH or HDP is to be used, downloading Hive is required.
```
(spark)  curl -s https://archive.apache.org/dist/spark/spark-3.0.1/spark-3.0.1-bin-hadoop2.7.tgz | tar -xvz -C /usr/local/
(hive)   curl -s https://archive.apache.org/dist/hive/hive-2.3.7/apache-hive-2.3.7-bin.tar.gz | tar -xvz -C /usr/local/
(hadoop) curl -s https://archive.apache.org/dist/hadoop/common/hadoop-3.0.0/hadoop-3.0.0.tar.gz | tar -xvz -C /usr/local/
```

### 2.2.2.1 Create a path with no version information using symbolic links
```
(spark)  cd /usr/local && ln -s spark-3.0.1-bin-hadoop2.7 spark
(hive)   cd /usr/local && ln -s apache-hive-2.3.7-bin hive
(hadoop) cd /usr/local && ln -s hadoop-3.0.0 hadoop
```