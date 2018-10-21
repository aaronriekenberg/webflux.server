#!/bin/sh

pgrep java > /dev/null 2>&1
if [ $? -eq 1 ]; then
  cd ~/webflux.server
  ./restart.sh > /dev/null 2>&1
fi
