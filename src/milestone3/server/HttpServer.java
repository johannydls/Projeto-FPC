package milestone3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import milestone3.handler.HttpRequestHandler;

public class HttpServer {

	public static void main(String[] args) {
		String command = null;
		int port;
		ServerSocket server_socket;
		
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
					System.out.println("HttpServer running on port "
									+ server_socket.getLocalPort());
					
					while(true) {
						System.out.println("=============================================");
						Socket socket = server_socket.accept();
						System.out.println("New connection accepted "
								+ socket.getInetAddress() + ":" + socket.getPort());
						System.out.println("=============================================\n");
						try {
							
							HttpRequestHandler request = new HttpRequestHandler(socket);
							
							Thread thread = new Thread(request);
							
							thread.start();
							
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
			} catch (IOException e) {
				
			}
		
		

	}

}
