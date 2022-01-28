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
public class ClearFallingStatsCommand implements CommandExecutor {

    Falling test;

    ClearFallingStatsCommand(Falling ref) {
        this.test = ref;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        test.blocksbroken.clear();
        test.ekiller = "";
        test.kills.clear();
        return true;
    }
}
