package me.emafire003.dev.seedlight_riftways.networking;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

public class RiftwayServerInfo implements Runnable {

    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = SeedLightRiftways.LISTENER_PORT;

    public static void start(){

    }

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
        //create the socket server object
        LOGGER.info("Listener server started!");
        try {
            server = new ServerSocket(port);
            AtomicBoolean stopserver = new AtomicBoolean(false);
            ServerLifecycleEvents.SERVER_STOPPING.register(mcserver -> {
                stopserver.set(true);
            });
            //keep listens indefinitely until receives 'exit' call or program terminates
            while(!stopserver.get()){
                //creating socket and waiting for client connection
                Socket socket = server.accept();
                //read from socket to ObjectInputStream object
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //convert ObjectInputStream object to String
                String message = (String) ois.readObject();
                LOGGER.info("Message Received: " + message);
                //create ObjectOutputStream object
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //write object to Socket
                if(SeedLightRiftways.RIFTWAYS_LOCATIONS.isEmpty() || !SeedLightRiftways.IS_RIFTWAY_ACTIVE){
                    oos.writeUTF("no_riftways");
                }else if(message.equalsIgnoreCase("diamond"))//TODO I would need a check for the list of items as password
                {
                    oos.writeUTF("can_connect");
                }else {
                    oos.writeUTF("wrong_password");
                }
                //close resources
                ois.close();
                oos.close();
                socket.close();
            }
            /*System.out.println("Shutting down Socket server!!");
            //close the ServerSocket object
            server.close();*/

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
