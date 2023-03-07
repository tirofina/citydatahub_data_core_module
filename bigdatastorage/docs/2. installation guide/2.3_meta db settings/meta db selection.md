# 2.3 Metastore DataBase Settings
- Spark Thrift uses Hive's Metastore DataBase and needs relative settings.

## 2.3.1 DataBase Selection
- By default, Apache Spark provides Derby as Metastore DB
- If you want to access from other process, please install one of the RDBMS such as MySQL and Postgres
- DB used by Ambari or Cloudera Manager on CDH or HDP can be utilized
!Notice : Thrift Server can only have one metastore, and guide covers the postgres metastore by default.

## 2.3.2 Using RDBMS as a Metastore DataBase
### 2.3.2.1 MetaStore DataBase Settings
#### 2.3.2.1.1 Mysql
- Download JDBC Driver
  - Download the jdbc driver that is compatible with the Java version.
  - Example : ```wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-8.0.22.tar.gz```
- Create databases and users for MetaDB
  - '%' is used to create an account with external access
  ```
  create database hive;
  create user '<user_name>'@'%' identified by '<hive_password>';
  grant all privileges on hive.* to '<user_name>'@'%';
  ```

#### 2.3.2.1.2 Postgres
- Download JDBC Driver
  - Download the JDBC Driver corresponding to the Postgres version you installed from the [Postgres JDBC Driver Download Page](https://jdbc.postgresql.org/download.html)
  - Example : ``` wget https://jdbc.postgresql.org/download/postgresql-42.2.19.jar ```
> if error occured by certificate, we can use `--no-check-certificate` option
- Create databases and users to use as MetaDB
  ```
  # Create Hive User
  create user <user_name> with password '<hive_password>'; # User name and Password can be modified
  # Create a database with the newly created user as the owner
  create database <database name> owner=hive 
  ```

### 2.3.2.2 JDBC Driver Deployment
- Using Symbolic Link to place jar files in the directory path that requires the downloaded MetaDB JDBC driver.
  ```
  $HIVE_HOME/lib
  $HIVE_HOME/jdbc
  $SPARK_HOME/jars
  ```
### 2.3.2.3 hive-site.xml configuration
- Copy and rename hive-default.xml.template under /usr/local/hive/conf
  ```zsh
  $cd /usr/local/hive/conf
  $cp hive-default.xml.template ./hive-site.xml
  ```
  
- Add MetaDB information to use as Metstore in hive-site.xml
  - User name and password refer to the newly created user information and password as shown in the **2.3 MetaStore DataBase Settings**
  > ex) hive/hive123!

- In the sample script below, the port number of ConnectionURL is 5500, but it needs to be modified depending on your installation.

```
  <property>
    <name>javax.jdo.option.ConnectionUserName</name>
    <value>hive</value>
    <description>Username to use against metastore database</description>
  </property>
  
  <property>
    <name>javax.jdo.option.ConnectionPassword</name>
    <value>hive123!</value>
    <description>password to use against metastore database</description>
  </property>
  
  <property>
    <name>javax.jdo.option.ConnectionDriverName</name>
    <value>org.postgresql.Driver</value>
    <description> Class name of JDBC Driver Metastore DB</description>
  </property>
  
  <property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:postgresql://localhost:5500/hive</value>
    <description>
      JDBC connect string for a JDBC metastore.
      To use SSL to encrypt/authenticate the connection, provide database-specific SSL flag in the connection URL.
      For example, jdbc:postgresql://myhost/db?ssl=true for postgres database.
    </description>
  </property>
```

### 2.3.2.4Hive metestore Initialization
- Initialize metastore using schematool in `$HIVE_HOME/bin`
  ```
  # Postgres
  ./bin/schematool -initSchema -dbType postgres 
  
  # Mysql
  ./bin/schematool -initSchema -dbType mysql
  ```
- Check MetaDB for a list of tables in the database to see if they are properly reset
  - Since it was an empty database when it was first created, if the non-empty table list means it is reset properly
