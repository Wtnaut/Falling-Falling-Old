/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
/**
 *
 * @author main
 */
public class SetFallingStatsCommand implements CommandExecutor{
    Falling main;
    SetFallingStatsCommand(Falling set){
        main=set;
    }//instantiation
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        try{
        if(strings.length>=3){
            OfflinePlayer p=main.getServer().getOfflinePlayer(strings[1]);
            main.getServer().getConsoleSender().sendMessage("[FF] setfallingstats test "+Integer.parseInt(strings[2]));
            if(strings[0].equals("edrag")){
                main.ekiller=p.getUniqueId().toString();
            }if("kills".equals(strings[0])){
                main.kills.put(p.getUniqueId().toString(),Integer.parseInt(strings[2]));
            }
            if("blocks".equals(strings[0])){
                main.blocksbroken.put(p.getUniqueId().toString(),Integer.parseInt(strings[2]));
            }
            return true;
        }else if(strings.length==2){
            OfflinePlayer p=main.getServer().getPlayer(strings[1]);
            
            if(strings[0].equals("edrag"))
                main.ekiller=p.getUniqueId().toString();
            return true;
        }
        else
            return false;
        }catch(Exception e){
            return false;
        }//try catch
       
    }//method
}

