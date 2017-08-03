package milestone3.handler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

public class HttpRequestHandler implements Runnable {
	
	final static String CRLF = "\r\n";
	
	Socket socket;
	InputStream input;
	OutputStream output;
	BufferedReader bufferedReader;
	
	public HttpRequestHandler(Socket socket) throws IOException {
		
		this.socket = socket;
		this.input = socket.getInputStream();
		this.output = socket.getOutputStream();
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	@Override
	public void run() {
		try {
			processRequest();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	
	private void processRequest() throws IOException {
		
		while(true) {
			
			String headerLine = bufferedReader.readLine();
			System.out.println(headerLine);
			
			if (headerLine.equals(CRLF) || headerLine.equals("")) {
				break;
			}
			
			StringTokenizer s = new StringTokenizer(headerLine);
			
			String temp = s.nextToken();
			
			if (temp.equals("GET")) {
				
				String fileName = s.nextToken();
				fileName = "." + fileName;
				
				FileInputStream fileInput = null;
				boolean fileExists = true;
				
				try {
					if (fileName.equals(".")) {
						fileInput = new FileInputStream("files/index.html");
					} else {
						fileInput = new FileInputStream("files/" + fileName);
					}
					
				} catch (FileNotFoundException e) {
					fileExists = false;
				}
				
				String serverLine = "Server: Servidor Web FPC" + CRLF;
				String dateLine = null;
				String statusLine = null;
				String contentTypeLine = null;
				String entityBody = null;
				String contentLengthLine = "error";
				String connectionLine = null;
				
				if (fileExists) {
					
					statusLine = "HTTP/1.1 200 OK" + CRLF;
					dateLine = "Date: " + new Date().toString() + CRLF;
					contentTypeLine = "Content-Type: " + contentType(fileName) + CRLF;
					contentLengthLine = "Content-Length: " + (new Integer(fileInput.available())).toString() + CRLF;
					connectionLine = "Connection: Closed" + CRLF;
					
				} else {
					
					statusLine = "HTTP/1.1 404 Not Found" + CRLF;
					contentTypeLine = "Content-Type: text/html" + CRLF;
					
					entityBody = "<html>"
							+ "<head><title>404 Not Found</title></head>"
							+ "<body><h1>404 Not Found</h1>"
							+ "<br>Uso: http://localhost:port/"
							+ "fileName.html</body></html>";
				}
				
				//Envia a linha de status
				output.write(statusLine.getBytes());
				System.out.print(statusLine);
				
				//Envia a linha de data
				output.write(dateLine.getBytes());
				System.out.print(dateLine);
				
				//Envia a linha do servidor
				output.write(serverLine.getBytes());
				System.out.print(serverLine);
				
				//Envia o Content-Length
				output.write(contentLengthLine.getBytes());
				System.out.print(contentLengthLine);
				
				//Envia a linha do tipo de conteudo
				output.write(contentTypeLine.getBytes());
				System.out.print(contentTypeLine);
				
				//Envia a linha de conectividade
				output.write(connectionLine.getBytes());
				System.out.print(connectionLine);
				
				//Envia a linha em branco para indicar o fim do cabe�alho
				output.write(CRLF.getBytes());
				System.out.print(CRLF);
				
				//Envia o corpo do arquivo
				if (fileExists) {
					
					sendBytes(fileInput, output);
					fileInput.close();
				
				} else {
					
					output.write(entityBody.getBytes());
					System.out.println(entityBody);
				}
				
			}
		}
		
		try {
			output.close();
			bufferedReader.close();
			socket.close();
		} catch (Exception e) {
			
		}
		
	}

	/**
	 * Fun��o para retornar o tipo do arquivo
	 * @param fileName
	 * @return o nome do tipo do arquivo
	 */
	private String contentType(String fileName) {
		
		if (fileName.endsWith(".htm") || fileName.endsWith(".html") || fileName.endsWith(".txt")) {
			return "text/html";
		} else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
			return "image/jpeg";
		} else if (fileName.endsWith(".gif")) {
			return "image/gif";
		} else {
			return "application/octet-stream";
		}
	}

	
	private void sendBytes(FileInputStream fileInput, OutputStream output) throws IOException {
		
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		while ((bytes = fileInput.read(buffer)) != -1) {
			output.write(buffer, 0, bytes);
			
		}
		
	}
	
}
