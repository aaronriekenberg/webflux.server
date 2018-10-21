#!/bin/sh -x

pkill java

sleep 2

if [ ! -d logs ]; then
  mkdir logs
fi

nohup java -Dspring.profiles.active=$(hostname) -jar ./webflux.server-1.0-SNAPSHOT.jar 2>&1 | svlogd logs &
