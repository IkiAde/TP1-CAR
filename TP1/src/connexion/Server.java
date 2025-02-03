package connexion;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
//import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
	
	private static final String[][] USERS = {
	        {"USER admin", "PASS password123"},
	        {"USER ikimath", "PASS pass1"},
	        {"USER guest", "PASS guest123"}
	    };
	private static ServerSocket dataserver;
	 public static void main(String argv[]){
		    
		    try{
		      ServerSocket server= new ServerSocket(2121);
		      while(true){
		        Socket socket= server.accept();
		        
		        
		        InputStream input=socket.getInputStream();	
		       
		        
		        OutputStream output= socket.getOutputStream();
		        
                Scanner scanner = new Scanner(input);
                
                
                output.write("220 Bienvenue\r\n".getBytes());
                String username = scanner.nextLine();
		        System.out.println(username);
		        output.write("331 User name okay, need password\r\n".getBytes());
		        
		     
                String password = scanner.nextLine();
                System.out.println(password);
                boolean isRunning= true;
                File rootDirectory = new File(".").getCanonicalFile();
                File currentDirectory = rootDirectory;

               
                if (authenticate(username, password)) {
                    
                	output.write("230 Auth réussie\r\n".getBytes());
                    
                	
                	 while(isRunning) {
                		 
                		if (scanner.hasNextLine()) {
                			
		                    String command = scanner.nextLine();
		                    System.out.println(command);
		                   
		                    
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
		                    else if (command.startsWith("SIZE ")) {
		                        String fileName = command.substring(5).trim();
		                        File file = new File(currentDirectory, fileName);
		                        if (file.exists() && file.isFile()) {
		                            output.write(("213 " + file.length() + "\r\n").getBytes());
		                        } else {
		                            output.write("550 File not found\r\n".getBytes());
		                        }
		                    }
		                    else if (command.equalsIgnoreCase("EPSV")) {
		                        try {
		                            dataserver = new ServerSocket(0);
		                            int dataPort = dataserver.getLocalPort();
		                            String epsvResponse = "229 Entering Extended Passive Mode (|||" + dataPort + "|)\r\n";
		                            output.write(epsvResponse.getBytes());
		                        } catch (IOException e) {
		                            output.write("425 Can't open data connection\r\n".getBytes());
		                        }
		                    }
		                    else if (command.startsWith("MDTM ")) {
		                        output.write("213 \r\n".getBytes()); 
		                    }

		                    
		                    else if (command.startsWith("RETR") || command.startsWith("GET")) {
		                    	
		                        String fileName = command.substring(5).trim();
		                        File file = new File(currentDirectory, fileName);
		                        if (!file.exists()) {
		                            output.write("550 File not found\r\n".getBytes());
		                            continue;
		                        }
		                        if (dataserver == null || dataserver.isClosed()) {
		                            output.write("425 Use EPSV first\r\n".getBytes());
		                            continue;
		                        }
		                        output.write("150 Opening data connection\r\n".getBytes());
		                        try (
		                            Socket dataSocket = dataserver.accept();
		                            FileInputStream fileInput = new FileInputStream(file);
		                            BufferedInputStream bis = new BufferedInputStream(fileInput);
		                            OutputStream dataOut = dataSocket.getOutputStream()
		                        ) {
		                            byte[] buffer = new byte[4096];
		                            int bytesRead;
		                            while ((bytesRead = bis.read(buffer)) != -1) {
		                                dataOut.write(buffer, 0, bytesRead);
		                            }
		                            dataOut.flush();
		                            output.write("226 Transfer complete\r\n".getBytes());
		                 
		                        } catch (IOException e) {
		                            output.write("426 Connection closed; transfer aborted\r\n".getBytes());
		                        } 
		                        finally {
                                    if (dataserver != null && !dataserver.isClosed()) {
                                        dataserver.close();
                                        
                                    }
                                    dataserver.close();
                                    dataserver = null;
                                    
                                }
		                        
		                    }
                		}
                	 }	
                	 
                  
                } 
                
                else {
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
