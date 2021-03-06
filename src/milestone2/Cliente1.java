package milestone2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente1 {
	
	public static void main(String[] args) throws IOException {
		
		Socket socket = new Socket("localhost", 3030);
		
		System.out.println("Conexão estabelecida!\n");
		
		InputStream input = socket.getInputStream();
		OutputStream output = socket.getOutputStream();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		PrintStream out = new PrintStream(output);
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Requisição: ");
		String mensagem = scanner.nextLine();

		out.println(mensagem);
		
		mensagem = in.readLine();
		
		System.out.println("Resposta do servidor:\n");
		
		String line;
		
		while((line = in.readLine()) != null) {
			System.out.println(line);
		}
		
		System.out.println("\nDesconectado.");
		
		scanner.close();
		in.close();
		out.close();
		socket.close();
		
		
	}

}