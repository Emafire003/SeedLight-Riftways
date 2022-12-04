package me.emafire003.dev.seedlight_riftways.util;

import me.emafire003.dev.seedlight_riftways.SeedLightRiftways;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CheckValidAddress extends Thread {
    private String address = "";
    private PlayerEntity player;

    public void setAddress(String address){
        this.address = address;
    }
    public void setPlayer(PlayerEntity p){
        this.player = p;
    }

    @Override
    public void run() {

        InetAddress[] iaddress
                = new InetAddress[0];
        try {
            iaddress = InetAddress.getAllByName(address);
        } catch (UnknownHostException e) {
            player.sendMessage(Text.literal(SeedLightRiftways.PREFIX+" §c§lWARNING! §cThe address you have specified, §5§l" + address + " §c is incorrect or unreachable! (Check your internet connection!)"));
            e.printStackTrace();
        }
        iaddress[0].toString();
        super.run();
    }
}
