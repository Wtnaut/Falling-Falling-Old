/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
/**
 *
 * @author main
 */
public class ResetFallingStatCommand implements CommandExecutor{
    Falling main;
    ResetFallingStatCommand(Falling set){
        main=set;
        
    }//instantiation

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(strings.length<1)
            return false;
        if(strings[0].equals("edrag")){
                main.ekiller="";
                return true;
        }
        if(strings[0].equals("kills")){
                main.kills.clear();
                return true;
        }
        if(strings[0].equals("blocks")){
                main.blocksbroken.clear();
                return true;
        }
        return false;
    }
}
