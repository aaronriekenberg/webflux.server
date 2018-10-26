#!/bin/sh -x

pkill java

sleep 2

if [ ! -d logs ]; then
  mkdir logs
fi

export JAVA_HOME=/home/aaron/jdk-11.0.1

JAVA_OPTS="-Dspring.profiles.active=$(hostname)"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=7999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=192.168.1.1"
JAVA_OPTS="$JAVA_OPTS -Xmx1g"

nohup $JAVA_HOME/bin/java $JAVA_OPTS -jar ./webflux.server-1.0-SNAPSHOT.jar 2>&1 | svlogd logs &
