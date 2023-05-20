#!/bin/bash
pwd
if [ "$RUN_COMPONENT" == "REGISTRY" ]; then
    echo "Registry start"
    cd ./target
    java -cp MyRMI-1.0-SNAPSHOT-jar-with-dependencies.jar myrmi.RegistryServer.registryServer
elif [ "$RUN_COMPONENT" == "SERVER" ]; then
    echo "Spark Service start"
    cd ./target
    java -cp MyRMI-1.0-SNAPSHOT-jar-with-dependencies.jar myrmi.SparkServer.SparkServer
elif [ "$RUN_COMPONENT" == "CLIENT" ]; then
    cd ./target
    java -cp MyRMI-1.0-SNAPSHOT-jar-with-dependencies.jar myrmi.RemoteClient
else
    echo "missing"
fi
