package milestone2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 {

	public static void main(String[] args) throws IOException {
		
		//Servidor
		@SuppressWarnings("resource")
		ServerSocket servidor = new ServerSocket(3030);
		
		System.out.println("Servidor ouvindo localhost:3030");
		
		System.out.println("Aguardando conexão");
		
		while (true) {
			//Cliente
			Socket cliente = servidor.accept();
			
			System.out.println(cliente.getInetAddress().getHostAddress());
			
			InputStream input = cliente.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			String request = br.readLine();
			String[] requestParam = request.split(" ");
			
			
			String path = requestParam[1];
			
			PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);

			File file = new File("files/" + path);
			
			System.out.println(requestParam[0] + " " + file.getPath() + " HTTP/1.1");
			
			String response = "";
			
			if (!file.exists()) {
				response = "HTTP/1.1 404 Not Found";
				//out.println(response);
			}
			
			else {
				FileReader fr = new FileReader(file);
				BufferedReader bfr = new BufferedReader(fr);
				String line;

				response += ("Method: " + requestParam[0]) + "\n";
				response += "HTTP/1.1 200 OK\r\n\r\n";
				response += "BODY:\n";
				
				while ((line = bfr.readLine()) != null) {
					out.write(line);
					out.println(line);
					response += line + "\n";
					out.flush();
				}
				bfr.close();
			
			}
			System.out.println(file.getAbsolutePath());
			out.println(response);
			br.close();
			out.close();
		}
		
	}
}
