#!/bin/bash
dir=$(pwd)
port=4564
dbPath="./db.xml"
logPath="./server.log"
port=$port ./gradlew build && (osascript -e "tell application \"Terminal\" to do script \"cd '$dir' && port=$port java -jar apps/client/build/libs/lab6Client.jar\"" ; logPath=$logPath dbPath=$dbPath port=$port java -jar apps/server/build/libs/lab6Server.jar)