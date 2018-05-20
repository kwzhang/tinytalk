#!/usr/bin/bash

rm -rf server
mkdir server
cd server
unzip ../jaxrs-server-generated.zip
datetime=`date +%Y%m%d%H%M%S`
cp -r ../../server/src/main/java/io/swagger/api/impl/ ../../server/src/main/java/io/swagger/api/impl_backup_${datetime}
rm ../../server/src/gen/java/io/swagger/api/* ../../server/src/gen/java/io/swagger/model/* ../../server/src/main/java/io/swagger/api/factories/*  ../../server/src/main/java/io/swagger/api/impl/* ../../server/src/main/java/io/swagger/api/Bootstrap.java
cp src/gen/java/io/swagger/api/* ../../server/src/gen/java/io/swagger/api/
cp src/gen/java/io/swagger/model/* ../../server/src/gen/java/io/swagger/model/
cp src/main/java/io/swagger/api/factories/* ../../server/src/main/java/io/swagger/api/factories/
cp src/main/java/io/swagger/api/impl/* ../../server/src/main/java/io/swagger/api/impl/
cp src/main/java/io/swagger/api/Bootstrap.java ../../server/src/main/java/io/swagger/api/
