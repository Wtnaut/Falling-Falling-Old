/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author main
 */
public class FallingInfoCommand implements CommandExecutor{
    Falling main;
    FallingInfoCommand(Falling set){
        main=set;
    }//instantiation
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(cs instanceof Player){
            Integer z=main.kills.get(((Player) cs).getUniqueId().toString());
                if(z==null){
                    z=0;
                }
            Integer y=main.blocksbroken.get(((Player) cs).getUniqueId().toString());
            if(y==null){
                y=0;
            }
            ((Player)cs).sendMessage(ChatColor.BOLD+""+ChatColor.GOLD+""+ChatColor.ITALIC+""+ChatColor.BOLD+"FallingFalling -- User Information");
            ((Player)cs).sendMessage(ChatColor.WHITE+"User: "+((Player)cs).getDisplayName());
            ((Player)cs).sendMessage(ChatColor.WHITE+""+ChatColor.BOLD+"Scores:");
            ((Player)cs).sendMessage(ChatColor.WHITE+"  Kills:"+z);
            ((Player)cs).sendMessage(ChatColor.WHITE+"  Blocks:"+y);
            if(main.ekiller.equals(((Player)cs).getUniqueId().toString()))
                ((Player)cs).sendMessage(ChatColor.WHITE+"  Dragon: First Place");
            else
                ((Player)cs).sendMessage(ChatColor.WHITE+"  Dragon: N/A");
            if(main.questcount==1||main.questcount==3){
                Integer x=main.event.get(((Player) cs).getUniqueId().toString());
                if(x==null){
                    x=0;
                }
                
            ((Player)cs).sendMessage(ChatColor.YELLOW+"  Event:"+x);
            }else
            ((Player)cs).sendMessage("");
            ((Player)cs).sendMessage(ChatColor.ITALIC+""+ChatColor.GRAY+"Welcome to FallingFalling! Completing any of the three permanent objectives makes you a winner of the server! Rewards will be given out on our OSMC server.");
            return true;
        }//if
        return false;
    }//oncommand
    
}
