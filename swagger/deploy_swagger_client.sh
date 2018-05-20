#!/usr/bin/bash

rm -rf client
mkdir client
cd client
unzip ../java-client-generated.zip

datetime=`date +%Y%m%d%H%M%S`
cp -r ../../server/src/test/java/io/swagger/client/api ../../server/src/test/java/io/swagger/client/api_backup_${datetime} 
rm -rf ../../server/src/test/java/io/swagger/client/api/* ../../server/src/main/java/io/swagger/client/api/* ../../server/src/main/java/io/swagger/client/model/*
cp -r src/test/java/io/swagger/client/api/* ../../server/src/test/java/io/swagger/client/api
cp -r src/main/java/io/swagger/client/api/* ../../server/src/main/java/io/swagger/client/api
cp -r src/main/java/io/swagger/client/model/* ../../server/src/main/java/io/swagger/client/model
