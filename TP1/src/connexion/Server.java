package connexion;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
	
	private static final String[][] USERS = {
	        {"admin", "password123"},
	        {"ikimath", "pass1"},
	        {"guest", "guest123"}
	    };

	 public static void main(String argv[]){
		    
		    try{
		      ServerSocket server= new ServerSocket(2121);
		      while(true){
		        Socket socket= server.accept();
		        

		        InputStream input=socket.getInputStream();	
		        //afficher l'adresse du socket connecté
		        System.out.println("New client "+ " "+ socket.getInetAddress()+"  connected");
		        OutputStream output= socket.getOutputStream();
		        
		        output.write("username:\r\n".getBytes());
                Scanner scanner = new Scanner(input);
                String username = scanner.nextLine();
		        output.write("2121 Service ready\r\n".getBytes());
		        
		     // Demander le mot de passe sans l'afficher à l'écran
                Console console = System.console();
                if (console == null) {
                    System.out.println("Console is not available.");
                    continue;
                }
		        
		        
		        output.write("password:\r\n".getBytes());
		        char[] passwordArray = console.readPassword(); 
                String password = new String(passwordArray);
                
                if (authenticate(username, password)) {
                    output.write("220 Service ready\r\n".getBytes());
                    
                    // Utiliser le nom d'utilisateur dans le message
                    output.write(("Name(localhost:" + username + ")\r\n").getBytes());
                  
                } else {
                    output.write("Authentication failed\r\n".getBytes());
                   
                }
                
		        socket.close();
		   	      

		       }

		    }

		    catch (IOException e) {
						e.getMessage();
				}
     }
	 
	 private static boolean authenticate(String username, String password) {
	        for (String[] user : USERS) {
	            if (user[0].equals(username) && user[1].equals(password)) {
	                return true; // Identifiants corrects
	            }
	        }
	        return false; // Identifiants incorrects
	    }
}
