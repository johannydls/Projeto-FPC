#!/bin/bash
command=$1
port_number=$2

if [ "$command" = "start" ]; then
java -jar http-server-fpc.jar   ${command} ${port_number} 
elif  [ "$X" = "$Y" ]; then
pkill -f 'java -jar'
fi

