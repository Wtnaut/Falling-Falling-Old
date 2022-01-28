/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.falling;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author main
 */
public class FallingListenerBlock implements Listener {

    Falling ref;

    FallingListenerBlock(Falling main) {
        this.ref = main;
    }//constructor

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(e.getPlayer()!=null){
        String cred = e.getPlayer().getUniqueId().toString();
            if(!ref.checkVanished(e.getPlayer())){
                if (ref.blocksbroken.get(cred) != null) {
                    int x = ref.blocksbroken.get(cred).intValue();
                    ref.blocksbroken.put(cred, (x + 1));
                } else {
                    ref.blocksbroken.put(cred, 1);
                }
            }
        }
    }//onBlockBreak
}
