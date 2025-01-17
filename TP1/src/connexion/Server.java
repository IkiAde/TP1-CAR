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
                boolean isRunning= true;
                
               
                if (authenticate(username, password)) {
                    
                	output.write("230 Auth réussie\r\n".getBytes());
                    // Utiliser le nom d'utilisateur dans le message
                	 while(isRunning) {
                		 
                		if (scanner.hasNextLine()) {
                			
		                    String command = scanner.nextLine();
		                    System.out.println(command);
		                    /* quitter la connexion */
		                    if (command.equals("SYST")) {
		                                output.write("215 windows system type.\r\n".getBytes());
		                               
		                    }
		                    
		                    else if(command.equals("FEAT")){
		                    	output.write("211-Features:\r\n".getBytes());
		                        output.write(" EPRT\r\n".getBytes());
		                        output.write(" EPSV\r\n".getBytes());
		                        output.write(" MDTM\r\n".getBytes());
		                        output.write(" SIZE\r\n".getBytes());
		                        output.write("211 End\r\n".getBytes());
		                    }
		                    else if (command.equalsIgnoreCase("QUIT")) {
		                        output.write("221 Service closing control connection\r\n".getBytes());
		                        isRunning = false;
		                        }
                		}
                	 }	
                  
                } else {
                	output.write("530 Authentication failed\r\n".getBytes());
                   
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
