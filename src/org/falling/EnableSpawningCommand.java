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
public class EnableSpawningCommand implements CommandExecutor {

    Falling test;

    EnableSpawningCommand(Falling ref) {
        this.test = ref;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        test.spawning = !test.spawning;
        return true;
    }

}
