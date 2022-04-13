#!/bin/bash

sparkMaster="local"
hivePort="10000"
hiveHost="0.0.0.0"
thriftMode="$1"

help()
{
    echo "Start Thrift Server : $0 [start|stop] -master [spark-master] -port [thrift-port] -url [thrift-url]"
    echo " -m set spark master"
    echo " -p set port of spart thrift server"
    echo " -u set url of spark thrift server"
}


# 옵션이름 뒤에 :이 붙은 것은 값을 필요로 함을 의미합니다.
while getopts opt
do
    case $opt in

        m)
            sparkMaster=$OPTARG
            ;;
        p)
            hivePort=$OPTARG
            ;;
        u)
            hiveHost=$OPTARG
            ;;
        # 상단의  옵션이 아니면 도움말을 출력하고 종료합니다.
        *)
            echo "Invalid option ${opt}"
            help
            exit 0
            ;;
    esac
done

if [ -z "${GEOHIKER_HOME}" ]; then
  export GEOHIKER_HOME="$(cd "`dirname "$0"`"/..; pwd)"
fi

echo "${thriftMode} Thrift Server"

shopt -s nocasematch
if [ "start" == "${thriftMode}" ]; then
  $GEOHIKER_HOME/sbin/datacore-start-thriftserver.sh \
  --master $sparkMaster \
  --jars $GEOHIKER_HOME/libs/geohiker-core-1.0.jar,$GEOHIKER_HOME/libs/geohiker-index-1.0.jar,$GEOHIKER_HOME/libs/geohiker-spark-1.0.jar,$GEOHIKER_HOME/libs/geohiker-datastore-1.0.jar \
  $GEOHIKER_HOME/libs/Thrift-Server-1.0.jar \
  --hiveconf hive.server2.thrift.port=$hivePort \
  --hiveconf hive.server2.thrift.bind.host=$hiveHost
elif [ "stop" == "${thriftMode}" ]; then
  $GEOHIKER_HOME/sbin/datacore-stop-thriftserver.sh
else
  echo "Invalid mode ${thriftMode}"
  help
fi