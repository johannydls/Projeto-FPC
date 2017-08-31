#!/bin/bash
case "$1" in
		start)
            nohup java -jar http-server-fpc.jar $1 $2 > log.txt 2>&1 &
			echo $! > .server.pid
			echo "Servidor rodando em http://localhost:$2"
			echo "Para finalizar, execute o comando './run.sh stop'"
            ;;
         
        stop)
            if [ -e ".server.pid" ]; then
				kill -9 `cat .server.pid`
				rm .server.pid
				echo "Servidor finalizado"
			fi 
            ;;
        *)
            echo $"Uso: '$0 start <port>' para iniciar ou $0 stop' para finalizar o servidor."
            exit 1
 
esac