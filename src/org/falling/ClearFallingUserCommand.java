/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 *
 * @author main
 */
public class ClearFallingUserCommand implements CommandExecutor{
    Falling main;
    ClearFallingUserCommand(Falling set){
        main=set;
    }//instantiation
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        try{
        if(strings.length>0){
            OfflinePlayer p=main.getServer().getOfflinePlayer(strings[0]);
            if(p==null){
                if(cs instanceof Player)
                    ((Player)cs).sendMessage(ChatColor.RED+"[FFdebug] Player not found.");
                return false;
            }//if
            if(main.ekiller.equals(p.getUniqueId().toString()))
                main.ekiller="";
            main.blocksbroken.remove(p.getUniqueId().toString());
            main.kills.remove(p.getUniqueId().toString());
            return true;
        }
        else
            return false;
        }catch(Exception e){
            return false;
        }
    }
}

