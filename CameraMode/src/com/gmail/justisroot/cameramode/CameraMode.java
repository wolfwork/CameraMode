package com.gmail.justisroot.cameramode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

public class CameraMode extends JavaPlugin implements Listener 
{
	public ArrayList<String> flyplayers = new ArrayList<String>();
	public List<String> allowedcmds = this.getConfig().getStringList("CameraMode.PlayersInCM.AllowedCommands");
	public HashMap<String, Location> locations = new HashMap<String, Location>();
	String reason = "You are in CameraMode!";
	
	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		if (getConfig().getBoolean("CameraMode.Enabled") == false) {
			getLogger().info("Plugin Disable Setting Detected...");
			getServer().getPluginManager().disablePlugin(this);
		}
		getServer().getPluginManager().registerEvents(this, this);
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " v" + pdfFile.getVersion() + " has been enabled"); 
	}
	@Override
	public void onDisable(){
		reloadConfig();
		saveConfig();
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " v" + pdfFile.getVersion() + " has been disabled");
	}
	/*
	 * *************************
	 *       Event Handlers
	 * *************************
	 */
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player player = (Player) e.getDamager();
			if(flyplayers.contains(player.getUniqueId().toString())){
				e.setCancelled(true);
						player.sendMessage(ChatColor.RED + reason);
			}
		}
	}
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		ProjectileSource proj = e.getEntity().getShooter();
		if (proj instanceof Player){
			Player player =  (Player) proj;
			if (flyplayers.contains(player.getUniqueId().toString())){
			player.sendMessage(ChatColor.RED + reason);
			e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
			if(e.isCancelled() == false){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED + reason);
			}
		}
	}
	@EventHandler
	public void onPlayerGameModeChange(final PlayerGameModeChangeEvent e) {
		String player = e.getPlayer().getUniqueId().toString();
		if (flyplayers.contains(player)){
				Location loc = locations.get(e.getPlayer().getUniqueId().toString());
				if (e.getPlayer().getWorld() == loc.getWorld()) {
					flyplayers.remove(player);
					e.getPlayer().teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
					CameraMode pInst = this;
					pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
					public void run() {
					e.getPlayer().sendMessage(ChatColor.RED + "You are no longer in CameraMode!");
					}
					}, 2L);
					if (e.getNewGameMode() == GameMode.SURVIVAL) {
					e.getPlayer().setAllowFlight(false);
					}
				
			}
		}
	}
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player){
			Player player = (Player)e.getEntity();
			if (flyplayers.contains(player.getUniqueId().toString())){
			player.sendMessage(ChatColor.RED + reason);
			e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
	 if (e.getPlayer().hasPermission("cameramode.fly") && (!(flyplayers.contains(e.getPlayer().getUniqueId().toString()))) && e.getPlayer().getGameMode() == (GameMode.SURVIVAL)) {
		 e.getPlayer().setAllowFlight(false);
		  }
	 if (e.getPlayer().hasPermission("cameramode.fly") && (flyplayers.contains(e.getPlayer().getUniqueId().toString())) && e.getPlayer().getAllowFlight() == (false)) {
		 e.getPlayer().setAllowFlight(true);
	 }
	}
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
			e.getPlayer().sendMessage(ChatColor.RED + reason);
			e.setCancelled(true);
		}
    }
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (getConfig().getBoolean("CameraMode.PlayersInCM.AreInvincible") == true) {
			if (e.getEntity() instanceof Player) {
				if (flyplayers.contains(e.getEntity().getUniqueId().toString())) {
					e.setCancelled(true);
				}	
			}
		}
    }
	@EventHandler
	public void onPlayerexpChange(PlayerExpChangeEvent e) {
		if (flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
			e.setAmount(0);
		}
    } 
	@EventHandler
	public void onEntityTarget(EntityTargetEvent e){
		if (getConfig().getBoolean("CameraMode.PlayersInCM.AreInvincible") == true) {
			if (e.getTarget() instanceof Player) {
				if (flyplayers.contains(e.getTarget().getUniqueId().toString())){
					e.setCancelled(true);
					e.setTarget(null);
				}
			}
		}
	}
	 @EventHandler
	 public void onFoodLevelChange(FoodLevelChangeEvent e) {
		 if (e.getEntity() instanceof Player) {
			if (flyplayers.contains(e.getEntity().getUniqueId().toString())) {
				e.setCancelled(true);
			}
		}
	}
	 @EventHandler
	 public void onPlayerRegainHealth(EntityRegainHealthEvent e) {
		 if (e.getEntity() instanceof Player) {
			 if (flyplayers.contains(e.getEntity().getUniqueId().toString())) {
				 if (e.getRegainReason() == RegainReason.SATIATED) {
					 e.setCancelled(true); 
				 }
	        }
		 }
	}
	 @EventHandler
	 public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
		 if (getConfig().getBoolean("CameraMode.PlayersInCM.CanChangeWorlds") == false) {
			 if (flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
				Location loc = locations.get(e.getPlayer().getUniqueId().toString());
				e.getPlayer().teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
				if (!(e.getFrom() == loc.getWorld())) {
					e.getPlayer().sendMessage(ChatColor.RED +  "You Cannot Change Worlds While In CameraMode!");
				}
			 }
		}
	
    }
	 @EventHandler
	 public void onPlayerCommandPreProccess(PlayerCommandPreprocessEvent e) {
		 if (getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == false) {
			 if (flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
			 	for (String commands : allowedcmds) {
			 		if (!(e.getMessage().contains(commands))) {
			 			e.setCancelled(true);
			 			e.getPlayer().sendMessage(ChatColor.RED + reason);
			 			System.out.println(allowedcmds);
			 			return;
			 		}
			 	}
			}
		}
	}
	 /*
	  * *****************************
	  *            Commands
	  * *****************************
	  */
	public boolean onCommand(CommandSender sender, Command cmd, String StringLabel, String[] args)
	{
	if (cmd.getName().equalsIgnoreCase("cameramode")) {
		if (args.length == 0) {
			if (sender.hasPermission("cameramode.cameramode") || (sender.hasPermission("cameramode.reload")) || (sender.hasPermission("cameramode.camera"))) {
				sender.sendMessage(ChatColor.AQUA + "__CameraMode Commands__");
				sender.sendMessage(ChatColor.DARK_AQUA + "/cameramode");
			} 
			if (sender.hasPermission("cameramode.reload")){
				sender.sendMessage(ChatColor.DARK_AQUA + "/cameramode reload");
			}
			if (sender.hasPermission("cameramode.camera")) {
				sender.sendMessage(ChatColor.DARK_AQUA + "/camera");
			}
		}else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender instanceof Player) {
					if (sender.hasPermission("cameramode.reload")) {
						reloadConfig();
						sender.sendMessage(ChatColor.GREEN + "Config Reloaded");
						getLogger().info("Reloading Configuration...");
						if (getConfig().getBoolean("CameraMode.Enabled") == false) {
							getLogger().info("Plugin Disable Setting Detected...");
							getServer().getPluginManager().disablePlugin(this);
						}
					}else{
						sender.sendMessage(ChatColor.RED + "You do not have permission!");
					}
				}else{
					reloadConfig();
					getLogger().info("Config reloaded");
					if (getConfig().getBoolean("CameraMode.Enabled") == false) {
						getLogger().info("Plugin Disable Setting Detected...");
						getServer().getPluginManager().disablePlugin(this);
					}
				}
			}else if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.AQUA + "__CameraMode Commands__");
				if (sender.hasPermission("cameramode.cameramode")) {
					sender.sendMessage(ChatColor.DARK_AQUA + "/cameramode");
				} 
				if (sender.hasPermission("cameramode.reload")){
					sender.sendMessage(ChatColor.DARK_AQUA + "/cameramode reload");
				}
				if (sender.hasPermission("cameramode.camera")) {
					sender.sendMessage(ChatColor.DARK_AQUA + "/camera");
				}
			}
		}else{
				sender.sendMessage(ChatColor.RED + "You do not have permission!");
		}
	}
	if (cmd.getName().equalsIgnoreCase("camera")) {
		Player p = (Player) sender;
		if (args.length == 0) {
			if (sender instanceof Player) {
				if (sender.hasPermission("cameramode.camera")) {
					String target = ((Player) sender).getUniqueId().toString();
					if(flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.SURVIVAL)) {
						((Player) sender).setAllowFlight(false);
						Location loc = locations.get(p.getUniqueId().toString());
						p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						flyplayers.remove(target);
						sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(p);
						}
					}else if (flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.CREATIVE)) {
						flyplayers.remove(target);
						Location loc = locations.get(p.getUniqueId().toString());
						p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(p);
						}
					}else{
						flyplayers.add(target);
						locations.put(p.getUniqueId().toString(), p.getLocation());
						((Player) sender).setAllowFlight(true);
						sender.sendMessage(ChatColor.GOLD + "You are now in CameraMode!");
						if (getConfig().getBoolean("CameraMode.PlayersInCM.AreVanished") == true) {
							for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
								pl.hidePlayer(p);
							}
						}
					}
				}else{
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to CameraMode!");
				}
			}else{
				getLogger().info("Usage: /camera <player> ");
			}
		}else if (args.length == 1) {
			if (sender instanceof Player) {
				if (sender.hasPermission("camera.other")) {
					
				}
			}
		}
	}
	return true;
	}
}






