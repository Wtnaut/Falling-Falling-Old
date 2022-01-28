/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 *
 * @author main
 */
public class FallingTabCompletion implements TabCompleter{
    Falling ref;
    FallingTabCompletion(Falling main){
        ref=main;
    }//constructor
    
    @Override
    public List<String> onTabComplete (CommandSender sender, Command cmd, String string, String[] args){
        List<String> list = new ArrayList<>();
        if(cmd.getName().equals("spawnfallingevent")){
            if(args.length<2){
                list.add("cobblestorm");
                list.add("anvils");
                list.add("motherlode");
                list.add("totemspawn");
                list.add("generosity");
                list.add("invasion");
                list.add("feast");
                list.add("starfall");
            }else if(args[0].equals("invasion")){
                list.add("1");
                list.add("2");
                list.add("3");
                list.add("4");
                list.add("5");
            }
                
        }//spawnfallingevent
        if((cmd.getName().equals("setfallingstats")&&args.length==2)||cmd.getName().equals("clearfallinguser")){
            for(Player p:ref.getServer().getOnlinePlayers())
                list.add(p.getDisplayName());
        }//setfallingstats
        if(cmd.getName().equals("resetfallingstat")||(cmd.getName().equals("setfallingstats")&&args.length<=1)){
            list.add("edrag");
            list.add("kills");
            list.add("blocks");
        }
        return list;
    }

}
