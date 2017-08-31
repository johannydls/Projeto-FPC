package milestone3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import milestone3.handler.HttpRequestHandler;

public class HttpServer {

	public static void main(String[] args) {
		
		String command = null;
		int port;
		
		ServerSocket server_socket;
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		try {
			
			command = String.valueOf(args[0]);
			port = Integer.parseInt(args[1]);
			
		} catch (Exception e) {
			port = 5555;
			command = "start";
		}	
		
			
		try {
			
			if(command.equals("start")){
				
				server_socket = new ServerSocket(port);
				
				System.out.println("\n===== Servidor FPC rodando na porta "
								+ server_socket.getLocalPort()
								+ " =====\n\n");
				
				while(true) {
					
					Socket socket = server_socket.accept();
					
					System.out.println("=================================================\n"
							+ "Nova conexão aceita "
							+ socket.getInetAddress() + ":" + socket.getPort()
							+ "\n=================================================\n");
					
					try {
						
						HttpRequestHandler request = new HttpRequestHandler(socket);
						
						threadPool.execute(request);					
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
