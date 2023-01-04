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

- Modify this part at top of `$THRIFT_HOME/bin/thrift-server.sh` to actual working directory of ivy.settings
   ```aidl
   ivyDir=<working directory of ivy.settings>/ivy.settings
   ```

- To update Geohiker version, modify this part at top of `$THRIFT_HOME/bin/thrift-server.sh`
  - Default version: 1.2.53 (Verified version as of January 04, 2023)
  ```aidl
  geohikerVersion=1.2.53
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
