# 2.5 Run Thrift server
> if you using dockerfile or docker-compose.yml, run thrift docker container and follow next step.
- Create a defendency management file named ivy.settings in the desired location of your environment running Thrift Server
  ```
  $vi ivy.settings
  ```
  ```
  <ivysettings>
  <settings defaultBranch="${ivy.deliver.branch}" defaultResolver="default-chain" />
  <property name ="osgeo"  value="https://repo.osgeo.org/repository/release/"/>
  <property name ="jboss" value="https://repository.jboss.org/nexus/congent/repositories/thirdparty-releases/"/>
  <property name="nexus-dtonic" value="https://nexus.dtonic.io/repository/maven-public"/>
  <resolvers>
  <ibiblio name="nexus" m2compatible="true" root="${nexus=dtonioc}"/>
  </resolvers>
  <resolvers>
  <ibiblio name="central" m2compatible="true"/>
  <ibiblio name="nexus" m2compatible="true" root="${nexus-dtonic}"/>
  <ibiblio name="jboss" m2compatible="true" root="${jboss}"/>
  <ibiblio name="os-geo" m2compatible="true" root="${osgeo}"/>
  <chain name="default-chain">
  <resolver ref="nexus" />
  <!--resolver ref="central" /-->
  <!--resolver ref="jboss" /-->
  <!--resolver ref="os-geo" /-->
  </chain>
  </resolvers>
  </ivysettings>
  ```
- Modify this part at top of `$THRIFT_HOME/bin/thrift-server.sh` 
  ```
  #!/bin/bash

  sparkMaster="local"
  hivePort="10000"
  hiveHost="0.0.0.0"
  thriftMode="$1"
  ```
- as follows
  ```
  #!/bin/bash

  export SPARK_DIST_CLASSPATH=$SPARK_DIST_CLASSPATH:~/.ivy2/jars/*

  sparkMaster="local"
  hivePort="10000"
  hiveHost="0.0.0.0"
  thriftMode="$1"
  ```

- Modify this part at bottom of `$THRIFT_HOME/bin/thrift-server.sh` 
  ```
  if [ "start" == "${thriftMode}" ]; then
  $GEOHIKER_HOME/sbin/datacore-start-thriftserver.sh \
  --master $sparkMaster \
  --jars $GEOHIKER_HOME/libs/geohiker-core-1.0.jar,$GEOHIKER_HOME/libs/geohiker-index-1.0.jar,$GEOHIKER_HOME/libs/geohiker-spark-1.0.jar,$GEOHIKER_HOME/libs/geohiker-datastore-1.0.jar \
  $GEOHIKER_HOME/libs/Thrift-Server-1.0.jar \
  --hiveconf hive.server2.thrift.port=$hivePort \
  --hiveconf hive.server2.thrift.bind.host=$hiveHost
  ```
- as follows
  ```
  if [ "start" == "${thriftMode}" ]; then
  $GEOHIKER_HOME/sbin/datacore-start-thriftserver.sh \
  --master $sparkMaster \
  --packages io.dtonic.geohiker:geohiker-spark:1.2.43,io.dtonic.geohiker:geohiker-datastore:1.2.43 \
  --conf spark.jars.ivySettings=<working directory of ivy.settings>/ivy.settings \
  --conf spark.sql.extensions=io.dtonic.geohiker.spark.GeohikerSparkExtensions,io.delta.sql.DeltaSparkSessionExtension \
  --conf spark.sql.catalog.spark_catalog=org.apache.spark.sql.delta.catalog.DeltaCatalog \
  $GEOHIKER_HOME/libs/Thrift-Server-1.0.jar
  ```  
- Enter the following command in the CLI environment to run the thrift server:
  ```
  $THRIFT_HOME/bin/thrift-server.sh start
  ```
> If you modify `--hiveconf` in `thrift-server.sh`, you can using this option : -m <Spark_Master> -p <Thrift_Port> -u <Thrift_Url>
  
- If you want to stop thrift server, enter the following command:
   ```
   $THRIFT_HOME/bin/thrift-server.sh stop
   ```

## 2.5.1 Run-Check using Beeline

run `$SPARK-HOME/bin/beeline` and type the command below:

```beeline
beeline> !connect jdbc:hive2://localhost:10000
Connecting to jdbc:hive2://localhost:10000
Enter username for jdbc:hive2://localhost:10000:
Enter password for jdbc:hive2://localhost:10000:
```
