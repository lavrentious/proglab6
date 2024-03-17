#!/bin/bash
dir=$(pwd)
./gradlew build && (osascript -e "tell application \"Terminal\" to do script \"cd '$dir' && java -jar apps/client/build/libs/lab6Client.jar\"" ; java -jar apps/server/build/libs/lab6Server.jar)