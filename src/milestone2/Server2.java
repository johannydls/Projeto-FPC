package milestone2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

public class Server2 {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		
		ServerSocket server = new ServerSocket(3030);
		
		System.out.println("Servidor rodando em localhost:3030");
		
		Socket client = server.accept();
		
		System.out.println("Cliente conectado");
		
		InputStream in = client.getInputStream();
		OutputStream out = client.getOutputStream();
		
		String data = new Scanner(in, "UTF-8").useDelimiter("\\r\\n\\r\\n").next();
		
		Matcher get = Pattern.compile("^GET").matcher(data);
		
		if (get.find()) {
			
			Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
			
			match.find();
			
			byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
					+ "Connection: Upgrade\r\n"
					+ "Upgrade: websocket\r\n"
					+ "Sec-WebSocket-Accept: "
					+ DatatypeConverter
					.printBase64Binary(
							MessageDigest
							.getInstance("SHA-1")
							.digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
									.getBytes("UTF-8")))
					+ "\r\n\r\n")
					.getBytes("UTF-8");
			
			out.write(response, 0, response.length);
		}
	}
}
