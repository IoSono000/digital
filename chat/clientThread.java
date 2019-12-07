package chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class clientThread extends Thread{
	private Socket clientSocket = null;
	private DataInputStream input = null;
	private PrintStream output = null;
	private final clientThread[] threads;
	private String clientName = null;
	private int maxClientsCount;
	
	public clientThread(Socket clientSocket, clientThread[] threads) {
	 this.clientSocket = clientSocket;
	 this.threads = threads;
	 maxClientsCount = threads.length;
	}
	
	public void run() {
	 int maxClientsCount = this.maxClientsCount;
	 clientThread[] threads = this.threads;
	
	 try {
	   /*
	    * Create input and output streams for this client.
	    */
	   input = new DataInputStream(clientSocket.getInputStream());
	   output = new PrintStream(clientSocket.getOutputStream());
	   String name;
	   while (true) {
	     output.println("Enter your username.");
	     name = input.readLine().trim();
	     break;
	   }
	
	   /* Welcome the new the client. */
	   output.println("Welcome " + name + " to our chat room...");
	   synchronized (this) {
	     for (int i = 0; i < maxClientsCount; i++) {
	       if (threads[i] != null && threads[i] == this) {
	         clientName = "@" + name;
	         break;
	       }
	     }
	     for (int i = 0; i < maxClientsCount; i++) {
	       if (threads[i] != null && threads[i] != this) {
	         threads[i].output.println("--- A new user " + name + " entered the chat room!! ---");
	       }
	     }
	   }
	   /* Start the conversation. */
	   while (true) {
	     String line = input.readLine();
	     if (line.startsWith("/quit")) {
	       break;
	     }
	     /* If the message is private sent it to the given client. */
	     if (line.startsWith("@")) {
	       String[] words = line.split("\\s", 2);
	       if (words.length > 1 && words[1] != null) {
	         words[1] = words[1].trim();
	         if (!words[1].isEmpty()) {
	           synchronized (this) {
	             for (int i = 0; i < maxClientsCount; i++) {
	               if (threads[i] != null && threads[i] != this
	                   && threads[i].clientName != null
	                   && threads[i].clientName.equals(words[0])) {
	                 threads[i].output.println("<" + name + "> " + words[1]);
	                 /*
	                  * Echo this message to let the client know the private message was sent
	                  */
	                 this.output.println(">" + name + "> " + words[1]);
	                 break;
	               }
	             }
	           }
	         }
	       }
	     } else {
	       /* The message is public. Broadcast other clients. */
	       synchronized (this) {
	         for (int i = 0; i < maxClientsCount; i++) {
	           if (threads[i] != null && threads[i].clientName != null) {
	             threads[i].output.println(name + ">> " + line);
	           }
	         }
	       }
	     }
	   }
	   synchronized (this) {
	     for (int i = 0; i < maxClientsCount; i++) {
	       if (threads[i] != null && threads[i] != this
	           && threads[i].clientName != null) {
	         threads[i].output.println("*** The user " + name + " is leaving the chat room !!! ***");
	       }
	     }
	   }
	   output.println("--- Bye bye " + name + " ---");
	
	   /*
	    * Clean up. Set the current thread variable to null so that a new client
	    * could be accepted by the server.
	    */
	   synchronized (this) {
	     for (int i = 0; i < maxClientsCount; i++) {
	       if (threads[i] == this) {
	         threads[i] = null;
	       }
	     }
	   }
	   /*
	    * Close the output stream, close the input stream, close the socket.
	    */
	   input.close();
	   output.close();
	   clientSocket.close();
	 } catch (IOException ioe) {
	 }
	}
}