#!/bin/bash

if [-d temp/ ];
then
	rm -rf temp/ http-server-fpc.jar
fi

mkdir temp/

javac -d temp/ -sourcepath src src/milestone3/server/HttpServer.java

cd temp/

jar cvfe ../http-server-fpc.jar milestone3.server.HttpServer *

cd ../

rm -rf temp/

if [ -e http-server-fpc.jar ];
then
	echo "Construct worked!"
else
	echo "Error!"
fi