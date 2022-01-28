/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author main
 */
public class InvasionLocationCommand implements CommandExecutor {

    Falling main;
    InvasionLocationCommand(Falling set){
        main=set;
    }
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(cs instanceof Player){
            main.invasionLoc=((Player)(cs)).getLocation();
            return true;
        } 
        return false;
    }
    
}
