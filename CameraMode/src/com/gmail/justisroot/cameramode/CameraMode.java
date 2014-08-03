package com.gmail.justisroot.cameramode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;


public class CameraMode extends JavaPlugin {
	
	//##########################//
	//######- Data Store -######//
	//##########################//
	
	public ArrayList<String> flyplayers = new ArrayList<String>();
	public HashMap<String, Integer> fireticks = new HashMap<String, Integer>();
	public HashMap<String, Integer> breath = new HashMap<String, Integer>();
	public ArrayList<String> pause = new ArrayList<String>();
	public List<String> allowedcmds = getConfig().getStringList("CameraMode.PlayersInCM.AllowedCommands");
	public HashMap<String, Location> locations = new HashMap<String, Location>();
	public ArrayList<String> tre = new ArrayList<String>();
	public HashMap<String, List<PotionEffect>> effects = new HashMap<String, List<PotionEffect>>();
	public HashMap<String, Vector> vel = new HashMap<String, Vector>();
	public HashMap<String, Entity> mobs = new HashMap<String, Entity>();
	public HashMap<String, Integer> pvpTimer = new HashMap<String, Integer>();
	public HashMap<String, Float> falldistance = new HashMap<String, Float>();
	public HashMap<UUID, Integer> coolDown = new HashMap<UUID, Integer>();
	public List<String> updates;
	
	//###### - Objects - ######//
	

	String reason = "You are in CameraMode!";


	public ArrayList<String> commands = new ArrayList<String>();
	
	
	//############################//
	//#########- Start - #########//
	//############################//
	
	@SuppressWarnings("unused")
	public void onEnable() {
		getConfig().options().copyHeader(true);
		getConfig().options().copyDefaults(true);
		saveConfig();
		if (getConfig().getBoolean("CameraMode.Enabled") == false) {
			getLogger().info("Plugin Disable Setting Detected...");
			getServer().getPluginManager().disablePlugin(this);
		}
		getServer().getPluginManager().registerEvents(new Events(this), this);
		PluginDescriptionFile pdfFile = this.getDescription();
		commands.add("camera");
		commands.add("cameramode");
		for (String commands : this.commands){
			getCommand(commands).setExecutor(new Commands(this));
		}
		getLogger().info(pdfFile.getName() + " v" + pdfFile.getVersion() + " has been enabled"); 
		if (getConfig().getBoolean("CameraMode.Updates.AutoUpdate") == true){
			Updater updater = new Updater(this, 80542, getFile(), Updater.UpdateType.DEFAULT, true);
		}else if (getConfig().getBoolean("CameraMode.Updates.NotifyOps") == true){
			Updater updater = new Updater(this, 80542, getFile(), Updater.UpdateType.NO_DOWNLOAD, true);
			if (updater.getResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE) && updater.getLatestType().toString().equalsIgnoreCase("release")) {
				updates.add(updater.getLatestFileLink().toString());
			}
		}else{
		}
	}
	public void onDisable(){
		reloadConfig();
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " v" + pdfFile.getVersion() + " has been disabled");
		updates.clear();
	}
	
	//#############################//
	//########- Booleans -#########//
	//#############################//
	
	public boolean commandIsWhitelisted(String playerCmd) {
        playerCmd = playerCmd.replace("/", "");
        for(String allowedCmd : this.allowedcmds) {
                allowedCmd = allowedCmd.replace("/", "");
                if(playerCmd.startsWith(allowedCmd)) return true;
        }
        return false;
	}
	public boolean isInt(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}