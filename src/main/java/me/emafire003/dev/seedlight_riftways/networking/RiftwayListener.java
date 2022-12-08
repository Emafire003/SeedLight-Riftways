package me.emafire003.dev.seedlight_riftways.networking;
// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;
import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.MOD_ID;

// Server class
public class RiftwayListener implements Runnable{

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            LOGGER.info("Starting the listener...");
            runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runServer() throws IOException {
        int progressive_thread_number = 0;
        LOGGER.info("Starting listener on port: " + SeedLightRiftways.LISTENER_PORT);
        // server is listening on the port provided from the seedlight class
        ServerSocket ss = new ServerSocket(SeedLightRiftways.LISTENER_PORT);

        LOGGER.info("Listener started on: " + ss);

        AtomicBoolean stopserver = new AtomicBoolean(false);
        ServerLifecycleEvents.SERVER_STOPPING.register(mcserver -> {
            stopserver.set(true);
            LOGGER.info("Stopping listener...");
        });
        // running infinite loop for getting
        // client request
        while (!stopserver.get())
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();

                LOGGER.info("A new client just connected: " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                LOGGER.info("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos);
                t.setName(MOD_ID + " cl_handler-" + progressive_thread_number);
                progressive_thread_number++;

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
        LOGGER.info("Stopped listener");
    }

}

// ClientHandler class
class ClientHandler extends Thread
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true)
        {
            try {

                // Ask user what he wants
                //dos.writeUTF("What do you want?[Date | Time]..\n"+ "Type Exit to terminate connection.");

                // receive the answer from client
                received = dis.readUTF();

                LOGGER.info("Message received: " + received);

                if(received.equals("exit"))
                {
                    //TODO set this to logger.debug
                    LOGGER.debug("Client " + this.s + " sends exit...");
                    LOGGER.info("Closing this connection.");
                    this.s.close();
                    LOGGER.debug("Connection closed");
                    break;
                }

                if(!SeedLightRiftways.IS_RIFTWAY_ACTIVE){
                    toreturn = "no_riftways";
                    dos.writeUTF(toreturn);
                    LOGGER.info("Sending 'no_riftways' to client");
                }else if(received.equalsIgnoreCase("diamond"))//TODO I would need a check for the list of items as password
                {
                    toreturn = "can_connect";
                    dos.writeUTF(toreturn);
                    LOGGER.info("Sending 'can_connect' to client");
                }else {
                    toreturn = "wrong_password";
                    dos.writeUTF(toreturn);
                    LOGGER.info("Sending 'wrong_password' to client");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
