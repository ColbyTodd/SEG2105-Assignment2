//**** Added for E50 b) CT

import java.io.*;

import common.*;
import ocsf.server.*;

public class ServerConsole implements ChatIF {

    /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  EchoServer server;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {

      server = new EchoServer(port);
  }

  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try 
    {
      server.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        //**** Changed for E50 a) CT
        if(message.charAt(0) == '#'){
          String[] command = message.split(" ");
          switch(command[0]){
            case "#quit":
              server.close();
              System.exit(1);
              break;

            case "#stop":
              server.stopListening();
              break;
            
            case "#close":
              server.close();
              break;

            case "#setport":
              server.setPort(Integer.parseInt(command[1]));
              break;

            case "#start":
              if(server.isListening() == false){
                server.listen();
              } else{
                display("Already running");
              }
              break;

            case "#getport":
              display(String.valueOf(server.getPort()));
              break;

            default:
              display("Invalid command");

          } 
        }else{
            server.sendToAllClients("SERVER MSG> " + message);
        display(message);
        
          }
        
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }
    
    /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("SERVER MSG> " + message);
  }

   /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    ServerConsole console = new ServerConsole(port);
    console.accept();
  }
}
