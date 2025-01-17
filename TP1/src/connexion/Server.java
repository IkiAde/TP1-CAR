package connexion;

//import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
	
	private static final String[][] USERS = {
	        {"USER admin", "PASS password123"},
	        {"USER ikimath", "PASS pass1"},
	        {"USER guest", "PASS guest123"}
	    };

	 public static void main(String argv[]){
		    
		    try{
		      ServerSocket server= new ServerSocket(2121);
		      while(true){
		        Socket socket= server.accept();
		        
		        
		        InputStream input=socket.getInputStream();	
		        //afficher l'adresse du socket connecté
		        
		        OutputStream output= socket.getOutputStream();
		        
                Scanner scanner = new Scanner(input);
                //entrer le username
                
                output.write("220 Bienvenue\r\n".getBytes());
                String username = scanner.nextLine();
		        System.out.println(username);
		        output.write("331 User name okay, need password\r\n".getBytes());
		        
		     
                String password = scanner.nextLine();
                System.out.println(password);
                
                if (authenticate(username, password)) {
                    
                	output.write("230 Auth réussie\r\n".getBytes());
                    // Utiliser le nom d'utilisateur dans le message
                   
                    String quit = scanner.nextLine();
                    
                    /* quitter la connexion */
                    if (quit.equals("quit")) {
                                output.write("221 Service closing control connection.\r\n".getBytes());
                                socket.close();
                    }
                  
                } else {
                	output.write("530 Authentication failed\r\n".getBytes());
                   
                }
                
		   	      

		       

		    }}

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
