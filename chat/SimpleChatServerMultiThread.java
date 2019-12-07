package chat;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

// Server class
public class SimpleChatServerMultiThread {
	
// Server socket.
private static ServerSocket serverSocket = null;
// Client socket.
private static Socket clientSocket = null;

// Defines the maximum number of clients
private static final int maxNumClients = 10;
private static final clientThread[] clients = new clientThread[maxNumClients];

public static void main(String args[]) {

 // The default port number.
 int port = 10000;
 if (args.length < 1)
   System.out.println("Server started..\n" + "Using port = " + port);
 else
   port = Integer.valueOf(args[0]).intValue();

 /*
  * Open a server socket on the port (default 10000)
  */
 try 
 {
   serverSocket = new ServerSocket(port);
 } catch (IOException e) {
   System.out.println("Errorino " + e);
 }

 /*
  * Create a client socket for each connection and pass it to a new client
  * thread.
  */
 while (true) {
   try {
     clientSocket = serverSocket.accept();
     int cont = 0;
     for (cont = 0; cont < maxNumClients; cont++) {
       if (clients[cont] == null) {
         (clients[cont] = new clientThread(clientSocket, clients)).start();
         break;
       }
     }
     if (cont == maxNumClients) {
       PrintStream os = new PrintStream(clientSocket.getOutputStream());
       os.println("The maximum number of users has been reached. Try again later..");
       os.close();
       clientSocket.close();
     }
   } catch (IOException ioe) {
     System.out.println(ioe);
   }
 }
}  
}