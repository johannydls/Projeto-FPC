#!/bin/bash
command=$1
port_number=$2
  

java -jar http-server-fpc.jar  ${command} ${port_number}