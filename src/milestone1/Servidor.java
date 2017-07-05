package milestone1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	
	private ServerSocket serverSocket;
	
	public Servidor(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		System.out.printf("Servidor rodando em localhost:%d\n", port);	
		System.out.println("\n*****************************************\n");
	}
	
	public void encerraServidor() throws IOException {
		serverSocket.close();
		System.out.println("Servidor desconectado.");
	}
	
	private Socket esperaConexao() throws IOException {
		Socket socket = serverSocket.accept();
		String cliente = socket.getRemoteSocketAddress().toString();
		System.out.printf("Nova conexão: Cliente [%s]\n", cliente);
		return socket;
	}
	
	private void trataRequisicao(Socket socket) throws IOException {
		
		InputStream input = socket.getInputStream();
		OutputStream output = socket.getOutputStream();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		PrintStream out = new PrintStream(output);
		
		String mensagem = in.readLine();
		
		System.out.println("Requisição: " + mensagem);
		
		String[] request = mensagem.split(" ");

		//Retorna a resposta para o cliente de acordo com o protocolo usado
		if (request[0].equalsIgnoreCase("INC")) {
			out.println(String.valueOf(protocoloINC(Integer.valueOf(request[1]))));
		} else if (request[0].equalsIgnoreCase("DEC")) {
			out.println(String.valueOf(protocoloDEC(Integer.valueOf(request[1]))));
		} else {
			out.println("USO CORRETO: DEC 10, INC 10");
		}
		
		System.out.println("Resposta enviada para o cliente");
		System.out.println("\n*****************************************\n");
		
		in.close();
		out.close();
		socket.close();
		
	}
	
	private int protocoloINC(int valor) {
		return ++valor;
	}
	
	private int protocoloDEC(int valor) {
		return --valor;
	}

	public static void main(String[] args) throws IOException {
		
		Servidor servidor = new Servidor(2525);

		Socket socket = servidor.esperaConexao();
		
		servidor.trataRequisicao(socket);
						
		servidor.encerraServidor();
		
	}
}
