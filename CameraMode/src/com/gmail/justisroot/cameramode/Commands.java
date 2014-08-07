package com.gmail.justisroot.cameramode;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Commands implements CommandExecutor{
	
int ID;
	CameraMode main;

	public Commands(CameraMode plugin){
		this.main = plugin;
	}
	
	@SuppressWarnings({ "deprecation" })
	public boolean onCommand(final CommandSender sender, Command cmd, String StringLabel, String[] args) {
	if (cmd.getName().equalsIgnoreCase("camera")) {
		//######################//
		//#####- Help Box -#####//
		if (args.length == 0) { 
			if (sender.hasPermission("cameramode.cm") || (sender.hasPermission("cameramode.reload")) || (sender.hasPermission("cameramode.camera")) || (sender.hasPermission("cameramode.config"))) {
				sender.sendMessage(ChatColor.AQUA + "__CameraMode Commands__");
				sender.sendMessage(ChatColor.DARK_AQUA + "/Camera" + ChatColor.GRAY + "  - Displays main Help List");
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
				sender.sendMessage(ChatColor.DARK_AQUA + "/Camera reload" + ChatColor.GRAY + "  - Reloads the Configuration");
			}
		//########################//
		//#######- Reload -#######//
		}else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender instanceof Player) {
					if (sender.hasPermission("cameramode.reload")) {
						main.reloadConfig();
						sender.sendMessage(ChatColor.GREEN + "Config Reloaded");
						main.getLogger().info("Reloading Configuration...");
						if (main.getConfig().getBoolean("CameraMode.Enabled") == false) {
							main.getLogger().info("Plugin Disable Setting Detected...");
							main.getServer().getPluginManager().disablePlugin(main);
						}
					}else{
						sender.sendMessage(ChatColor.RED + "You do not have permission!");
					}
				}else{
					main.reloadConfig();
					main.getLogger().info("Config reloaded");
					if (main.getConfig().getBoolean("CameraMode.Enabled") == false) {
						main.getLogger().info("Plugin Disable Setting Detected...");
						main.getServer().getPluginManager().disablePlugin(main);
					}
				}
			//#####################//
			//###- Config_Help -###//
			}else if (args[0].equalsIgnoreCase("config")) {
				if (sender.hasPermission("cameramode.config") || !(sender instanceof Player)) {
					sender.sendMessage(ChatColor.AQUA + "__CameraMode Configuration Options__PG.1__");
					sender.sendMessage(ChatColor.GRAY + "[1] " + ChatColor.DARK_GRAY + "Enabled: " + ChatColor.RED + main.getConfig().getString("CameraMode.Enabled"));
					sender.sendMessage(ChatColor.GRAY + "[2] " + ChatColor.DARK_AQUA + "CameraModed players are invincible: " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.AreInvincible"));
					sender.sendMessage(ChatColor.GRAY + "[3] " + ChatColor.DARK_AQUA + "CameraModed players are vanished: " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.AreVanished"));
					sender.sendMessage(ChatColor.GRAY + "[4] " + ChatColor.DARK_AQUA + "CameraModed players can change worlds: " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.CanChangeWorlds"));
					sender.sendMessage(ChatColor.GRAY + "[5] " + ChatColor.DARK_AQUA + "Automatically update when newer version is available: " + ChatColor.RED + main.getConfig().getString("CameraMode.Updates.AutoUpdate"));
					sender.sendMessage(ChatColor.GRAY + "[6] " + ChatColor.DARK_AQUA + "Notify Ops of newer available version: " + ChatColor.RED + main.getConfig().getString("CameraMode.Updates.NotifyOps"));
					sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
					sender.sendMessage(ChatColor.AQUA + "To view available options and their #, - " + ChatColor.DARK_AQUA + "/camera config");
					sender.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "To change pages - " + ChatColor.GRAY + "/camera config page #");
				}else if (!sender.hasPermission("cameramode.config") && sender instanceof Player){
					sender.sendMessage(ChatColor.RED + "You do not have permission.");
				}
			}else{
				if (sender.hasPermission("cameramode.cm") || (sender.hasPermission("cameramode.reload")) && (sender.hasPermission("cameramode.camera"))) {
					sender.sendMessage(ChatColor.AQUA + "__CameraMode Commands__");
					sender.sendMessage(ChatColor.DARK_AQUA + "/Camera" + ChatColor.GRAY + "  - Displays main Help List");
				} 
				if (sender.hasPermission("cameramode.cm")){
					sender.sendMessage(ChatColor.DARK_AQUA + "/CameraMode" + ChatColor.GRAY + "  - Enables CameraMode");
				}
				if (sender.hasPermission("cameramode.config")) {
					sender.sendMessage(ChatColor.DARK_AQUA + "/Camera config" + ChatColor.GRAY + "  - Configure CameraMode");
				}
				if (sender.hasPermission("cameramode.reload")) {
					sender.sendMessage(ChatColor.DARK_AQUA + "/Camera reload" + ChatColor.GRAY + "  - Reloads the Configuration");
				}
			}
		}else if (args.length == 2){
			if (args[0].equalsIgnoreCase("config")) {   
				if (sender.hasPermission("cameramode.config") || !(sender instanceof Player)) { 
					sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
					sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
				}else if (!sender.hasPermission("cameramode.config") && sender instanceof Player){
					sender.sendMessage(ChatColor.RED + "You do not have permission.");
				}
			}
		//#######################//
		//###- Configuration -###//
		}else if (args.length == 3){
			if (args[0].equalsIgnoreCase("config")) {
				if (sender.hasPermission("cameramode.config") || !(sender instanceof Player)) {
    /*Enable*/		
					if (args[1].equalsIgnoreCase("1")) {
						if (args[2].equalsIgnoreCase("true")){
            				if (main.getConfig().getString("CameraMode.Enabled").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.Enabled", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								main.saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (main.getConfig().getString("CameraMode.Enabled").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.Enabled", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made. Don't forget to " + ChatColor.GRAY + "/camera reload");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
						
   /*Invincible*/	
					}else if (args[1].equalsIgnoreCase("2")){
						if (args[2].equalsIgnoreCase("true")){
							if (main.getConfig().getString("CameraMode.PlayersInCM.AreInvincible").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PlayersInCM.AreInvincible", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (main.getConfig().getString("CameraMode.PlayersInCM.AreInvincible").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PlayersInCM.AreInvincible", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
	/*Vanished*/
					}else if (args[1].equalsIgnoreCase("3")){
						if (args[2].equalsIgnoreCase("true")){
							if (main.getConfig().getString("CameraMode.PlayersInCM.AreVanished").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PlayersInCM.AreVanished", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (main.getConfig().getString("CameraMode.PlayersInCM.AreVanished").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PlayersInCM.AreVanished", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
  /*World_Change*/  
					}else if (args[1].equalsIgnoreCase("4")){
						if (args[2].equalsIgnoreCase("true")){
							if (main.getConfig().getString("CameraMode.PlayersInCM.CanChangeWorlds").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PlayersInCM.CanChangeWorlds", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (main.getConfig().getString("CameraMode.PlayersInCM.CanChangeWorlds").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PlayersInCM.CanChangeWorlds", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
    /*PvpTimer*/
					}else if (args[1].equalsIgnoreCase("7")){
						if (main.isInt(args[2])){
							int vall = Integer.parseInt(args[2]);
							if (main.getConfig().getInt("CameraMode.PvpTimer") == (vall)){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already set to " + ChatColor.DARK_AQUA + main.getConfig().getInt("CameraMode.PvpTimer"));
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PvpTimer", vall);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.RED + "This option only accepts integers!");
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
   /*UseCommands*/
					}else if (args[1].equalsIgnoreCase("10")){
						if (args[2].equalsIgnoreCase("true")){
							if (main.getConfig().getString("CameraMode.PlayersInCM.CanUseCommands").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PlayersInCM.CanUseCommands", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (main.getConfig().getString("CameraMode.PlayersInCM.CanUseCommands").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PlayersInCM.CanUseCommands", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
	/*Commands*/
					}else if (args[1].equalsIgnoreCase("addcmd")){
	                    if (main.commandIsWhitelisted(args[2]) == true) {
	                    	sender.sendMessage(ChatColor.GRAY + "That command is already listed");
	                    }else{
	                    	main.allowedcmds.add(args[2].replaceFirst("/", ""));
	                    	main.reloadConfig();
	                    	main.getConfig().set("CameraMode.PlayersInCM.AllowedCommands", main.allowedcmds);
	    				    StringBuilder cl = new StringBuilder();
	    				    for(String item : main.allowedcmds) {
	    				            cl.append(" /").append(item).append(",");
	    				    }
							sender.sendMessage(ChatColor.GRAY + "Allowed Commands:" + ChatColor.AQUA + (cl.toString()));
							sender.sendMessage(ChatColor.GREEN + "Changes Made!");
							main.saveConfig();
	                    }
					}else if (args[1].equalsIgnoreCase("delcmd")) {
	                    if (main.commandIsWhitelisted(args[2]) == false) {
	                    	sender.sendMessage(ChatColor.GRAY + "That command not yet whitelisted.");
	                    }else{
	                    	main.allowedcmds.remove(args[2].replace("/", ""));
	                    	main.reloadConfig();
	                    	main.getConfig().set("CameraMode.PlayersInCM.AllowedCommands", main.allowedcmds);
	                    	main.saveConfig();
	    				    StringBuilder cl = new StringBuilder();
	    				    for(String item : main.allowedcmds) {
	    				            cl.append(" /").append(item).append(",");
	    				    }
							sender.sendMessage(ChatColor.GRAY + "Allowed Commands:" + ChatColor.AQUA + (cl.toString()));
							sender.sendMessage(ChatColor.GREEN + "Changes Made!");
	                    }
					}else if (args[1].equalsIgnoreCase("5")){
						if (args[2].equalsIgnoreCase("true")){
							if (main.getConfig().getString("CameraMode.Updates.AutoUpdate").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.Updates.AutoUpdate", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (main.getConfig().getString("CameraMode.Updates.AutoUpdate").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.Updates.AutoUpdate", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
					}else if (args[1].equalsIgnoreCase("6")){
						if (args[2].equalsIgnoreCase("true")){
							if (main.getConfig().getString("CameraMode.Updates.NotifyOps").equalsIgnoreCase("true")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already enabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.Updates.NotifyOps", true);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else if (args[2].equalsIgnoreCase("false")){
							if (main.getConfig().getString("CameraMode.Updates.NotifyOps").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.Updates.NotifyOps", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
					}else if(args[1].equalsIgnoreCase("8")){
/*CameraTimeLimit*/		if(main.isInt(args[2])){
							int valld = Integer.parseInt(args[2]);
							if (main.getConfig().getInt("CameraMode.CameraTimeLimit") == (valld)){
								sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already set to " + ChatColor.DARK_AQUA + main.getConfig().getInt("CameraMode.CameraTimeLimit"));
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.CameraTimeLimit", valld);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.RED + "This option only accepts integers!");
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
					}else if(args[1].equalsIgnoreCase("9")){
/*CameraCoolDown*/		if(main.isInt(args[2])){
							int valls = Integer.parseInt(args[2]);
							if (main.getConfig().getInt("CameraMode.CameraCoolDown") == (valls)){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already set to " + ChatColor.DARK_AQUA + main.getConfig().getInt("CameraMode.CameraCoolDown"));
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.CameraCoolDown", valls);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.RED + "This option only accepts integers!");
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
					}else if(args[1].equalsIgnoreCase("page")){
						if((args[2].equalsIgnoreCase("1"))){
							sender.sendMessage(ChatColor.AQUA + "__CameraMode Configuration Options__PG.1__");
							sender.sendMessage(ChatColor.GRAY + "[1] " + ChatColor.DARK_GRAY + "Enabled: " + ChatColor.RED + main.getConfig().getString("CameraMode.Enabled"));
							sender.sendMessage(ChatColor.GRAY + "[2] " + ChatColor.DARK_AQUA + "CameraModed players are invincible: " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.AreInvincible"));
							sender.sendMessage(ChatColor.GRAY + "[3] " + ChatColor.DARK_AQUA + "CameraModed players are vanished: " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.AreVanished"));
							sender.sendMessage(ChatColor.GRAY + "[4] " + ChatColor.DARK_AQUA + "CameraModed players can change worlds: " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.CanChangeWorlds"));
							sender.sendMessage(ChatColor.GRAY + "[5] " + ChatColor.DARK_AQUA + "Automatically update when newer version is available: " + ChatColor.RED + main.getConfig().getString("CameraMode.Updates.AutoUpdate"));
							sender.sendMessage(ChatColor.GRAY + "[6] " + ChatColor.DARK_AQUA + "Notify Ops of newer available version: " + ChatColor.RED + main.getConfig().getString("CameraMode.Updates.NotifyOps"));
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, - " + ChatColor.DARK_AQUA + "/camera config");
							sender.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "To change pages - " + ChatColor.GRAY + "/camera config page #");
						}else if(args[2].equalsIgnoreCase("2")){
							sender.sendMessage(ChatColor.AQUA + "__CameraMode Configuration Options__PG.2__");
							sender.sendMessage(ChatColor.GRAY + "[7] " + ChatColor.DARK_AQUA + "PvP timer lasts " + ChatColor.RED + main.getConfig().getInt("CameraMode.PvpTimer") + ChatColor.DARK_AQUA + " seconds");
							sender.sendMessage(ChatColor.GRAY + "[8] " + ChatColor.DARK_AQUA + "CameraMode lasts " + ChatColor.RED + main.getConfig().getInt("CameraMode.CameraTimeLimit") + ChatColor.DARK_AQUA + " seconds");
							sender.sendMessage(ChatColor.GRAY + "[9] " + ChatColor.DARK_AQUA + "CameraMode has a cooldown of " + ChatColor.RED + main.getConfig().getInt("CameraMode.CameraCoolDown") + ChatColor.DARK_AQUA + " seconds");
							sender.sendMessage(ChatColor.GRAY + "[10] " + ChatColor.DARK_AQUA + "CameraModed players can use commands: " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.CanUseCommands"));
							   StringBuilder cl = new StringBuilder();
							    for(String item : main.allowedcmds) {
							            cl.append(" /").append(item).append(",");
							    }	sender.sendMessage(ChatColor.DARK_GRAY + "To toggle a cmd: " + ChatColor.GRAY + "/camera config (addcmd/delcmd) </command>");
							sender.sendMessage(ChatColor.DARK_AQUA + "Allowed Commands:" + ChatColor.GREEN + (cl.toString()));
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, - " + ChatColor.DARK_AQUA + "/camera config");
							sender.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "To change pages - " + ChatColor.GRAY + "/camera config page #");
						}else{
							sender.sendMessage(ChatColor.AQUA + "__CameraMode Configuration Options__PG.∞__");
							sender.sendMessage(ChatColor.GRAY + "[∞] " + ChatColor.DARK_AQUA + "There is nothing here...");
							sender.sendMessage("");
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, - " + ChatColor.DARK_AQUA + "/camera config");
							sender.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "To change pages - " + ChatColor.GRAY + "/camera config page #");
						}
					}else{
						sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.GRAY + "/camera config (option#) (newValue)");
						if (main.getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == (false)){
							sender.sendMessage(ChatColor.GRAY + "To toggle a cmd: " + ChatColor.GRAY + "/camera config (addcmd/delcmd) </command>");
						}
						sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
					}
				}else if (!sender.hasPermission("cameramode.config") && sender instanceof Player){
					sender.sendMessage(ChatColor.RED + "You do not have permission.");
				}
			}
	//########################//
	//#####- CameraMode -#####// 
		}else{
			if (sender.hasPermission("cameramode.cm") || (sender.hasPermission("cameramode.reload")) || (sender.hasPermission("cameramode.camera")) || sender.hasPermission("cameramode.config")) {
				sender.sendMessage(ChatColor.RED + "Too many arguments.");
			}else{
				sender.sendMessage(ChatColor.RED + "You do not have permissions.");
			}
		}
	}
	if (cmd.getName().equalsIgnoreCase("cameramode")) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				if (sender.hasPermission("cameramode.cm")) {
					final Player p = (Player) sender;
 /*Survival*/ 		final String target = ((Player) sender).getUniqueId().toString();
   /*off*/			if(main.flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.SURVIVAL)) {
						((Player) sender).setAllowFlight(false);
						Location loc = main.locations.get(p.getUniqueId().toString());
						p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						CameraMode pInst = main;
						p.setFallDistance(main.falldistance.get(p.getUniqueId().toString()));
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
							public void run() {
								p.setVelocity(main.vel.get(p.getUniqueId().toString()));
							}
						}, (long) 0.5);
						main.flyplayers.remove(target);
						((Player) sender).addPotionEffects(main.effects.get(((Player) sender).getUniqueId().toString()));
						sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						int Fireup = main.fireticks.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setFireTicks(Fireup);
						int air = main.breath.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setRemainingAir(air);
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(p);
						}
						main.coolDown.put(((Player) sender).getUniqueId(), main.getConfig().getInt("CameraMode.CameraCoolDown"));
						ID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
							public void run() {
									main.coolDown.put(((Player) sender).getUniqueId(), main.coolDown.get(((Player) sender).getUniqueId()) - 1);
									if (main.coolDown.get(((Player) sender).getUniqueId()) == 1){
										Bukkit.getScheduler().cancelTask(ID);
									}
							}
						}, 0,20);
						main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run() {
								main.coolDown.remove(((Player) sender).getUniqueId());
							}
						},20 * main.getConfig().getInt("CameraMode.CameraCoolDown"));
						
  /*Creative*/		}else if (main.flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.CREATIVE)) {
	  /*off*/	
						main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
						public void run() {
						p.setVelocity(main.vel.get(p.getUniqueId().toString()));
						}
						}, 1L);
						main.flyplayers.remove(target);
						p.setFallDistance(main.falldistance.get(p.getUniqueId().toString()));
						int Fireup = main.fireticks.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setFireTicks(Fireup);
						int air = main.breath.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setRemainingAir(air);
						Location loc = main.locations.get(p.getUniqueId().toString());
						p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						((Player) sender).addPotionEffects(main.effects.get(((Player) sender).getUniqueId().toString()));
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(p);
						}
						main.coolDown.put(((Player) sender).getUniqueId(), main.getConfig().getInt("CameraMode.CameraCoolDown"));
						ID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
							public void run() {
									main.coolDown.put(((Player) sender).getUniqueId(), main.coolDown.get(((Player) sender).getUniqueId()) - 1);
									if (main.coolDown.get(((Player) sender).getUniqueId()) == 1){
										Bukkit.getScheduler().cancelTask(ID);
									}
							}
						}, 0,20);
						main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run() {
								main.coolDown.remove(((Player) sender).getUniqueId());
							}
						},20 * main.getConfig().getInt("CameraMode.CameraCoolDown"));
						
					}else{
						if (!(main.coolDown.containsKey(((Player) sender).getUniqueId()))) {
	      /*On*/			if (!(main.pvpTimer.containsKey(((Player) sender).getUniqueId().toString()))) {
								main.flyplayers.add(target);
								main.falldistance.put(p.getUniqueId().toString(), p.getFallDistance());
								main.vel.put(((Player) sender).getUniqueId().toString(), ((Player) sender).getVelocity());
								main.locations.put(p.getUniqueId().toString(), p.getLocation());
								((Player) sender).setAllowFlight(true);
								main.fireticks.put(((Player) sender).getUniqueId().toString(), ((Player) sender).getFireTicks());
								((Player) sender).setFireTicks(0);
								main.breath.put(((Player) sender).getUniqueId().toString(), ((Player) sender).getRemainingAir());
								main.effects.put(((Player) sender).getUniqueId().toString(), (List<PotionEffect>) ((Player) sender).getActivePotionEffects());
								for (PotionEffect effect : ((Player) sender).getActivePotionEffects())
							        ((Player) sender).removePotionEffect(effect.getType());
								sender.sendMessage(ChatColor.GOLD + "You are now in CameraMode!");
								if (main.getConfig().getBoolean("CameraMode.PlayersInCM.AreVanished") == true) {
									for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
										pl.hidePlayer(p);
									}
								}
								for(Entity mob : ((Player)sender).getNearbyEntities(36, 36, 36)) {
									if(mob.getType().equals(EntityType.BLAZE) || mob.getType().equals( EntityType.CAVE_SPIDER )|| mob.getType().equals(   EntityType.CREEPER) || mob.getType().equals(   EntityType.ENDERMAN )|| mob.getType().equals(   EntityType.GIANT) || mob.getType().equals(   EntityType.IRON_GOLEM )|| mob.getType().equals(   EntityType.PIG_ZOMBIE )|| mob.getType().equals(   EntityType.PLAYER )|| mob.getType().equals(   EntityType.SILVERFISH) || mob.getType().equals(   EntityType.SKELETON) || mob.getType().equals(   EntityType.SPIDER )|| mob.getType().equals(   EntityType.WITCH )|| mob.getType().equals(   EntityType.WITHER) || mob.getType().equals(   EntityType.ZOMBIE) || mob.getType().equals(   EntityType.WOLF)){
										if(mob.getType().equals(EntityType.PLAYER)){
											//* Potential Stuff Here*//
										}else{
											Creature mobs = (Creature) mob;
											if(mobs.getTarget() != null && mobs.getTarget().equals((LivingEntity)sender)){
												mobs.setTarget(null);
												if(mob.getType().equals(EntityType.ENDERMAN)){
													main.enderman.put(((Player) sender).getUniqueId(), mob);
												}
											}
										}
									}
								}	
								if (main.getConfig().getInt("CameraMode.CameraTimeLimit") > 0) {
									sender.sendMessage(ChatColor.GREEN + "You have " + (main.getConfig().getInt("CameraMode.CameraTimeLimit") + " seconds."));
									main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
									public void run() {
										if(main.flyplayers.contains(((Player)sender).getUniqueId().toString())) {
											main.coolDown.put(((Player) sender).getUniqueId(), main.getConfig().getInt("CameraMode.CameraCoolDown"));
											ID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
												public void run() {
														main.coolDown.put(((Player) sender).getUniqueId(), main.coolDown.get(((Player) sender).getUniqueId()) - 1);
														if (main.coolDown.get(((Player) sender).getUniqueId()) == 1){
															Bukkit.getScheduler().cancelTask(ID);
														}
												}
											}, 0,20);
											main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
												public void run() {
													main.coolDown.remove(((Player) sender).getUniqueId());
												}
											},20 * main.getConfig().getInt("CameraMode.CameraCoolDown"));
										}
										if(main.flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.SURVIVAL)) {
											((Player) sender).setAllowFlight(false);
											Location loc = main.locations.get(p.getUniqueId().toString());
											p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
											CameraMode pInst = main;
											p.setFallDistance(main.falldistance.get(p.getUniqueId().toString()));
											pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
												public void run() {
													p.setVelocity(main.vel.get(p.getUniqueId().toString()));
												}
											}, (long) 0.5);
											main.flyplayers.remove(target);
											((Player) sender).addPotionEffects(main.effects.get(((Player) sender).getUniqueId().toString()));
											sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
											int Fireup = main.fireticks.get(((Player) sender).getUniqueId().toString()).intValue();
											((Player) sender).setFireTicks(Fireup);
											int air = main.breath.get(((Player) sender).getUniqueId().toString()).intValue();
											((Player) sender).setRemainingAir(air);
											for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
												pl.showPlayer(p);
											}
										}else if (main.flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.CREATIVE)) {
											CameraMode pInst = main;
											pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
											public void run() {
											p.setVelocity(main.vel.get(p.getUniqueId().toString()));
											}
											}, 1L);
											main.flyplayers.remove(target);
											p.setFallDistance(main.falldistance.get(p.getUniqueId().toString()));
											int Fireup = main.fireticks.get(((Player) sender).getUniqueId().toString()).intValue();
											((Player) sender).setFireTicks(Fireup);
											int air = main.breath.get(((Player) sender).getUniqueId().toString()).intValue();
											((Player) sender).setRemainingAir(air);
											Location loc = main.locations.get(p.getUniqueId().toString());
											p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
											sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
											((Player) sender).addPotionEffects(main.effects.get(((Player) sender).getUniqueId().toString()));
											for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
												pl.showPlayer(p);
											}
										}
									}
									}, main.getConfig().getLong("CameraMode.CameraTimeLimit") * 20);
									main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
										public void run() {
											sender.sendMessage(ChatColor.RED + "You have 3 seconds.");
										}
										}, (main.getConfig().getLong("CameraMode.CameraTimeLimit") * 20)-3);
								}
							}else{
								sender.sendMessage(ChatColor.RED + "You cannot CameraMode while in battle! " + ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + main.pvpTimer.get(((Player) sender).getUniqueId().toString()).intValue() + ChatColor.DARK_GRAY + "]");
							}
						}else{
							sender.sendMessage(ChatColor.RED + "Your camera needs " + ChatColor.DARK_GRAY + "[" + main.coolDown.get(((Player) sender).getUniqueId()) + "]" + ChatColor.RED + " more seconds to cool down!" );
						}
					}
				}else{
					sender.sendMessage(ChatColor.RED + "You do not have permission!");
				}
			}else{
				main.getLogger().info("Usage: /cameramode <player> ");
			}
  //##########################//
  //###- CameraMode Other -###//
		}else if (args.length == 1) {
			if (sender instanceof Player) {
				if (sender.hasPermission("cameramode.other") || args[0].equalsIgnoreCase(((Player) sender).getName().toString())) {
					if (Bukkit.getServer().getPlayerExact(args[0]) != null){
						final Player targetPlayer = Bukkit.getServer().getPlayerExact(args[0]);
						final String superTarget = targetPlayer.getUniqueId().toString();
						if(main.flyplayers.contains(superTarget) && (targetPlayer).getGameMode() == (GameMode.SURVIVAL)) {
							targetPlayer.setAllowFlight(false);
							Location loc = main.locations.get(superTarget);
							targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
							CameraMode pInst = main;
		/*OFF*/				pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
							public void run() {
							targetPlayer.setVelocity(main.vel.get(superTarget));
							}
							}, 1L);
							main.flyplayers.remove(superTarget);
							targetPlayer.setFallDistance(main.falldistance.get(targetPlayer.getUniqueId().toString()));
							targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
							targetPlayer.addPotionEffects(main.effects.get(targetPlayer.getUniqueId().toString()));
							if (superTarget.equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
								sender.sendMessage(ChatColor.GRAY + "Try just /cm next time ;)");
							}else{
								targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " + sender.getName().toString());
								sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
							}
							int Fireup = main.fireticks.get(superTarget).intValue();
							targetPlayer.setFireTicks(Fireup);
							int air = main.breath.get(superTarget).intValue();
							(targetPlayer).setRemainingAir(air);
							for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
								pl.showPlayer(targetPlayer);
							}
							main.coolDown.put(targetPlayer.getUniqueId(), main.getConfig().getInt("CameraMode.CameraCoolDown"));
							ID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
								public void run() {
										main.coolDown.put(targetPlayer.getUniqueId(), main.coolDown.get(targetPlayer.getUniqueId()) - 1);
										if (main.coolDown.get(targetPlayer.getUniqueId()) == 1){
											Bukkit.getScheduler().cancelTask(ID);
										}
								}
							}, 0,20);
							main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
								public void run() {
									main.coolDown.remove(targetPlayer.getUniqueId());
								}
							},20 * main.getConfig().getInt("CameraMode.CameraCoolDown"));
							
						}else if (main.flyplayers.contains(superTarget) && targetPlayer.getGameMode() == (GameMode.CREATIVE)) {
							CameraMode pInst = main;
							pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
								public void run() {
									targetPlayer.setVelocity(main.vel.get(superTarget));
								}
							}, 1L);
							main.flyplayers.remove(targetPlayer);
							int Fireup = main.fireticks.get(superTarget).intValue();
							(targetPlayer).setFireTicks(Fireup);
							int air = main.breath.get(superTarget).intValue();
	/*OFF*/					(targetPlayer).setRemainingAir(air);
							targetPlayer.setFallDistance(main.falldistance.get(targetPlayer.getUniqueId().toString()));
							Location loc = main.locations.get(superTarget);
							targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
							targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
							targetPlayer.addPotionEffects(main.effects.get(targetPlayer.getUniqueId().toString()));
							if (superTarget.equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
								sender.sendMessage(ChatColor.GRAY + "Try just /cm next time ;)");
							}else{
								targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " + sender.getName().toString());
								sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
							}
							for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
								pl.showPlayer(targetPlayer);
							}
							main.coolDown.put(targetPlayer.getUniqueId(), main.getConfig().getInt("CameraMode.CameraCoolDown"));
							ID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
								public void run() {
										main.coolDown.put(targetPlayer.getUniqueId(), main.coolDown.get(targetPlayer.getUniqueId()) - 1);
										if (main.coolDown.get(targetPlayer.getUniqueId()) == 1){
											Bukkit.getScheduler().cancelTask(ID);
										}
								}
							}, 0,20);
							main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
								public void run() {
									main.coolDown.remove(targetPlayer.getUniqueId());
								}
							},20 * main.getConfig().getInt("CameraMode.CameraCoolDown"));
							
						}else{
							if (!(main.coolDown.containsKey(targetPlayer))){
								if (!(main.pvpTimer.containsKey(superTarget))){
								main.flyplayers.add(superTarget);
								main.locations.put(superTarget, targetPlayer.getLocation());
								targetPlayer.setAllowFlight(true);
								main.fireticks.put(superTarget, targetPlayer.getFireTicks());
		/*ON*/					targetPlayer.setFireTicks(0);
								main.vel.put(superTarget, targetPlayer.getVelocity());
								main.breath.put(superTarget, targetPlayer.getRemainingAir());
								main.effects.put(superTarget, (List<PotionEffect>) targetPlayer.getActivePotionEffects());
								for (PotionEffect effect : targetPlayer.getActivePotionEffects()){
							        targetPlayer.removePotionEffect(effect.getType());
								}
								for(Entity mob : targetPlayer.getNearbyEntities(36, 36, 36)) {
									if(mob.getType().equals(EntityType.BLAZE) || mob.getType().equals( EntityType.CAVE_SPIDER )|| mob.getType().equals(   EntityType.CREEPER) || mob.getType().equals(   EntityType.ENDERMAN )|| mob.getType().equals(   EntityType.GIANT) || mob.getType().equals(   EntityType.IRON_GOLEM )|| mob.getType().equals(   EntityType.PIG_ZOMBIE )|| mob.getType().equals(   EntityType.PLAYER )|| mob.getType().equals(   EntityType.SILVERFISH) || mob.getType().equals(   EntityType.SKELETON) || mob.getType().equals(   EntityType.SPIDER )|| mob.getType().equals(   EntityType.WITCH )|| mob.getType().equals(   EntityType.WITHER) || mob.getType().equals(   EntityType.ZOMBIE) || mob.getType().equals(   EntityType.WOLF)){
										if(mob.getType().equals(EntityType.PLAYER)){
											//* Potential Stuff Here*//
										}else{
											Creature mobs = (Creature) mob;
											if(mobs.getTarget() != null && mobs.getTarget().equals((LivingEntity)targetPlayer)){
												mobs.setTarget(null);
												if(mob.getType().equals(EntityType.ENDERMAN)){
													//main.enderman.put(targetPlayer.getUniqueId(), mob);
												}
											}
										}
									}
								}	
								main.falldistance.put(targetPlayer.getUniqueId().toString(), targetPlayer.getFallDistance());
								targetPlayer.sendMessage(ChatColor.GOLD + "You are now in CameraMode!");
									if (superTarget.equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
										sender.sendMessage(ChatColor.GRAY + "Try just /cm next time ;)");
									}else{
										targetPlayer.sendMessage(ChatColor.GOLD + "Compliments of " + sender.getName().toString());
										sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has successfully been put in CameraMode");
									}
									if (main.getConfig().getBoolean("CameraMode.PlayersInCM.AreVanished") == true) {
										for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
											pl.hidePlayer(targetPlayer);
										}
									}
									if (main.getConfig().getInt("CameraMode.CameraTimeLimit") > 0) {
										if (!superTarget.equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
										sender.sendMessage(ChatColor.GREEN + "They have " + (main.getConfig().getInt("CameraMode.CameraTimeLimit") + " seconds."));
										targetPlayer.sendMessage(ChatColor.GREEN + "You have " + (main.getConfig().getInt("CameraMode.CameraTimeLimit") + " seconds."));
										}else{
											targetPlayer.sendMessage(ChatColor.GREEN + "You have " + (main.getConfig().getInt("CameraMode.CameraTimeLimit") + " seconds."));
										}
										main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
										public void run() {
											if(main.flyplayers.contains(targetPlayer.getUniqueId().toString())) {
												main.coolDown.put(targetPlayer.getUniqueId(), main.getConfig().getInt("CameraMode.CameraCoolDown"));
												ID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
													public void run() {
															main.coolDown.put(targetPlayer.getUniqueId(), main.coolDown.get(targetPlayer.getUniqueId()) - 1);
															if (main.coolDown.get(targetPlayer.getUniqueId()) == 1){
																Bukkit.getScheduler().cancelTask(ID);
															}
													}
												}, 0,20);
												main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
													public void run() {
														main.coolDown.remove(targetPlayer.getUniqueId());
													}
												},20 * main.getConfig().getInt("CameraMode.CameraCoolDown"));
											}
											if(main.flyplayers.contains(superTarget) && targetPlayer.getGameMode() == (GameMode.SURVIVAL)) {
												targetPlayer.setAllowFlight(false);
												Location loc = main.locations.get(superTarget);
												targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
												CameraMode pInst = main;
												targetPlayer.setFallDistance(main.falldistance.get(superTarget));
												pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
													public void run() {
														targetPlayer.setVelocity(main.vel.get(superTarget));
													}
												}, (long) 0.5);
												main.flyplayers.remove(superTarget);
												targetPlayer.addPotionEffects(main.effects.get(superTarget));
												if (!superTarget.equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
												sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is no longer in CameraMode!");
												targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
												}else{
													targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
												}
												int Fireup = main.fireticks.get(superTarget).intValue();
												targetPlayer.setFireTicks(Fireup);
												int air = main.breath.get(superTarget).intValue();
												targetPlayer.setRemainingAir(air);
												for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
													pl.showPlayer(targetPlayer);
												}
											}else if (main.flyplayers.contains(superTarget) && targetPlayer.getGameMode() == (GameMode.CREATIVE)) {
												CameraMode pInst = main;
												pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
												public void run() {
												targetPlayer.setVelocity(main.vel.get(superTarget));
												}
												}, 1L);
												main.flyplayers.remove(superTarget);
												targetPlayer.setFallDistance(main.falldistance.get(superTarget));
												int Fireup = main.fireticks.get(superTarget).intValue();
												targetPlayer.setFireTicks(Fireup);
												int air = main.breath.get(superTarget).intValue();
												targetPlayer.setRemainingAir(air);
												Location loc = main.locations.get(superTarget);
												targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
												if (!superTarget.equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
												sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is no longer in CameraMode!");
												targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
												}else{
													targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
												}
												targetPlayer.addPotionEffects(main.effects.get(superTarget));
												for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
													pl.showPlayer(targetPlayer);
												}
											}
										}
										}, main.getConfig().getLong("CameraMode.CameraTimeLimit") * 20);
										main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
											public void run() {
												targetPlayer.sendMessage(ChatColor.RED + "You have 3 seconds.");
											}
											}, (main.getConfig().getLong("CameraMode.CameraTimeLimit") * 20)-3);
									}
								}else{
									sender.sendMessage(ChatColor.RED + args[0] + " is ccurrently battling. " + ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + main.pvpTimer.get(superTarget).intValue() + ChatColor.DARK_GRAY + "]");
								}
							}else{
								sender.sendMessage(ChatColor.RED + args[0] + "'s camera needs " + ChatColor.DARK_GRAY + "[" + main.coolDown.get(targetPlayer.getUniqueId()) + "]" + ChatColor.RED + " more seconds to cool down!" );
							}
						}
					}else{
						sender.sendMessage(ChatColor.RED + args[0] + " is not online.");
					}
				}else{
					sender.sendMessage(ChatColor.RED + "You may only CameraMode yourself!");
				}
  //###################//
  //####- Console -####//
			}else{
				if (Bukkit.getServer().getPlayerExact(args[0]) != null){
					final Player targetPlayer = Bukkit.getServer().getPlayerExact(args[0]);
					final String superTarget = targetPlayer.getUniqueId().toString();
					if (!(main.flyplayers.contains(superTarget))){
						if (!(main.coolDown.containsKey(targetPlayer))){
							if (!(main.pvpTimer.containsKey(superTarget))){
								main.flyplayers.add(superTarget);
								main.locations.put(superTarget, targetPlayer.getLocation());
								targetPlayer.setAllowFlight(true);
								main.fireticks.put(superTarget, targetPlayer.getFireTicks());
								targetPlayer.setFireTicks(0);
								main.falldistance.put(targetPlayer.getUniqueId().toString(), targetPlayer.getFallDistance());
								main.vel.put(superTarget, targetPlayer.getVelocity());
	/*ON*/						main.breath.put(superTarget, targetPlayer.getRemainingAir());
								main.effects.put(superTarget, (List<PotionEffect>) targetPlayer.getActivePotionEffects());
								for (PotionEffect effect : targetPlayer.getActivePotionEffects())
							        targetPlayer.removePotionEffect(effect.getType());
								sender.sendMessage(ChatColor.GOLD + args[0] + " has successfully been put in CameraMode");
								targetPlayer.sendMessage(ChatColor.GOLD + "You are now in CameraMode!");
								targetPlayer.sendMessage(ChatColor.GOLD + "Compliments of " + ChatColor.GRAY + ChatColor.ITALIC + "CONSOLE.");
								if (main.getConfig().getBoolean("CameraMode.PlayersInCM.AreVanished") == true) {
									for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
										pl.hidePlayer(targetPlayer);
									}
								}
								for(Entity mob : targetPlayer.getNearbyEntities(36, 36, 36)) {
									if(mob.getType().equals(EntityType.BLAZE) || mob.getType().equals( EntityType.CAVE_SPIDER )|| mob.getType().equals(   EntityType.CREEPER) || mob.getType().equals(   EntityType.ENDERMAN )|| mob.getType().equals(   EntityType.GIANT) || mob.getType().equals(   EntityType.IRON_GOLEM )|| mob.getType().equals(   EntityType.PIG_ZOMBIE )|| mob.getType().equals(   EntityType.PLAYER )|| mob.getType().equals(   EntityType.SILVERFISH) || mob.getType().equals(   EntityType.SKELETON) || mob.getType().equals(   EntityType.SPIDER )|| mob.getType().equals(   EntityType.WITCH )|| mob.getType().equals(   EntityType.WITHER) || mob.getType().equals(   EntityType.ZOMBIE) || mob.getType().equals(   EntityType.WOLF)){
										if(mob.getType().equals(EntityType.PLAYER)){
											//* Potential Stuff Here*//
										}else{
											Creature mobs = (Creature) mob;
											if(mobs.getTarget() != null && mobs.getTarget().equals((LivingEntity)targetPlayer)){
												mobs.setTarget(null);
												if(mob.getType().equals(EntityType.ENDERMAN)){
													//main.enderman.put(targetPlayer.getUniqueId(), mob);
												}
											}
										}
									}
								}
								if (main.getConfig().getInt("CameraMode.CameraTimeLimit") > 0) {
									sender.sendMessage(ChatColor.GREEN + "They have " + (main.getConfig().getInt("CameraMode.CameraTimeLimit") + " seconds."));
									targetPlayer.sendMessage(ChatColor.GREEN + "You have " + (main.getConfig().getInt("CameraMode.CameraTimeLimit") + " seconds."));
									main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
									public void run() {
										if(main.flyplayers.contains(targetPlayer.getUniqueId().toString())) {
											main.coolDown.put(targetPlayer.getUniqueId(), main.getConfig().getInt("CameraMode.CameraCoolDown"));
											ID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
												public void run() {
														main.coolDown.put(targetPlayer.getUniqueId(), main.coolDown.get(targetPlayer.getUniqueId()) - 1);
														if (main.coolDown.get(targetPlayer.getUniqueId()) == 1){
															Bukkit.getScheduler().cancelTask(ID);
														}
												}
											}, 0,20);
											main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
												public void run() {
													main.coolDown.remove(targetPlayer.getUniqueId());
												}
											},20 * main.getConfig().getInt("CameraMode.CameraCoolDown"));
										}
										if(main.flyplayers.contains(superTarget) && targetPlayer.getGameMode() == (GameMode.SURVIVAL)) {
											targetPlayer.setAllowFlight(false);
											Location loc = main.locations.get(superTarget);
											targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
											CameraMode pInst = main;
											targetPlayer.setFallDistance(main.falldistance.get(superTarget));
											pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
												public void run() {
													targetPlayer.setVelocity(main.vel.get(superTarget));
												}
											}, (long) 0.5);
											main.flyplayers.remove(superTarget);
											targetPlayer.addPotionEffects(main.effects.get(superTarget));
											sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is no longer in CameraMode!");
											targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
											int Fireup = main.fireticks.get(superTarget).intValue();
											targetPlayer.setFireTicks(Fireup);
											int air = main.breath.get(superTarget).intValue();
											targetPlayer.setRemainingAir(air);
											for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
												pl.showPlayer(targetPlayer);
											}
										}else if (main.flyplayers.contains(superTarget) && targetPlayer.getGameMode() == (GameMode.CREATIVE)) {
											CameraMode pInst = main;
											pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
											public void run() {
											targetPlayer.setVelocity(main.vel.get(superTarget));
											}
											}, 1L);
											main.flyplayers.remove(superTarget);
											targetPlayer.setFallDistance(main.falldistance.get(superTarget));
											int Fireup = main.fireticks.get(superTarget).intValue();
											targetPlayer.setFireTicks(Fireup);
											int air = main.breath.get(superTarget).intValue();
											targetPlayer.setRemainingAir(air);
											Location loc = main.locations.get(superTarget);
											targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
											sender.sendMessage(ChatColor.RED + targetPlayer.getName() + " is no longer in CameraMode!");
											targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
											targetPlayer.addPotionEffects(main.effects.get(superTarget));
											for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
												pl.showPlayer(targetPlayer);
											}
										}
									}
									}, main.getConfig().getLong("CameraMode.CameraTimeLimit") * 20);
									main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
										public void run() {
											targetPlayer.sendMessage(ChatColor.RED + "You have 3 seconds.");
										}
										}, (main.getConfig().getLong("CameraMode.CameraTimeLimit") * 20)-3);
								}
							}else{
								sender.sendMessage(ChatColor.RED + args[0] + " is currently battling.");
							}
						}else{
							sender.sendMessage(ChatColor.RED + args[0] + "'s camera needs " + ChatColor.DARK_GRAY + "[" + main.coolDown.get(targetPlayer.getUniqueId()) + "]" + ChatColor.RED + " more seconds to cool down!" );
						}
					}else if (main.flyplayers.contains(superTarget) && (targetPlayer).getGameMode() == (GameMode.SURVIVAL)) {
						targetPlayer.setAllowFlight(false);
						Location loc = main.locations.get(superTarget);
						targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
	/*OFF*/				targetPlayer.setVelocity(main.vel.get(superTarget));
						CameraMode pInst = main;
						targetPlayer.setFallDistance(main.falldistance.get(targetPlayer.getUniqueId().toString()));
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
						public void run() {
						main.flyplayers.remove(superTarget);
						}
						}, 5L);
						sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
						targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						targetPlayer.setFallDistance(main.falldistance.get(targetPlayer.getUniqueId().toString()));
						targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " + ChatColor.GRAY + ChatColor.ITALIC + "CONSOLE.");
						targetPlayer.addPotionEffects(main.effects.get(superTarget));
						int Fireup = main.fireticks.get(superTarget).intValue();
						targetPlayer.setFireTicks(Fireup);
						int air = main.breath.get(superTarget).intValue();
						(targetPlayer).setRemainingAir(air);
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(targetPlayer);
						}
						main.coolDown.put(targetPlayer.getUniqueId(), main.getConfig().getInt("CameraMode.CameraCoolDown"));
						ID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
							public void run() {
									main.coolDown.put(targetPlayer.getUniqueId(), main.coolDown.get(targetPlayer.getUniqueId()) - 1);
									if (main.coolDown.get(targetPlayer.getUniqueId()) == 1){
										Bukkit.getScheduler().cancelTask(ID);
									}
							}
						}, 0,20);
						main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run() {
								main.coolDown.remove(targetPlayer.getUniqueId());
							}
						},20 * main.getConfig().getInt("CameraMode.CameraCoolDown"));
						
					}else{
						CameraMode pInst = main;
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
							public void run() {
								main.flyplayers.remove(targetPlayer);
							}
						}, 5L);
						int Fireup = main.fireticks.get(superTarget).intValue();
						(targetPlayer).setFireTicks(Fireup);
	/*OFF*/				int air = main.breath.get(superTarget).intValue();
						(targetPlayer).setRemainingAir(air);
						Location loc = main.locations.get(superTarget);
						targetPlayer.setFallDistance(main.falldistance.get(targetPlayer.getUniqueId().toString()));
						targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						targetPlayer.setVelocity(main.vel.get(superTarget));
						sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
						targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " +ChatColor.GRAY + ChatColor.ITALIC + "CONSOLE.");
						targetPlayer.addPotionEffects(main.effects.get(superTarget));
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(targetPlayer);
						}
						main.coolDown.put(targetPlayer.getUniqueId(), main.getConfig().getInt("CameraMode.CameraCoolDown"));
						ID = main.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable(){
							public void run() {
									main.coolDown.put(targetPlayer.getUniqueId(), main.coolDown.get(targetPlayer.getUniqueId()) - 1);
									if (main.coolDown.get(targetPlayer.getUniqueId()) == 1){
										Bukkit.getScheduler().cancelTask(ID);
									}
							}
						}, 0,20);
						main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable(){
							public void run() {
								main.coolDown.remove(targetPlayer.getUniqueId());
							}
						},20 * main.getConfig().getInt("CameraMode.CameraCoolDown"));
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

