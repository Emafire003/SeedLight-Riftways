package me.emafire003.dev.seedlight_riftways.networking;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

@Environment(EnvType.CLIENT)
public class RiftwayClient implements Runnable{

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
            runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runClient() throws IOException
    {
        try
        {
            // getting localhost ip
            InetAddress ip = InetAddress.getByName(SeedLightRiftwaysClient.SERVER_IP);
            SeedLightRiftways.LOGGER.info("Asking the server " + ip + " if there are any active riftways...");

            // establish the connection with server port SeedLightRiftways.LISTENER_PORT (9000 usually)
            Socket s = new Socket(ip, SeedLightRiftways.LISTENER_PORT);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            try{
                // the following loop performs the exchange of
                // information between client and client handler
                while (true)
                {
                    LOGGER.info("Sending stuff");
                    //TODO switch to the list of items
                    String tosend = "diamond";
                    dos.writeUTF(tosend);

                    // printing date or time as requested by client
                    String received = dis.readUTF();
                    LOGGER.info("The server said: " + received);

                    if(received.equalsIgnoreCase("canConnect")){
                        LOGGER.info("Whoooh connecting to the other server. I think");
                        SeedLightRiftwaysClient.connectToServer();
                        tosend = "exit";
                        dos.writeUTF(tosend);
                        break;
                    }else if(received.equalsIgnoreCase("wrong_password")){
                        SeedLightRiftwaysClient.sendFailedConnectionMessage("You have not set the right combination of items for the servers password! Try again!");
                        tosend = "exit";
                        dos.writeUTF(tosend);
                        break;
                    }else if(received.equalsIgnoreCase("no_riftways")){
                        SeedLightRiftwaysClient.sendFailedConnectionMessage("No riftways present on the server!");
                        tosend = "exit";
                        dos.writeUTF(tosend);
                        break;
                    }else{
                        SeedLightRiftwaysClient.sendFailedConnectionMessage("Maybe the server is offline or unreachable, or doesn't have the mod/plugin installed!");
                        tosend = "exit";
                        dos.writeUTF(tosend);
                        break;
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
                dos.writeUTF("exit");
                dos.close();
                dis.close();
                s.close();
            }


            // closing resources
            SeedLightRiftwaysClient.connection_initialised = false;
            LOGGER.info("Closing this connection : " + s);
            s.close();
            LOGGER.info("Connection closed");
            dos.close();
        }catch(Exception e){
            SeedLightRiftwaysClient.sendFailedConnectionMessage("Maybe the server is offline or unreachable!");
            e.printStackTrace();
            SeedLightRiftwaysClient.connection_initialised = false;
        }
    }
}

