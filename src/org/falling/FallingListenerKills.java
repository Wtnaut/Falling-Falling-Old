/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 *
 * @author main
 */
public class FallingListenerKills implements Listener {

    Falling ref;

    FallingListenerKills(Falling main) {
        this.ref = main;
    }//constructor

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(e.getEntity().getKiller()!=null){
        String killcred = e.getEntity().getKiller().getUniqueId().toString();
        if (ref.kills.get(killcred) != null&&e.getEntity().getKiller()!=null) {
            ref.kills.replace(killcred, (ref.kills.get(killcred).intValue() + 1));
        } else {
            ref.kills.put(killcred, 1);
        }
        }//check if null
        if(ref.questcount==1&&e.getEntity()!=null){
            String dcred = e.getEntity().getUniqueId().toString();
            if (ref.event.get(dcred) != null) {
                ref.event.replace(dcred, (ref.event.get(dcred).intValue() + 1));
            } else {
                ref.event.put(dcred, 1);
            }//inner if else
        }
        
    }//onPlayerDeath
}
