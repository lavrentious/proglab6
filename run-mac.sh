#!/bin/bash
dir=$(pwd)
port=5666
port=$port ./gradlew build && (osascript -e "tell application \"Terminal\" to do script \"cd '$dir' && port=$port java -jar apps/client/build/libs/lab6Client.jar\"" ; port=$port java -jar apps/server/build/libs/lab6Server.jar)