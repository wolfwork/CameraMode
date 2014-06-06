package com.gmail.justisroot.cameramode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventPriority;
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
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CameraMode extends JavaPlugin implements Listener {
	public ArrayList<String> flyplayers = new ArrayList<String>();
	public HashMap<String, Integer> fireticks = new HashMap<String, Integer>();
	public HashMap<String, Integer> breath = new HashMap<String, Integer>();
	public ArrayList<String> pause = new ArrayList<String>();
	public List<String> allowedcmds = this.getConfig().getStringList("CameraMode.PlayersInCM.AllowedCommands");
	public HashMap<String, Location> locations = new HashMap<String, Location>();
	public ArrayList<String> tre = new ArrayList<String>();
	public HashMap<String, List<PotionEffect>> effects = new HashMap<String, List<PotionEffect>>();
	public HashMap<String, Vector> vel = new HashMap<String, Vector>();
	String reason = "You are in CameraMode!";
	
	@Override
	public void onEnable() {
		getConfig().options().header("http://dev.bukkit.org/bukkit-plugins/cameramode/pages/config-yml-explained/");
		getConfig().options().copyHeader(true);
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
	public void onPlayerLeave(PlayerQuitEvent w) {
		
	}
	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
	 if (!e.getPlayer().hasPermission("cameramode.fly") && (!(flyplayers.contains(e.getPlayer().getUniqueId().toString()))) && e.getPlayer().getGameMode() == (GameMode.SURVIVAL)) {
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
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
			e.setCancelled(true);
		}
    }
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
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
					e.getEntity().setFireTicks(0);
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
	public void onEntityTarget(EntityTargetLivingEntityEvent e){
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
	 public void onPlayerGameModeChange(final PlayerGameModeChangeEvent e) {
		final String player = e.getPlayer().getUniqueId().toString();
		if (flyplayers.contains(player)){
			Location loc = locations.get(e.getPlayer().getUniqueId().toString());
			if (!pause.contains(e.getPlayer().getUniqueId().toString())) {
				if (e.getPlayer().getWorld() == loc.getWorld()) {
					flyplayers.remove(player);
					e.getPlayer().teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
					CameraMode pInst = this;
					pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
					public void run() {
					e.getPlayer().sendMessage(ChatColor.RED + "You are no longer in CameraMode!");
					flyplayers.remove(player);
					}
					}, 5L);
					int Fireup = fireticks.get(e.getPlayer().getUniqueId().toString()).intValue();
					e.getPlayer().setFireTicks(Fireup);
					if (e.getNewGameMode() == GameMode.SURVIVAL) {
					e.getPlayer().setAllowFlight(false);
					}
				}
			}else{
				CameraMode pInst = this;
				pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
				public void run() {
					e.getPlayer().setAllowFlight(true);
				}
				}, 5L);
				e.setCancelled(true);
			}
		}
	}
	 @EventHandler
	 public void onPlayerChangeWorld(final PlayerChangedWorldEvent e) {
		 if (getConfig().getBoolean("CameraMode.PlayersInCM.CanChangeWorlds") == false) {
			 if (flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
				Location loc = locations.get(e.getPlayer().getUniqueId().toString());
				e.getPlayer().teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
				if (!(e.getFrom() == loc.getWorld())) {
					e.getPlayer().sendMessage(ChatColor.RED +  "You Cannot Change Worlds While In CameraMode!");
				}
			}
		}else{
			CameraMode pInst = this;
			pause.add(e.getPlayer().getUniqueId().toString());
			pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
			public void run() {
				pause.remove(e.getPlayer().getUniqueId().toString());
			}
			}, 50L);
		}
	
    } 
	 @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProccess(PlayerCommandPreprocessEvent e) {
        if (getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == false) {
                if (flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
                        if (commandIsWhitelisted(e.getMessage()) == false) {
                                e.setCancelled(true);
                                e.getPlayer().sendMessage(ChatColor.RED + reason);
                                return;
                        }
                }
        }
    }

	 private boolean commandIsWhitelisted(String playerCmd) {
        playerCmd = playerCmd.replace("/", "");
        for(String allowedCmd : this.allowedcmds) {
                allowedCmd = allowedCmd.replace("/", "");
                if(playerCmd.startsWith(allowedCmd)) return true;
        }
        return false;
	}
	 
	 /*
	  * *****************************
	  *            Commands
	  * *****************************
	  */
	 
	@SuppressWarnings({ "deprecation" })
	public boolean onCommand(CommandSender sender, Command cmd, String StringLabel, String[] args) {
	if (cmd.getName().equalsIgnoreCase("camera")) {
		if (args.length == 0) {
			if (sender.hasPermission("cameramode.cm") || (sender.hasPermission("cameramode.reload")) || (sender.hasPermission("cameramode.camera")) || (sender.hasPermission("cameramode.config"))) {
				sender.sendMessage(ChatColor.AQUA + "__CameraMode Commands__");
				sender.sendMessage(ChatColor.DARK_AQUA + "/Camera" + ChatColor.GRAY + "  - Displays This Help List");
			}else{
				sender.sendMessage(ChatColor.RED + "You do not have permission.");
			}
			if (sender.hasPermission("cameramode.cm")){
				sender.sendMessage(ChatColor.DARK_AQUA + "/CameraMode" + ChatColor.GRAY + "  - Enables CameraMode");
			}
			if (sender.hasPermission("cameramode.config")) {
				sender.sendMessage(ChatColor.DARK_AQUA + "/Camera config" + ChatColor.GRAY + "  - Configure CameraMode");
			}
			if (sender.hasPermission("cameramode.reload")) {
				sender.sendMessage(ChatColor.DARK_AQUA + "/Camera reload" + ChatColor.GRAY + "  - Reloads Configuration");
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
			}else if (args[0].equalsIgnoreCase("config")) {
				if (sender.hasPermission("cameramode.config") || !(sender instanceof Player)) {
				    StringBuilder cl = new StringBuilder();
				    for(String item : allowedcmds) {
				            cl.append(" /").append(item).append(",");
				    }
					sender.sendMessage(ChatColor.AQUA + "__CameraMode Configuration Options__");
					sender.sendMessage(ChatColor.GRAY + "[1] " + ChatColor.DARK_GRAY + "Enabled: " + ChatColor.RED + getConfig().getString("CameraMode.Enabled"));
					sender.sendMessage(ChatColor.GRAY + "[2] " + ChatColor.DARK_AQUA + "CameraModed Players are Invincible: " + ChatColor.RED + getConfig().getString("CameraMode.PlayersInCM.AreInvincible"));
					sender.sendMessage(ChatColor.GRAY + "[3] " + ChatColor.DARK_AQUA + "CameraModed Players are Vanished: " + ChatColor.RED + getConfig().getString("CameraMode.PlayersInCM.AreVanished"));
					sender.sendMessage(ChatColor.GRAY + "[4] " + ChatColor.DARK_AQUA + "CameraModed Players can Change Worlds " + ChatColor.RED + getConfig().getString("CameraMode.PlayersInCM.CanChangeWorlds"));
					sender.sendMessage(ChatColor.GRAY + "[5] " + ChatColor.DARK_AQUA + "CameraModed Players can Use Commands " + ChatColor.RED + getConfig().getString("CameraMode.PlayersInCM.CanUseCommands"));
					if (getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == (false)){
						sender.sendMessage(ChatColor.DARK_GRAY + "Add A Command With" + ChatColor.GRAY + " /camera config addcmd </newcommand>");
						sender.sendMessage(ChatColor.GRAY + "Allowed Commands:" + ChatColor.AQUA + (cl.toString()));
					}
				}else if (!sender.hasPermission("cameramode.config") && sender instanceof Player){
					sender.sendMessage(ChatColor.RED + "You do not have permission.");
				}
			}else{
				if (sender.hasPermission("cameramode.cm") || (sender.hasPermission("cameramode.reload")) && (sender.hasPermission("cameramode.camera"))) {
					sender.sendMessage(ChatColor.AQUA + "__CameraMode Commands__");
					sender.sendMessage(ChatColor.DARK_AQUA + "/Camera" + ChatColor.GRAY + "  - Displays This Help List");
				} 
				if (sender.hasPermission("cameramode.cm")){
					sender.sendMessage(ChatColor.DARK_AQUA + "/CameraMode" + ChatColor.GRAY + "  - Enables CameraMode");
				}
				if (sender.hasPermission("cameramode.config")) {
					sender.sendMessage(ChatColor.DARK_AQUA + "/Camera config" + ChatColor.GRAY + "  - Configure CameraMode");
				}
				if (sender.hasPermission("cameramode.reload")) {
					sender.sendMessage(ChatColor.DARK_AQUA + "/Camera reload" + ChatColor.GRAY + "  - Reloads Configuration");
				}
			}
		}else if (args.length == 2){
			if (args[0].equalsIgnoreCase("config")) {   
				if (sender.hasPermission("cameramode.config") || !(sender instanceof Player)) { 
					sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
					sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
					if (getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == (false)){
						sender.sendMessage(ChatColor.GRAY + "To add a command to the 'AvailableCommands' list: " + ChatColor.GRAY + "/camera config addcmd </newcommand>");
					}
				}else if (!sender.hasPermission("cameramode.config") && sender instanceof Player){
					sender.sendMessage(ChatColor.RED + "You do not have permission.");
				}
			}
		}else if (args.length == 3){
			if (args[0].equalsIgnoreCase("config")) {
				if (sender.hasPermission("cameramode.config") || !(sender instanceof Player)) {
					if (args[1].equalsIgnoreCase("1")) {
						if (args[2].equalsIgnoreCase("true")){
							if (getConfig().getString("CameraMode.Enabled").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								this.getConfig().set("CameraMode.Enabled", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (getConfig().getString("CameraMode.Enabled").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								this.getConfig().set("CameraMode.Enabled", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "to modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
						
					}else if (args[1].equalsIgnoreCase("2")){
						if (args[2].equalsIgnoreCase("true")){
							if (getConfig().getString("CameraMode.PlayersInCM.AreInvincible").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								getConfig().set("CameraMode.PlayersInCM.AreInvincible", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (getConfig().getString("CameraMode.PlayersInCM.AreInvincible").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								getConfig().set("CameraMode.PlayersInCM.AreInvincible", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
					}else if (args[1].equalsIgnoreCase("3")){
						if (args[2].equalsIgnoreCase("true")){
							if (getConfig().getString("CameraMode.PlayersInCM.AreVanished").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								getConfig().set("CameraMode.PlayersInCM.AreVanished", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (getConfig().getString("CameraMode.PlayersInCM.AreVanished").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								getConfig().set("CameraMode.PlayersInCM.AreVanished", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
					}else if (args[1].equalsIgnoreCase("4")){
						if (args[2].equalsIgnoreCase("true")){
							if (getConfig().getString("CameraMode.PlayersInCM.CanChangeWorlds").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								getConfig().set("CameraMode.PlayersInCM.CanChangeWorlds", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (getConfig().getString("CameraMode.PlayersInCM.CanChangeWolrds").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								getConfig().set("CameraMode.PlayersInCM.CanChangeWorlds", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "to modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
					}else if (args[1].equalsIgnoreCase("5")){
						if (args[2].equalsIgnoreCase("true")){
							if (getConfig().getString("CameraMode.PlayersInCM.CanUseCommands").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								getConfig().set("CameraMode.PlayersInCM.CanUseCommands", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (getConfig().getString("CameraMode.PlayersInCM.CanUseCommands").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								getConfig().set("CameraMode.PlayersInCM.CanUseCommands", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "to modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
					}else if (args[1].equalsIgnoreCase("addcmd")){
	                    if (commandIsWhitelisted(args[2]) == true) {
	                    	sender.sendMessage(ChatColor.GRAY + "That command is already listed");
	                    }else{
	                    	allowedcmds.add(args[2].replaceFirst("/", ""));
	                    	getConfig().set("CameraMode.PlayersInCM.AllowedCommands", allowedcmds);
							sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
							saveConfig();
	                    }
					}else{
						sender.sendMessage(ChatColor.AQUA + "to modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
						if (getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == (false)){
							sender.sendMessage(ChatColor.GRAY + "To add a command to the 'AvailableCommands' list: " + ChatColor.GRAY + "/camera config addcmd <command>");
						}
						sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
					}
				}else if (!sender.hasPermission("cameramode.config") || sender instanceof Player){
					sender.sendMessage(ChatColor.RED + "You do not have permission.");
				}
			}
		}else{
			if (sender.hasPermission("cameramode.cm") || (sender.hasPermission("cameramode.reload")) || (sender.hasPermission("cameramode.camera")) || sender.hasPermission("cameramode.config")) {
				sender.sendMessage(ChatColor.RED + "Too many arguments.");
			}
		}
	}
	if (cmd.getName().equalsIgnoreCase("cameramode")) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				if (sender.hasPermission("cameramode.cm")) {
					Player p = (Player) sender;
					final String target = ((Player) sender).getUniqueId().toString();
					if(flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.SURVIVAL)) {
						((Player) sender).setAllowFlight(false);
						Location loc = locations.get(p.getUniqueId().toString());
						p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						CameraMode pInst = this;
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
							public void run() {
								flyplayers.remove(target);
							}
						}, 5L);
						((Player) sender).addPotionEffects(effects.get(((Player) sender).getUniqueId().toString()));
						sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						int Fireup = fireticks.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setFireTicks(Fireup);
						int air = breath.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setRemainingAir(air);
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(p);
						}
					}else if (flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.CREATIVE)) {
						CameraMode pInst = this;
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
						public void run() {
						flyplayers.remove(target);
						}
						}, 5L);
						int Fireup = fireticks.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setFireTicks(Fireup);
						int air = breath.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setRemainingAir(air);
						Location loc = locations.get(p.getUniqueId().toString());
						p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						((Player) sender).addPotionEffects(effects.get(((Player) sender).getUniqueId().toString()));
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(p);
						}
					}else{
						flyplayers.add(target);
						locations.put(p.getUniqueId().toString(), p.getLocation());
						((Player) sender).setAllowFlight(true);
						fireticks.put(((Player) sender).getUniqueId().toString(), ((Player) sender).getFireTicks());
						((Player) sender).setFireTicks(0);
						breath.put(((Player) sender).getUniqueId().toString(), ((Player) sender).getRemainingAir());
						effects.put(((Player) sender).getUniqueId().toString(), (List<PotionEffect>) ((Player) sender).getActivePotionEffects());
						for (PotionEffect effect : ((Player) sender).getActivePotionEffects())
					        ((Player) sender).removePotionEffect(effect.getType());
						sender.sendMessage(ChatColor.GOLD + "You are now in CameraMode!");
						if (getConfig().getBoolean("CameraMode.PlayersInCM.AreVanished") == true) {
							for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
								pl.hidePlayer(p);
							}
						}
					}
				}else{
					sender.sendMessage(ChatColor.RED + "You do not have permission!");
				}
			}else{
				getLogger().info("Usage: /cameramode <player> ");
			}
		}else if (args.length == 1) {
			if (sender instanceof Player) {
				if (sender.hasPermission("cameramode.other") || args[0].equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
					if (Bukkit.getServer().getPlayerExact(args[0]) != null){
						final Player targetPlayer = Bukkit.getServer().getPlayerExact(args[0]);
						final String superTarget = targetPlayer.getUniqueId().toString();
						if(flyplayers.contains(superTarget) && (targetPlayer).getGameMode() == (GameMode.SURVIVAL)) {
							targetPlayer.setAllowFlight(false);
							Location loc = locations.get(superTarget);
							targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
							CameraMode pInst = this;
							pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
							public void run() {
							flyplayers.remove(superTarget);
							}
							}, 5L);
							targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
							targetPlayer.addPotionEffects(effects.get(targetPlayer.getUniqueId().toString()));
							if (superTarget.equalsIgnoreCase(targetPlayer.getUniqueId().toString())) {
								sender.sendMessage(ChatColor.GRAY + "Try just /cm next time ;)");
							}else{
								targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " + sender.getName().toString());
								sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
							}
							int Fireup = fireticks.get(superTarget).intValue();
							targetPlayer.setFireTicks(Fireup);
							int air = breath.get(superTarget).intValue();
							(targetPlayer).setRemainingAir(air);
							for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
								pl.showPlayer(targetPlayer);
							}
						}else if (flyplayers.contains(superTarget) && targetPlayer.getGameMode() == (GameMode.CREATIVE)) {
							CameraMode pInst = this;
							pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
								public void run() {
									flyplayers.remove(targetPlayer);
								}
							}, 5L);
							int Fireup = fireticks.get(superTarget).intValue();
							(targetPlayer).setFireTicks(Fireup);
							int air = breath.get(superTarget).intValue();
							(targetPlayer).setRemainingAir(air);
							Location loc = locations.get(superTarget);
							targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
							targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
							targetPlayer.addPotionEffects(effects.get(targetPlayer.getUniqueId().toString()));
							if (superTarget.equalsIgnoreCase(targetPlayer.getUniqueId().toString())) {
								sender.sendMessage(ChatColor.GRAY + "Try just /cm next time ;)");
							}else{
								targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " + sender.getName().toString());
								sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
							}
							for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
								pl.showPlayer(targetPlayer);
							}
						}else{
						flyplayers.add(superTarget);
						locations.put(superTarget, targetPlayer.getLocation());
						targetPlayer.setAllowFlight(true);
						fireticks.put(superTarget, targetPlayer.getFireTicks());
						targetPlayer.setFireTicks(0);
						breath.put(superTarget, targetPlayer.getRemainingAir());
						effects.put(superTarget, (List<PotionEffect>) targetPlayer.getActivePotionEffects());
						for (PotionEffect effect : targetPlayer.getActivePotionEffects())
					        targetPlayer.removePotionEffect(effect.getType());
						targetPlayer.sendMessage(ChatColor.GOLD + "You are now in CameraMode!");
							if (superTarget.equalsIgnoreCase(superTarget)) {
								sender.sendMessage(ChatColor.GRAY + "Try just /cm next time ;)");
							}else{
								targetPlayer.sendMessage(ChatColor.GOLD + "Compliments of " + sender.getName().toString());
								sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has successfully been put in CameraMode");
							}
							if (getConfig().getBoolean("CameraMode.PlayersInCM.AreVanished") == true) {
								for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
									pl.hidePlayer(targetPlayer);
								}
							}
						}
					}else{
						sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
					}
				}else{
					sender.sendMessage(ChatColor.RED + "You may only CameraMode yourself!");
				}
			}else{
				if (Bukkit.getServer().getPlayerExact(args[0]) != null){
					final Player targetPlayer = Bukkit.getServer().getPlayerExact(args[0]);
					final String superTarget = targetPlayer.getUniqueId().toString();
					if (!(flyplayers.contains(superTarget))){
						flyplayers.add(superTarget);
						locations.put(superTarget, targetPlayer.getLocation());
						targetPlayer.setAllowFlight(true);
						fireticks.put(superTarget, targetPlayer.getFireTicks());
						targetPlayer.setFireTicks(0);
						breath.put(superTarget, targetPlayer.getRemainingAir());
						effects.put(superTarget, (List<PotionEffect>) targetPlayer.getActivePotionEffects());
						for (PotionEffect effect : targetPlayer.getActivePotionEffects())
					        targetPlayer.removePotionEffect(effect.getType());
						sender.sendMessage(ChatColor.GOLD + args[0] + " has successfully been put in CameraMode");
						targetPlayer.sendMessage(ChatColor.GOLD + "You are now in CameraMode!");
						targetPlayer.sendMessage(ChatColor.GOLD + "Compliments of " + ChatColor.GRAY + ChatColor.ITALIC + "CONSOLE.");
						if (getConfig().getBoolean("CameraMode.PlayersInCM.AreVanished") == true) {
							for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
								pl.hidePlayer(targetPlayer);
							}
						}
					}else if (flyplayers.contains(superTarget) && (targetPlayer).getGameMode() == (GameMode.SURVIVAL)) {
						targetPlayer.setAllowFlight(false);
						Location loc = locations.get(superTarget);
						targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						CameraMode pInst = this;
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
						public void run() {
						flyplayers.remove(superTarget);
						}
						}, 5L);
						sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
						targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " +ChatColor.GRAY + ChatColor.ITALIC + "CONSOLE.");
						targetPlayer.addPotionEffects(effects.get(superTarget));
						int Fireup = fireticks.get(superTarget).intValue();
						targetPlayer.setFireTicks(Fireup);
						int air = breath.get(superTarget).intValue();
						(targetPlayer).setRemainingAir(air);
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(targetPlayer);
						}
					}else{
						CameraMode pInst = this;
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
							public void run() {
								flyplayers.remove(targetPlayer);
							}
						}, 5L);
						int Fireup = fireticks.get(superTarget).intValue();
						(targetPlayer).setFireTicks(Fireup);
						int air = breath.get(superTarget).intValue();
						(targetPlayer).setRemainingAir(air);
						Location loc = locations.get(superTarget);
						targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
						targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " +ChatColor.GRAY + ChatColor.ITALIC + "CONSOLE.");
						targetPlayer.addPotionEffects(effects.get(superTarget));
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(targetPlayer);
						}
					}
				}else{
					sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
				}
			}
		}else{
			sender.sendMessage(ChatColor.RED + "Usage: /cameramode <player>");
		}
	}
	return true;
	}
}

