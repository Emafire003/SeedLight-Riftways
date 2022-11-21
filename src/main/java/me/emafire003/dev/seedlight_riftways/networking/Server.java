package me.emafire003.dev.seedlight_riftways.networking;

import me.emafire003.dev.seedlight_riftways.SeedlightRiftways;

import java.net.*;
import java.io.*;

//TODO the "password" could be a list of items which needs to be "given" to the portal. I think i could simply hash it
//without really salting them or whatever. It's not meant to be a real security feature, authme plugins adn mods exist
public class Server
{
    //initialize socket and input stream
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;
    private Socket          socket   = null;
    //private ServerSocket    server   = null;
    private DataInputStream in       =  null;
    private DataOutputStream out     =  null;

    // constructor with port
    public Server(int port)
    {
        Thread thread = new Thread();
        thread.start();
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            SeedlightRiftways.LOGGER.info("Web Server started");

            SeedlightRiftways.LOGGER.info("Waiting for a client ...");

            socket = server.accept();
            SeedlightRiftways.LOGGER.info("Client accepted");

            // takes input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            out = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            String line = "";

            // reads message from client until "Over" is sent
            while (!line.equals("Over"))
            {
                try
                {
                    String key = in.readUTF();
                    //json file read line 1
                    String password = "password";
                    //hash key or unhash the other pswd
                    if(key.equals("password")){
                        //send data in output
                    }
                    //Salvare il salt in un file chiamato salt.txt e la password come hash saltato nel file normale

                    SeedlightRiftways.LOGGER.info("Client used tried to us the key" + line);

                }
                catch(IOException i)
                {
                    SeedlightRiftways.LOGGER.info(i.toString());
                }
            }
            SeedlightRiftways.LOGGER.info("Closing connection");

            // close connection
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            SeedlightRiftways.LOGGER.info(i.toString());
        }
    }
}
