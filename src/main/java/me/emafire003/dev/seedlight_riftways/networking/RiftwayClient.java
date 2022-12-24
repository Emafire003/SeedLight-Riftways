package me.emafire003.dev.seedlight_riftways.networking;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import me.emafire003.dev.seedlight_riftways.client.SeedLightRiftwaysClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static me.emafire003.dev.seedlight_riftways.SeedLightRiftways.LOGGER;

@Environment(EnvType.CLIENT)
public class RiftwayClient implements Runnable{

    //TODO make the messages translatable

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

    /**This method checks if there is a riftway in the select world (the default one is the last played one)
     * and if it finds a riftway it makes the player join the world*/
    public static void connectWorld() throws ExecutionException, InterruptedException {
        if(SeedLightRiftwaysClient.SERVER_IP.equalsIgnoreCase("local") || SeedLightRiftwaysClient.SERVER_IP.equalsIgnoreCase("local:")){
            //This bit checks if the last played world has an active riftway. The first line gets the last played world for the client,
            // the other two check if it's contained in the list of active riftways in the worlds
            if(MinecraftClient.getInstance().isInSingleplayer()){ //If it's the last played and we are in single player, it will just connect to itself
                SeedLightRiftwaysClient.setLocalConnectionAllowed();
                return;
            }
            String worldName = Objects.requireNonNull(SeedLightRiftwaysClient.getLastPlayedWorld()).getName();
            if(worldName == null){ // Null check, if it's null it means the level doesn't exist aka no worlds in the saves folder
                SeedLightRiftwaysClient.sendFailedConnectionMessage("Maybe the world you have specified doesn't exist or is in another location!");
                return;
            }
            if(SeedLightRiftways.getIsRiftwayActiveInWorld().contains(worldName)){
                SeedLightRiftwaysClient.playEnterRiftwaySoundEffect(); //TODO not sure which of the two of these gets triggered
                SeedLightRiftwaysClient.setLocalConnectionAllowed(); // Actually connecting to the world
                SeedLightRiftwaysClient.playEnterRiftwaySoundEffect();
            }else{
                SeedLightRiftwaysClient.sendFailedConnectionMessage("No riftways present in that world!");
            }
        }
        //This is an important bit. If the string IS "local" or "local:" there's the block above. If it STARTS with "local" but NOT "local:"
        // it means the string is badly formatted, because it needs to be local:worldname.
        // Not starting with "local:" but starting with "local" it means that the string is like this: "localworldname" which is incorrect
        else if(SeedLightRiftwaysClient.SERVER_IP.startsWith("local") && !SeedLightRiftwaysClient.SERVER_IP.startsWith("local:")){
            SeedLightRiftwaysClient.sendFailedConnectionMessage("The name of the world to connect to is badly formatted! It should be 'local:worldname' and not 'localworldname'");
        }
        else{ // Here there should be a bit of the string after "local:". That's the world name, the one the player will connect to
            String worldName = SeedLightRiftwaysClient.SERVER_IP.replaceAll("local:", "");
            if(SeedLightRiftways.getIsRiftwayActiveInWorld().contains(worldName)){
                SeedLightRiftwaysClient.playEnterRiftwaySoundEffect(); //TODO not sure which of the two of these gets triggered
                SeedLightRiftwaysClient.setLocalConnectionAllowed(); // Actually connecting to the world
                SeedLightRiftwaysClient.playEnterRiftwaySoundEffect();
            }else{
                SeedLightRiftwaysClient.sendFailedConnectionMessage("1No riftways present in that world!");
            }
        }
    }

    public static void runClient() throws IOException
    {
        try
        {
            // getting localhost ip
            if(SeedLightRiftwaysClient.SERVER_IP.startsWith("local")){
                connectWorld();
                SeedLightRiftwaysClient.connection_initialised = false;
                return;
            }
            InetAddress ip = InetAddress.getByName(SeedLightRiftwaysClient.SERVER_IP);
            SeedLightRiftways.LOGGER.debug("Asking the server " + ip + " if there are any active riftways...");

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
                    String tosend = SeedLightRiftwaysClient.SERVER_ITEMS_PASSWORD;
                    dos.writeUTF(tosend);

                    // printing date or time as requested by client
                    String received = dis.readUTF();
                    LOGGER.debug("The server said: " + received);

                    if(received.equalsIgnoreCase("can_connect")){
                        SeedLightRiftwaysClient.playEnterRiftwaySoundEffect();
                        SeedLightRiftwaysClient.setConnectionAllowed();
                        SeedLightRiftwaysClient.playEnterRiftwaySoundEffect();
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
            LOGGER.debug("Connection closed");
            dos.close();
        }catch(Exception e){
            SeedLightRiftwaysClient.sendFailedConnectionMessage("Maybe the server is offline or unreachable!");
            e.printStackTrace();
            SeedLightRiftwaysClient.connection_initialised = false;
        }
    }
}

