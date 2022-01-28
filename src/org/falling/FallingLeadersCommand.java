/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;

import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author main
 */
public class FallingLeadersCommand implements CommandExecutor{
    Falling ref;
    
    FallingLeadersCommand(Falling main){
        ref=main;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        int prevmax=-1;
        int max=0;
        String uuid="";
        cs.sendMessage("Blocks:");
        for(int outerit=0;outerit<10;outerit++){
            uuid="";
            max=0;
            for(String key:ref.blocksbroken.keySet()){
                if (ref.blocksbroken.get(key) > max&&(ref.blocksbroken.get(key)<prevmax||prevmax==-1)) {
                    max = ref.blocksbroken.get(key);
                    uuid = key;
                }else if(ref.blocksbroken.get(key)==max){
                   cs.sendMessage(outerit+"> "+ref.getServer().getOfflinePlayer(UUID.fromString(key)));
                }
            }
            cs.sendMessage(outerit+"> "+ref.getServer().getOfflinePlayer(UUID.fromString(uuid)));
            prevmax=max;
        }//outerit
        
        prevmax=-1;
        max=0;
        uuid="";
        cs.sendMessage("Kills:");
        for(int outerit=0;outerit<10;outerit++){
            uuid="";
            max=0;
            for(String key:ref.kills.keySet()){
                if (ref.kills.get(key) > max&&(ref.kills.get(key)<prevmax||prevmax==-1)) {
                    max = ref.kills.get(key);
                    uuid = key;
                }else if(ref.kills.get(key)==max){
                   cs.sendMessage(outerit+"> "+ref.getServer().getOfflinePlayer(UUID.fromString(key)));
                }
            }
            cs.sendMessage(outerit+"> "+ref.getServer().getOfflinePlayer(UUID.fromString(uuid)));
            prevmax=max;
        }//outerit
        return true;
    }
    
}
