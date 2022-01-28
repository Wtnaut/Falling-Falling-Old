/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author main
 */
public class FallingListener implements Listener {

    Falling ref;

    FallingListener(Falling main) {
        this.ref = main;
    }//constructor

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntityType() == EntityType.ENDER_DRAGON && ref.ekiller.equals("")&&e.getEntity().getKiller()!=null) {
            ref.ekiller = e.getEntity().getKiller().getUniqueId().toString();
        }
        if(ref.questcount==3&&e.getEntity().getKiller()!=null){
            String killcred = e.getEntity().getKiller().getUniqueId().toString();
            if (ref.event.get(killcred) != null&&e.getEntity().getKiller()!=null) {
                ref.event.replace(killcred, (ref.event.get(killcred).intValue() + 1));
            } else {
                ref.event.put(killcred, 1);
            }//inner if else
        }//if
        if(ref.questcount==2&&ref.questentity!=null&&e.getEntity().getKiller()!=null){
            if(e.getEntity().equals(ref.questentity)){
                e.getEntity().getKiller().getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING));
                ref.getServer().broadcastMessage(ChatColor.DARK_PURPLE+""+e.getEntity().getKiller().getDisplayName()+" killed QuestPig! They are "+ChatColor.GOLD+"UNDYING");
                ref.questentity=null;
                ref.questcount=0;
            }//inner if
        }//outer if
    }
}//class
