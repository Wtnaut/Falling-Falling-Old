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
public class SpawnFallingEventCommand implements CommandExecutor{
    Falling main;
    SpawnFallingEventCommand(Falling set){
        main=set;
    }
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        try{
        if(args.length<1)
            main.spawnEvents();
        else{
            args[0]=args[0].toLowerCase();
            switch(args[0]){
                case"cobblestorm":
                    main.cobbleStorm();
                    break;
                case"anvils":
                    main.anvils();
                    break;
                case"motherlode":
                    main.motherlode();
                    break;
                case"totemspawn":
                    main.breathOfLife();
                    break;
                case"generosity":
                    main.doubleDip();
                    break;
                case"invasion":
                    if(args.length<2)
                        main.invasion();
                    else{
                        try{
                            int x=Integer.parseInt(args[1]);
                            if (x>5||x<0)
                                x=0;
                            main.invasion(x);
                        }catch(Exception e){
                             main.getServer().getConsoleSender().sendMessage("[FF] Invasion Error.");
                        }
                    }//inner else
                    break;
                case"feast":
                    main.cooked();
                    break;
                case"starfall":
                    main.starryNight();
                    break;
                default:
                    main.spawnEvents();
                    break;
            }//switch
        }//if else
        
        }catch(Exception e){
            main.getServer().getConsoleSender().sendMessage("[FF] Command spawnfallingevent failed.");
        }//catch
        return true;
    }
}//class
