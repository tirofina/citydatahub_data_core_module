# Appendix. Run on Docker

The docker directory composed of 3 docker files; `hadoop, postgres, thrift`. The docker-compose file builds and runs the docker files. The command to run docker-compose file is as below:

  ``` 
(in docker directory) 
> docker network create -d bridge local-docker-bridge
> docker-compose up -d --build
  ```

## 1. Docker Port binding
If you want to disable IPv4-mapped IPV6, Set your ipv6 settings as below:

```
sudo vi /etc/sysctl.conf

net.ipv6.conf.all.disable_ipv6 = 1

net.ipv6.conf.default.disable_ipv6 = 1

net.ipv6.conf.lo.disable_ipv6 = 1

sudo sysctl -p

sudo reboot
```

## 2. Working With External Hadoop Cluster
Because thrift server is spark application, the thrift server directly communicate with hadoop slaves(data node, node manager) by socket. In order to support that, the network of thrift container must be host.

If you change a network of thrift container, you need to change some settings, as below:

### 2.1 Replace hadoop configuration files
In `docker/conf` directory, there are configuration files for each services. To allow thrift access to external hadoop, all you have to do is replacing configuration files in `docker/conf/hadoop` directory with external hadoop's

### 2.2 Metastore DB setting
Standalone mode of Bigdata Storage works with existing postgres metastore DataBase in docker with bridge network. In order to communicate with external hadoop thrift container needs to change of its network mode from bridge network to host network. Thus the alternative access of postgressql container is necessary. Docker-compose file has binded port to access postgresql's 5432 port. The simple change of `docker/conf/hive/hive-site.xml` to let thrift use existing postgressql as metastore DB again.

- before
```
<property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:postgresql://7x_postgres:5432/hive</value>
</property>
```
- after
```
<property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:postgresql://<DOCKER_HOST_IP>:54321/hive</value>
</property>
```

After Configurations, restart thrift server to apply newly added settings.
