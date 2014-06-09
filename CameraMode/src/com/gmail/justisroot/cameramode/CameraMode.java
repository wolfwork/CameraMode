package com.gmail.justisroot.cameramode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.gmail.justisroot.cameramode.Updater.UpdateType;

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
	public ArrayList<String> pvpTimer = new ArrayList<String>();
	
	//###### - Objects - ######//
	
	Player playerr;
	String reason = "You are in CameraMode!";
	Updater updater;
	Events event;
	Entity putMobs = mobs.put(playerr.getUniqueId().toString(), (Entity) playerr.getNearbyEntities(15, 15, 15));
	Entity getMobs = mobs.get(playerr.getUniqueId().toString());
	UpdateType biff = (getConfig().getBoolean("CameraMode.Updates.AutoUpdate")) ? Updater.UpdateType.DEFAULT : Updater.UpdateType.NO_DOWNLOAD;
	
	//############################//
	//#########- Start - #########//
	//############################//
	
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {
		getConfig().options().copyHeader(true);
		getConfig().options().copyDefaults(true);
		saveConfig();
		if (getConfig().getBoolean("CameraMode.Enabled") == false) {
			getLogger().info("Plugin Disable Setting Detected...");
			getServer().getPluginManager().disablePlugin(this);
		}
		getServer().getPluginManager().registerEvents(event, this);
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " v" + pdfFile.getVersion() + " has been enabled"); 
		Updater updater = new Updater(this, 80542, getFile(), biff, true);
	}
	@Override
	public void onDisable(){
		reloadConfig();
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " v" + pdfFile.getVersion() + " has been disabled");
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
}