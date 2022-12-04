package me.emafire003.dev.seedlight_riftways.networking;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class RiftwayClientInfo implements Runnable {

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
        try {
            InetAddress host = InetAddress.getByName(SeedLightRiftwaysClient.SERVER_IP);
            Socket socket = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            //establish socket connection to server
            socket = new Socket(host.getHostName(), SeedLightRiftways.LISTENER_PORT);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            SeedLightRiftways.LOGGER.info("Asking the server " + host + " if there are any active riftways...");


            //TODO will need to send the password along (aka the set of item names)
            oos.writeUTF("diamond");

            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            if(message.equalsIgnoreCase("canConnect")){
                SeedLightRiftwaysClient.connectToServer();
            }else if(message.equalsIgnoreCase("wrong_password")){
                SeedLightRiftwaysClient.sendFailedConnectionMessage("You have not set the right combination of items for the servers password! Try again!");
            }else if(message.equalsIgnoreCase("no_riftways")){
                SeedLightRiftwaysClient.sendFailedConnectionMessage("No riftways present on the server!");
            }else{
                SeedLightRiftwaysClient.sendFailedConnectionMessage("Maybe the server is offline or unreachable!");
            }
            //close resources
            ois.close();
            oos.close();
            socket.close();

        }catch (Exception e) {
            SeedLightRiftwaysClient.sendFailedConnectionMessage("Maybe the server is offline or unreachable!");
            e.printStackTrace();
        }
    }
}
