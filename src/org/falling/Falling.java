package org.falling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author main
 */
public class Falling extends JavaPlugin {

    public boolean spawning = false;
    public boolean showscore = true;
    public String ekiller = "";
    public String lastevent="";
    
    Scoreboard board;

    Location invasionLoc;
    
    public int invcount=0;
    public int questcount=0; 
    Entity questentity=null;

    public int message = 0;
    public HashMap<String, Integer> event = new HashMap<String, Integer>();
    public HashMap<String, Integer> blocksbroken = new HashMap<String, Integer>();
    public HashMap<String, Integer> kills = new HashMap<String, Integer>();

    @Override
    public void onEnable() {
        this.getCommand("spawnfalling").setExecutor(new SpawnFallCommand());
        this.getCommand("togglefall").setExecutor(new EnableSpawningCommand(this));
        this.getCommand("clearfallingstats").setExecutor(new ClearFallingStatsCommand(this));
        this.getCommand("seteventlocation").setExecutor(new InvasionLocationCommand(this));
        this.getCommand("fallinginfo").setExecutor(new FallingInfoCommand(this));
        
        this.getCommand("spawnfallingevent").setExecutor(new SpawnFallingEventCommand(this));
        this.getCommand("setfallingstats").setExecutor(new SetFallingStatsCommand(this));
        this.getCommand("clearfallinguser").setExecutor(new ClearFallingUserCommand(this));
        this.getCommand("resetfallingstat").setExecutor(new ResetFallingStatCommand(this));
        this.getCommand("fallingleaders").setExecutor(new FallingLeadersCommand(this));
        
        this.getCommand("spawnfallingevent").setTabCompleter(new FallingTabCompletion(this));
        this.getCommand("setfallingstats").setTabCompleter(new FallingTabCompletion(this));
        this.getCommand("clearfallinguser").setTabCompleter(new FallingTabCompletion(this));
        this.getCommand("resetfallingstat").setTabCompleter(new FallingTabCompletion(this));
                
        getServer().getPluginManager().registerEvents(new FallingListener(this), this);
        getServer().getPluginManager().registerEvents(new FallingListenerBlock(this), this);
        getServer().getPluginManager().registerEvents(new FallingListenerKills(this), this);

        this.invasionLoc = new Location(this.getServer().getWorlds().get(0), 0, 60, 0);
        this.getDataFolder().mkdirs();

        board = this.getServer().getScoreboardManager().getMainScoreboard();
        try {

            loadBase();
            loadScore();
        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("[FF] FallingFalling failed to load old data.");
            ekiller="";
            spawning=false;
            showscore=false;
        }//exception

        setupScore();

        int id = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                spawnAll();
            }
        }, 0, 80);
        int id2 = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                updateScores();
            }
        }, 25, 100);
        int id3 = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                spawnEvents();
            }
        }, 75, 18000);

    }//onenable

    //---------------------OTHER-------------------------
    public boolean checkVanished(Player p){
        for(Player check:this.getServer().getOnlinePlayers()){
            if(check.canSee(p)==false)
                return true;
        }
        return false;
    }
    //---------------------EVENTS-------------------------
    public void spawnEvents() {
        if (this.getServer().getOnlinePlayers().size() != 0&&this.spawning==true) {
            try{
            if(this.invcount>=3){
                this.invasion();
                invcount=0;
            }else{
                invcount++;
                switch((int)(Math.random()*7)){
                    default:
                        this.cobbleStorm();
                        break;
                    case 1:
                        this.anvils();
                        break;
                    case 2:
                        this.motherlode();
                        break;
                    case 3:
                        this.breathOfLife();
                        break;
                    case 4:
                        this.doubleDip();
                        break;
                    case 5:
                        this.cooked();
                        break;
                    case 6:
                        this.starryNight();
                        break;
                    
                }
            }//else
            }catch(Exception e){
                this.getServer().getConsoleSender().sendMessage("[FF] Failed to spawn event.");
            }
        }//if
    }//spawnevents

    public Player randomPlayer() throws Exception{
        ArrayList playerlist = new ArrayList<Player>();
        for (Player p : this.getServer().getOnlinePlayers()) {
            if(!checkVanished(p))
                playerlist.add(p);
        }
        return (Player) playerlist.get(((int) (Math.random() * playerlist.size())));
    }//randomPlayer

    public void broadcastTitle(String title, String subtext) {
        for (Player p : this.getServer().getOnlinePlayers()) {
            p.sendTitle(title, subtext, 10, 80, 30);
        }
    }//broadcastTitle

    public void cobbleStorm() throws Exception{
        this.broadcastTitle(ChatColor.RED + "WARNING", "COBBLESTORM");
        this.lastevent=ChatColor.YELLOW+"Cobblestorm";

        for (int x = 0; x <= 2000; x += 200) {
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
                    try{
                    Player cobbled = randomPlayer();
                    getServer().broadcastMessage(ChatColor.AQUA + "" + cobbled.getName() + ChatColor.RED + " is gettin cobbled!");
                    for (int x = -2; x < 3; x++) {
                        for (int z = -2; z < 3; z++) {
                            cobbled.getWorld().spawnFallingBlock(new Location(cobbled.getWorld(), cobbled.getLocation().getBlockX() + x + 0.5, 150, cobbled.getLocation().getBlockZ() + z + 0.5), Material.COBBLESTONE, (byte) 0);
                        }//inner for
                    }//outer for
                    
                    for (int x = -2; x < 3; x++) {
                        for (int z = -2; z < 3; z++) {
                            cobbled.getWorld().spawnFallingBlock(new Location(cobbled.getWorld(), cobbled.getLocation().getBlockX() + x + 0.5, 153, cobbled.getLocation().getBlockZ() + z + 0.5), Material.COBBLESTONE, (byte) 0);
                        }//inner for
                    }//outer for

                    }catch(Exception e){
                        getServer().getConsoleSender().sendMessage("[FF] FallingFalling event fail @cobbleStorm.");
                    }//catch
                }//run()
            }, 100 + x);//runnable
            if(x/200>this.getServer().getOnlinePlayers().size()/2)
                x=2001;
        }//outermost for

    }//cobblestorm

    public void anvils() throws Exception{
        this.broadcastTitle(ChatColor.RED + "WARNING", "ANVILS DETECTED");
        this.lastevent=ChatColor.YELLOW+"Anvils";
        for(int x=0;x<400;x+=50){
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() { 
                public void run(){
                    for(Player anvilled:getServer().getOnlinePlayers()){
                        Location l=anvilled.getLocation();
                        l.setY(150);
                        anvilled.getWorld().spawnFallingBlock(l,Material.ANVIL,(byte)0);
                     }//for loop
                }//run
            },100+x);
        }//outer for loop
    }//anvils

    public void motherlode() throws Exception{
        Player p=this.randomPlayer();
        Location l =p.getLocation().clone();
        int x;
        int z;
        l.setY(150);
        do{
        x=((int)(Math.random()*55));
        z=55-x;
        switch((int)(4*Math.random())){
            default:
                break;
            case 1:
                z= -z;
                break;
            case 2:
                z= -z;
                x= -x;
                break;
            case 3:
                x= -x;
                break;
        }
        l.setX(x+l.getBlockX());
        l.setZ(z+l.getBlockZ());
        }while(!p.getWorld().getWorldBorder().isInside(l));
        for(int y=0;y<5;y++){
            for(int xi=-2;xi<=2;xi++){
                for(int zi=-2;zi<=2;zi++){
                    Location temp=new Location(l.getWorld(),l.getBlockX()+xi+0.5,l.getY()+(2*y),l.getBlockZ()+zi+0.5);
                    if(y==0||y==4||xi==-2||xi==2||zi==-2||zi==2){
                        if(((int)(2*Math.random()))==0)
                            temp.getWorld().spawnFallingBlock(temp,Material.OBSIDIAN,(byte)0);
                        else
                            temp.getWorld().spawnFallingBlock(temp,Material.DEEPSLATE_COAL_ORE,(byte)0);
                    }else if(zi!=0||xi!=0){
                        if((int)(4*Math.random())<3)
                            temp.getWorld().spawnFallingBlock(temp,Material.DEEPSLATE_IRON_ORE,(byte)0);
                        else if((int)(4*Math.random())<3)
                            temp.getWorld().spawnFallingBlock(temp,Material.IRON_BLOCK,(byte)0);
                        else
                            temp.getWorld().spawnFallingBlock(temp,Material.DIAMOND_BLOCK,(byte)0);
                    }else if(y==2&&xi==0&&zi==0){
                        if((int)(8*Math.random())==0)
                            temp.getWorld().spawnFallingBlock(temp,Material.NETHERITE_BLOCK,(byte)0);
                        else
                            temp.getWorld().spawnFallingBlock(temp,Material.DIAMOND_BLOCK,(byte)0);
                    }else
                        temp.getWorld().spawnFallingBlock(temp,Material.IRON_BLOCK,(byte)0);
                }//z for
            }//x for
            l.setY(l.getBlockY()+3);
        }//Y for

        //get coords from a random player
        this.broadcastTitle(ChatColor.AQUA + "MOTHERLODE"," AT: X" + l.getBlockX() + " Z" + l.getBlockZ());
        this.lastevent=ChatColor.YELLOW+"Motherlode X"+l.getBlockX()+" Z"+l.getBlockZ();
    }//motherlode

    public void breathOfLife() throws Exception{
        this.questcount=(1+((int)(Math.random()*3)));
        String x="";
        switch(questcount){
            default:
                x="Most Deaths";
                this.questentity=null;
                break;
            case 2:
                x="Kill QuestPig";
                
                
                this.questentity=this.invasionLoc.getWorld().spawnEntity(this.invasionLoc.clone(), EntityType.PIG);
                ((Pig)this.questentity).setGlowing(true);
                ((Pig)this.questentity).setMaxHealth(75);
                ((Pig)this.questentity).setHealth(75);
                ((Pig)this.questentity).setCustomName(ChatColor.AQUA+""+ChatColor.BOLD+"QuestPig");
                ((Pig)this.questentity).setCustomNameVisible(true);
                 this.getServer().getScheduler().scheduleSyncDelayedTask(this,new Runnable(){public void run(){
                     Pig name = (Pig)questentity;
                     questentity=null;
                     if(name!=null){
                     name.setHealth(0);
                     getServer().broadcastMessage(ChatColor.AQUA+"QuestPig got away...");
                     }
                 }},3000);
                break;
            case 3:
                x="Mob Kills";
                this.questentity=null;
                break;
        }//switch
        if(questcount==1||questcount==3){
            this.getServer().getScheduler().scheduleSyncDelayedTask(this,new Runnable(){public void run(){
                     if (event.size() == 0) {
                         getServer().broadcastMessage(ChatColor.DARK_PURPLE+"NOBODY tried the totem quest!"+ChatColor.RED+" YOU ARE ALL"+ChatColor.BOLD+" MORTAL.");
                        } else {
                            int max = 0;
                            String uuid = "";
                            for (Player key : getServer().getOnlinePlayers()) {
                                if(event.get(key.getUniqueId().toString())!=null){
                                    if (event.get(key.getUniqueId().toString()) > max) {
                                        max = event.get(key.getUniqueId().toString());
                                        uuid = key.getUniqueId().toString();
                                    }
                                }//outer if
                                
                            }//for
                            if(!uuid.equals("")){
                                getServer().broadcastMessage(ChatColor.DARK_PURPLE+""+getServer().getPlayer(UUID.fromString(uuid)).getName().toString()+" won the quest! They are "+ChatColor.GOLD+"UNDYING");
                                if(getServer().getPlayer(UUID.fromString(uuid)).isOnline()){
                                    getServer().getPlayer(UUID.fromString(uuid)).getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING));
                                }//if
                            }else
                                getServer().broadcastMessage(ChatColor.DARK_PURPLE+"NOBODY won the totem quest!"+ChatColor.RED+" YOU ARE ALL"+ChatColor.BOLD+" MORTAL.");
                            
                     }//else
                     
                            event.clear();
                            questcount=0;
                 }},3000);
        }
        this.broadcastTitle(ChatColor.GOLD + "TOTEMSPAWN","Temporary Quest: "+x);
        this.lastevent=ChatColor.YELLOW+"Totemspawn: "+x;
        /**
         * Entity e=x.getWorld().spawnEntity(c,EntityType.PHANTOM); 
        ((Phantom)e).setSize(10);
        ((Phantom)e).setVisualFire(false);
        ((Phantom)e).setMaxHealth(150);
        ((Phantom)e).setHealth(150);
        ((Phantom)e).setGlowing(true);
        ((Phantom)e).setCustomName(ChatColor.GOLD+"LORD OF THE FEAST");
        ((Phantom)e).setCustomNameVisible(true);
         * 
         */
    }//breathOfLife

    public void doubleDip() throws Exception{
        this.broadcastTitle(ChatColor.GOLD + "GENEROSITY", "More Blocks Falling!");
        for(int x=0;x<=4800;x+=100){
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){public void run(){spawnAll();}},60+x);
        this.lastevent=ChatColor.YELLOW+"DoubleDip";
        }//for loop
        
    }//doubledip

    public void invasion() throws Exception{
        this.invasion(((int)(Math.random()*6)));
        this.lastevent=ChatColor.RED+"--INVASION--";
    }//invasion
    
    public void invasion(int invanum) throws Exception{
        EntityType main,supp,rare,boss;
        String type;
        switch(invanum){
            default:
                main=EntityType.COW;
                supp=EntityType.CHICKEN;
                rare=EntityType.SHEEP;
                boss=EntityType.HORSE;
                type="Grassy";
                break;
            case 1:
                main=EntityType.WITHER_SKELETON;
                supp=EntityType.BLAZE;
                rare=EntityType.ZOGLIN;
                boss=EntityType.PIGLIN;
                type="Nether";
                break;
            case 2:
                main=EntityType.ENDERMAN;
                supp=EntityType.ENDERMITE;
                rare=EntityType.SHULKER;
                boss=EntityType.ENDERMAN;
                type="End";
                break;
            case 3:
                main=EntityType.GUARDIAN;
                supp=EntityType.AXOLOTL;
                rare=EntityType.PUFFERFISH;
                boss=EntityType.ELDER_GUARDIAN;
                type="Ocean";
                break;
            case 4:
                main=EntityType.SPIDER;
                supp=EntityType.SILVERFISH;
                rare=EntityType.CAVE_SPIDER;
                boss=EntityType.SILVERFISH;
                type="Arthropod";
                break;
            case 5:
                main=EntityType.VINDICATOR;
                supp=EntityType.PILLAGER;
                rare=EntityType.RAVAGER;
                boss=EntityType.ILLUSIONER;
                type="Pillager";
                break;
        }//switch
        this.broadcastTitle(ChatColor.RED + "INVASION", type+" invasion!");
        for(Player p:this.getServer().getOnlinePlayers()){
            p.getWorld().spawnEntity(this.invasionLoc,supp);
        }//for loop
        int l=this.getServer().getOnlinePlayers().size()/2;
        if(l>4)
            l=4;
        if(l>=0)
            l=1;
        for(int x=0;x<1200*l;x+=600)
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){public void run(){
                for(int x=((int)(Math.random()*3+4));x>=0;x--)
                    invasionLoc.getWorld().spawnEntity(invasionLoc,main);
                invasionLoc.getWorld().spawnEntity(invasionLoc,rare);
                getServer().broadcastMessage(ChatColor.RED + "Invasion Wave Spawning!");
                    
            }},x);
        //for
        
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){public void run(){
                broadcastTitle(ChatColor.RED + "INVASION", "BOSS WAVE!");
                 for(int x=((int)(Math.random()*3+8));x>=0;x--)
                    invasionLoc.getWorld().spawnEntity(invasionLoc,main);
                invasionLoc.getWorld().spawnEntity(invasionLoc,rare);
                invasionLoc.getWorld().spawnEntity(invasionLoc,rare);
                Entity e=invasionLoc.getWorld().spawnEntity(invasionLoc,boss);
                e.setCustomName(ChatColor.RED+""+ChatColor.BOLD+"BOSS");
                e.setCustomNameVisible(true);
            
            }},l*1200);
        
        
            
    }//invasion [int]

    public void cooked() throws Exception{ 
        this.broadcastTitle(ChatColor.GOLD + "FEAST", "Food Incoming!");
        for(Player p:this.getServer().getOnlinePlayers()){
            p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,16));
            Location l = new Location(p.getWorld(),p.getLocation().getBlockX()+0.5,150,p.getLocation().getBlockZ()+0.5);
            Material m;
            int r=(int)(Math.random()*3);
            if(r==0)
                m=Material.SMOKER;
            else if(r==1)
                m=Material.BLAST_FURNACE;
            else
                m=Material.FURNACE;
            p.getWorld().spawnFallingBlock(l, m,(byte)0);
        }//for loop
        Player x=this.randomPlayer();
        Location c=x.getLocation().clone();
        c.setY(100);
        Entity e=x.getWorld().spawnEntity(c,EntityType.PHANTOM); 
        ((Phantom)e).setSize(10);
        ((Phantom)e).setVisualFire(false);
        ((Phantom)e).setMaxHealth(150);
        ((Phantom)e).setHealth(150);
        ((Phantom)e).setGlowing(true);
        ((Phantom)e).setCustomName(ChatColor.GOLD+"LORD OF THE FEAST");
        ((Phantom)e).setCustomNameVisible(true);
        
        c.setY(90);
        
        for(int i=0;i<2;i++){
        Entity b=x.getWorld().spawnEntity(c,EntityType.PHANTOM); 
        ((Phantom)b).setSize(2);
        ((Phantom)b).setVisualFire(true);
        ((Phantom)b).setMaxHealth(20);
        ((Phantom)b).setHealth(20);
        ((Phantom)b).setCustomName(ChatColor.RED+"FEAST MINION");
        ((Phantom)b).setCustomNameVisible(true);
        }//for loop
        x.getWorld().setTime(14000);
        getServer().broadcastMessage(ChatColor.GOLD+"The Lord of the Feast has spawned above "+ChatColor.BLUE+x.getDisplayName());
        
        this.lastevent=ChatColor.YELLOW+"Feast";
    }//cooked
    
    public void starryNight() throws Exception{
        this.broadcastTitle(ChatColor.GOLD + "STARFALL", "Meteors are crashing from the sky!");

        for (int x = 0; x <= 2100; x += 300) {
            this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
                    try{
                    Player chosen = randomPlayer();
                    getServer().broadcastMessage(ChatColor.AQUA + "" + chosen.getName() + ChatColor.GOLD + " is seeing a meteor storm!");
                    for(int i=0;i<15;i++){
                    int x=(int)(Math.random()*5-2);
                    int z=(int)(Math.random()*5-2);
                    int y=(int)(Math.random()*5-2);
                    Material m;
                    if(x+z<-1)
                    m= Material.GLOWSTONE;
                    else if(x+z<1)
                    m= Material.SHROOMLIGHT;
                    else if(x+z==4)
                    m=Material.SEA_LANTERN;
                    else
                    m=Material.MAGMA_BLOCK;

                    chosen.getWorld().spawnFallingBlock(new Location(chosen.getWorld(), chosen.getLocation().getBlockX() + x + 0.5, 150+(3*y), chosen.getLocation().getBlockZ() + z + 0.5), m, (byte) 0);
                    }//for loop
                    }catch(Exception e){
                       getServer().getConsoleSender().sendMessage("[FF] FallingFalling event fail @starryNight.");
                    }
                    
                }//run()
            }, 100 + x);//runnable
            if(x/300>this.getServer().getOnlinePlayers().size()/2)
                x=2101;
        }//outermost for
        this.lastevent=ChatColor.YELLOW+"Starfall";
    }//starryNight

    //----------------------------------------------------
    public void save() throws FileNotFoundException, IOException {

        try {
            File other = new File(this.getDataFolder() + "/Falling/otherdata.txt");
            if (!other.exists()) {
                other.createNewFile();
            }
            PrintWriter lbs = new PrintWriter(other);
            String x=this.invasionLoc.getWorld().toString();
            String y="";
            boolean equals=false;
            for(int i=0;i<x.length()-1;i++){
                if(equals)
                    y=""+y+x.charAt(i);
                if(x.charAt(i)=='=')
                    equals=true;
            }
            
            lbs.println(y);
            lbs.println(ekiller);
            lbs.println(showscore);
            lbs.println(spawning);
            lbs.println(this.invasionLoc.getX());
            lbs.println(this.invasionLoc.getY());
            lbs.println(this.invasionLoc.getZ());
            
            lbs.close();
        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("[FF] FallingFalling failed to save to otherdata.");
        }
        //saving hashmaps
        try {
            File killfile = new File(this.getDataFolder() + "/Falling/kills.txt");
            if (!killfile.exists()) {
                killfile.createNewFile();
            }
            PrintWriter ks = new PrintWriter(killfile);
            for (String uuid : this.kills.keySet()) {
                ks.println(uuid);
                ks.println(kills.get(uuid));
            }
            ks.print("stop");
            ks.close();
        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("[FF] FallingFalling failed to save to kills.");
        }

        try {
            File blockfile = new File(this.getDataFolder() + "/Falling/blocksbroken.txt");
            if (!blockfile.exists()) {
                blockfile.createNewFile();
            }
            PrintWriter bs = new PrintWriter(blockfile);
            for (String uuid : this.blocksbroken.keySet()) {
                bs.println(uuid);
                bs.println(blocksbroken.get(uuid));
            }
            bs.print("stop");
            bs.close();

        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("[FF] FallingFalling failed to save to blocks.");
        }
    }//save

    public void loadBase() throws FileNotFoundException {
        Scanner lb = new Scanner(new File(this.getDataFolder() + "/Falling/otherdata.txt"));
        String world=lb.nextLine();
        ekiller = lb.nextLine();
        showscore = lb.nextBoolean();
        spawning = lb.nextBoolean();
        double x= lb.nextDouble();
        double y= lb.nextDouble();
        double z= lb.nextDouble();      
        
        this.invasionLoc=new Location(this.getServer().getWorld(world),x,y,z);
        lb.close();
    }//loadbase

    public void loadScore() throws IOException {
        Scanner ks = new Scanner(new File(this.getDataFolder() + "/Falling/kills.txt"));
        while (ks.hasNextLine()) {
            String s = ks.nextLine();
            if (!s.equals("stop")) {
                this.kills.put(s, Integer.parseInt(ks.nextLine()));
            }
        }//while
        ks.close();
        Scanner bs = new Scanner(new File(this.getDataFolder() + "/Falling/blocksbroken.txt"));
        while (bs.hasNextLine()) {
            String s = bs.nextLine();
            if (!s.equals("stop")) {
                this.blocksbroken.put(s, Integer.parseInt(bs.nextLine()));
            }
        }//while
        bs.close();
    }//loadscore

    public void updateScores() {
        if (ekiller.equals("")) {
            this.board.getTeam("enderdragon").setPrefix("The dragon lives on...");
        } else {
            this.board.getTeam("enderdragon").setPrefix(this.getServer().getOfflinePlayer(UUID.fromString(ekiller)).getName());
        }
        if (this.kills.size() == 0) {
            this.board.getTeam("topkills").setPrefix("No one");
        } else {
            int max = 0;
            String uuid = "";
            for (String key : this.kills.keySet()) {
                if (this.kills.get(key) > max) {
                    max = this.kills.get(key);
                    uuid = key;
                }
            }//for
            this.board.getTeam("topkills").setPrefix(this.getServer().getOfflinePlayer(UUID.fromString(uuid)).getName().toString() + ": " + max);
        }//else
        if (this.blocksbroken.size() == 0) {
            this.board.getTeam("topblocks").setPrefix("No one");
        } else {
            int max = 0;
            String uuid = "";
            for (String key : this.blocksbroken.keySet()) {
                if (this.blocksbroken.get(key) > max) {
                    max = this.blocksbroken.get(key);
                    uuid = key;
                }
            }//for
            this.board.getTeam("topblocks").setPrefix(this.getServer().getOfflinePlayer(UUID.fromString(uuid)).getName().toString() + ": " + max);
             this.board.getTeam("prevent").setPrefix(this.lastevent);
        }//else
        try {
            this.save();
        } catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("[FF] FallingFalling failed to save all data. if this error is seen there is a significant problem.");
        }
    }

    private void setupScore() {
        if (this.board.getObjective("FallingObj") != null) {
            this.board.getObjective("FallingObj").unregister();
        }
        if (this.board.getTeam("enderdragon") != null) {
            this.board.getTeam("enderdragon").unregister();
        }
        if (this.board.getTeam("topkills") != null) {
            this.board.getTeam("topkills").unregister();
        }
        if (this.board.getTeam("topblocks") != null) {
            this.board.getTeam("topblocks").unregister();
        }
        if (this.board.getTeam("prevent") != null) {
            this.board.getTeam("prevent").unregister();
        }

        Objective objective = this.board.registerNewObjective("FallingObj", "dummy", "Error-Should not see this");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.UNDERLINE + "" + ChatColor.YELLOW + "OBJECTIVES");

        Score info1 = objective.getScore(ChatColor.GRAY + "Blocks spawn every 100 ticks.");
        info1.setScore(8);
        Score info2 = objective.getScore(ChatColor.GRAY + "Scores update every 100 ticks.");
        info2.setScore(7);
        Score info3 = objective.getScore(ChatColor.GRAY + "Events happen every 18K ticks.");
        info3.setScore(6);

        Score space1 = objective.getScore(ChatColor.GREEN + "");
        space1.setScore(5);

        Score title1 = objective.getScore(ChatColor.GOLD + "Ender Dragon Slayer:");
        title1.setScore(4);
        Team ekill = board.registerNewTeam("enderdragon");
        ekill.addEntry(ChatColor.RED + "");
        ekill.setPrefix("The dragon lives on");
        objective.getScore(ChatColor.RED + "").setScore(3);

        Score space2 = objective.getScore(ChatColor.DARK_GREEN + "");
        space2.setScore(1);

        Score title2 = objective.getScore(ChatColor.GOLD + "Top Kills:");
        title2.setScore(0);
        Team topkill = board.registerNewTeam("topkills");
        topkill.addEntry(ChatColor.BLUE + "");
        topkill.setPrefix("None.");
        objective.getScore(ChatColor.BLUE + "").setScore(-1);

        Score space3 = objective.getScore(ChatColor.GRAY + "");
        space3.setScore(-2);

        Score space4 = objective.getScore(ChatColor.DARK_AQUA + "");
        space4.setScore(9);

        Score title3 = objective.getScore(ChatColor.GOLD + "Most Blocks Broken:");
        title3.setScore(-3);
        Team topblock = board.registerNewTeam("topblocks");
        topblock.addEntry(ChatColor.YELLOW + "");
        topblock.setPrefix("None.");
        objective.getScore(ChatColor.YELLOW + "").setScore(-4);
        
        Score space5 = objective.getScore(ChatColor.DARK_AQUA + "     ");
        space5.setScore(-5);

        Score title4 = objective.getScore(ChatColor.GOLD + "Last Event:");
        title4.setScore(-6);
        
        Team prevent = board.registerNewTeam("prevent");
        prevent.addEntry(ChatColor.BLACK + " ");
        prevent.setPrefix("None.");
        objective.getScore(ChatColor.BLACK + " ").setScore(-7);
        /*
        Team updating = board.registerNewTeam("teamname");
        updating.addEntry(ChatColor.RED+"");
        updating.setPrefix("Show Text");
        objective.getScore(ChatColor.RED+"").setScore(2);
         */
    }//setupscore

    public void spawnAll() {
        if(((int)(1000*Math.random()))==0){
            EntityType et=EntityType.COD;
            String name="codd";
            switch(((int)(15*Math.random()))){
                default:
                    et=EntityType.AXOLOTL;
                    name="Wtnaut";
                    break;
                case 1:
                    et=EntityType.ENDERMAN;
                    name=ChatColor.RED+"muzak23";
                    break;
                case 2:
                    et=EntityType.GOAT;
                    name="Goatmir";
                    break;
                case 3:
                    et=EntityType.CAT;
                    name="iiEthan";
                    break;
                case 4:
                    et=EntityType.BLAZE;
                    name="ABlazingEBoy";
                    break;
                case 5:
                    et=EntityType.WOLF;
                    name="SolarAwe";
                    break;
                case 6:
                    et=EntityType.WOLF;
                    name="TheCounterWolf";
                    break;
                case 7:
                    et=EntityType.BAT;
                    name="MarioFoli";
                    break;
                case 8:
                    et=EntityType.BEE;
                    name=ChatColor.RED+"(STINKY)"+ChatColor.GOLD+"Cheese_Addict";
                    break;
                case 9:
                    et=EntityType.CHICKEN;
                    name="FeralWaffle";
                    break;
                case 10:
                    et=EntityType.PHANTOM;
                    name="Bomo";
                    break;
                case 11:
                    et=EntityType.PARROT;
                    name="Brainytigy";
                    break;
                case 12:
                    et=EntityType.FOX;
                    name=ChatColor.RED+"Vilhu";
                    break;
                case 13:
                    et=EntityType.HORSE;
                    name="Dasckepur";
                    break;
                case 14:
                    et=EntityType.COW;
                    name="Xiaonoot";
                    break;
            }
            if(this.getServer().getOnlinePlayers().size()>0){
                try{
                Location l=this.randomPlayer().getLocation().clone();
                Entity e=l.getWorld().spawnEntity(l,et);
                e.setCustomName(ChatColor.YELLOW+""+name);
                e.setCustomNameVisible(true);    
                }catch(Exception e){
                    
                }
            }
        }//random easter egg
        if (this.spawning) {
            this.getServer().getOnlinePlayers().forEach((Player player) -> {
                if(!checkVanished(player)){
                    Location l = player.getLocation().clone();
                    l.setY(100.00);
                    l.setX(l.getBlockX() + ((int) (Math.random() * 11 -5)) + 0.5);
                    l.setZ(l.getBlockZ() + ((int) (Math.random() * 11 -5)) + 0.5);
                    Material m = getMaterial();
                    if (m == Material.SPAWNER) {
                        l.setY(60.0);
                        for (int x = 0; x < 8; x++) {
                            player.getWorld().spawnEntity(l, EntityType.BLAZE);
                        }
                        this.message = 3;
                    } else if (m == Material.LECTERN) {
                        l.setY(player.getLocation().getY());
                        for (int x = 0; x < 8; x++) {
                            player.getWorld().spawnEntity(l, EntityType.VILLAGER);
                        }
                        if (this.message < 2) {
                            this.message = 2;
                        }
                    } else {
                        player.getWorld().spawnFallingBlock(l, m, (byte) 0);
                    }
                }
            });
            switch (this.message) {
                case 3:
                    this.getServer().broadcastMessage(ChatColor.GOLD + "Blazes have spawned!");
                    break;
                case 2:
                    this.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "Villagers have spawned!");
                    break;
                case 1:
                    this.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "An extremely rare block has spawned!");
                    break;
                default:
                    break;
            }
            this.message = 0;
        }

    }

    @Override
    public void onDisable() {
    }

    private Material getMaterial() {
        int x = (int) (Math.random() * 1200);
        if (x ==0 && this.message == 0) {
            this.message = 1;
        }
        if (x == 0) {
            return Material.BEDROCK;
        } else if (x == 1) {
            return Material.BEACON;
        } else if (x == 2) {
            return Material.CONDUIT;
        } else if (x == 3 || x == 4) {
            return Material.NETHERITE_BLOCK;
        } else if (x == 5 || x == 6) {
            return Material.ENCHANTING_TABLE;
        } else if (x == 7 || x == 8) {
            return Material.CRIMSON_HYPHAE;
        } else if (x == 9 || x == 10) {
            return Material.WARPED_HYPHAE;
        } else if (x == 11 || x == 12) {
            return Material.SPONGE;
        } else if (x <= 16) {
            return Material.ANCIENT_DEBRIS;
        } else if (x <= 21) {
            return Material.OBSIDIAN;
        } else if (x <= 26) {
            return Material.IRON_BLOCK;
        } else if (x <= 30) {
            return Material.DIAMOND_BLOCK;
        } else if (x == 31) { 
            return Material.SPAWNER;
        } else if (x <= 36) {
            return Material.GOLD_BLOCK;
        } else if (x <= 41) {
            return Material.COAL_BLOCK;
        } else if (x <= 53) {
            return Material.PODZOL;
        } else if (x <= 65) {
            return Material.DIAMOND_ORE;
        } else if (x <= 77) {
            return Material.NETHER_QUARTZ_ORE;
        } else if (x <= 89) {
            return Material.END_STONE;
        } else if (x <= 101) {
            return Material.LAPIS_ORE;
        } else if (x <= 112) {
            return Material.CAULDRON;
        } else if (x <= 124) {
            return Material.ICE;
        } else if (x == 125) {
            return Material.LECTERN;
        } else if (x <= 137) {
            return Material.COPPER_BLOCK;
        } else if (x <= 149) {
            return Material.GLOWSTONE;
        } else if (x <= 161) {
            return Material.BOOKSHELF;
        } else if (x <= 173) {
            return Material.PURPUR_BLOCK;
        } else if (x <= 185) {
            return Material.SHULKER_BOX;
        } else if (x <= 205) {
            return Material.ANDESITE;
        } else if (x <= 225) {
            return Material.IRON_ORE;
        } else if (x <= 245) {
            return Material.GOLD_ORE;
        } else if (x <= 265) {
            return Material.NETHERRACK;
        } else if (x <= 285) {
            return Material.MYCELIUM;
        } else if (x <= 305) {
            return Material.DARK_OAK_LOG;
        } else if (x <= 315) {
            return Material.ACACIA_LOG;
        } else if (x <= 335) {
            return Material.WHITE_WOOL;
        } else if (x <= 355) {
            return Material.OAK_LEAVES;
        } else if (x <= 375) {
            return Material.BLACKSTONE;
        } else if (x <= 395) {
            return Material.DEEPSLATE;
        } else if (x <= 405) {
            return Material.GRAVEL;
        } else if (x <= 425) {
            return Material.PRISMARINE;
        } else if (x <= 445) {
            return Material.RED_SAND;
        } else if (x <= 450) {
            return Material.GILDED_BLACKSTONE;
        } else if (x <= 510) {
            return Material.SAND;
        } else if (x <= 550) {
            return Material.COPPER_ORE;
        } else if (x <= 565) {
            return Material.IRON_ORE;
        } else if (x <= 570) {
            return Material.DIAMOND_ORE;
        } else if (x <= 630) {
            return Material.BIRCH_LOG;
        } else if (x <= 690) {
            return Material.COBBLESTONE;
        } else if (x <= 750) {
            return Material.SPRUCE_LOG;
        } else if (x <= 840) {
            return Material.GRASS_BLOCK;
        } else if (x <= 930) {
            return Material.DIRT;
        } else if (x <= 1020) {
            return Material.COAL_ORE;
        } else if (x <= 1110) {
            return Material.STONE;
        }

        return Material.OAK_LOG;
    }

}
