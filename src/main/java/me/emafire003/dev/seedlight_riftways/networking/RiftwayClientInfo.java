package me.emafire003.dev.seedlight_riftways.networking;

import me.emafire003.dev.seedlight_riftways.SeedlightRiftways;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
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
            //get the localhost IP address, if server is running on some other IP, you need to use that
            InetAddress host = InetAddress.getByName(SeedLightRiftwaysClient.SERVER_IP);
            Socket socket = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            for(int i=0; i<5;i++){
                //establish socket connection to server
                socket = new Socket(host.getHostName(), SeedlightRiftways.LISTENER_PORT);
                //write to socket using ObjectOutputStream
                oos = new ObjectOutputStream(socket.getOutputStream());
                SeedlightRiftways.LOGGER.info("Asking the server " + host + " if there are any active riftways...");
                if(i==4)oos.writeObject("exit");
                else oos.writeObject(""+i);
                if(i==4){
                    oos.writeObject("exit");
                }else{
                    oos.writeUTF("diamond");
                }
                //read the server response message
                ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();
                if(message.equalsIgnoreCase("canConnect")){

                }
                //close resources
                ois.close();
                oos.close();
                Thread.sleep(100);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
