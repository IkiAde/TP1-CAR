package connexion;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	

	 public static void main(String argv[]){
		    
		    try{
		      ServerSocket server= new ServerSocket(2121);
		      while(true){
		        Socket socket= server.accept();
		        InputStream input=socket.getInputStream();	
		        //afficher l'adresse du socket connecté
		        System.out.println("New client "+ " "+ socket.getInetAddress()+"  connected");
		        OutputStream output= socket.getOutputStream();
		        output.write("2121\r\n".getBytes());
		        
		        Scanner scanner= new Scanner(input);
		        String str=scanner.nextLine();
		        socket.close();
		   	      

		       }

		    }

		    catch (IOException e) {
						e.getMessage();
				}
     }
}
