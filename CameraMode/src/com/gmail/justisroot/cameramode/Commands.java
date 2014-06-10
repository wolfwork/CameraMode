package com.gmail.justisroot.cameramode;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Commands{
	

	CameraMode main;
	
	@SuppressWarnings({ "deprecation" })
	public boolean onCommand(CommandSender sender, Command cmd, String StringLabel, String[] args) {
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
				    StringBuilder cl = new StringBuilder();
				    for(String item : main.allowedcmds) {
				            cl.append(" /").append(item).append(",");
				    }
					sender.sendMessage(ChatColor.AQUA + "__CameraMode Configuration Options__");
					sender.sendMessage(ChatColor.GRAY + "[1] " + ChatColor.DARK_GRAY + "Enabled: " + ChatColor.RED + main.getConfig().getString("CameraMode.Enabled"));
					sender.sendMessage(ChatColor.GRAY + "[2] " + ChatColor.DARK_AQUA + "CameraModed Players are Invincible: " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.AreInvincible"));
					sender.sendMessage(ChatColor.GRAY + "[3] " + ChatColor.DARK_AQUA + "CameraModed Players are Vanished: " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.AreVanished"));
					sender.sendMessage(ChatColor.GRAY + "[4] " + ChatColor.DARK_AQUA + "CameraModed Players can Change Worlds " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.CanChangeWorlds"));
					sender.sendMessage(ChatColor.GRAY + "[5] " + ChatColor.DARK_AQUA + "CameraModed Players can Use Commands " + ChatColor.RED + main.getConfig().getString("CameraMode.PlayersInCM.CanUseCommands"));
					if (main.getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == (false)){
						sender.sendMessage(ChatColor.GRAY + "To toggle a cmd: " + ChatColor.GRAY + "/camera config (addcmd/delcmd) </command>");
						sender.sendMessage(ChatColor.GRAY + "Allowed Commands:" + ChatColor.AQUA + (cl.toString()));
					}
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
					sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
					sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
					if (main.getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == (false)){
						sender.sendMessage(ChatColor.GRAY + "To toggle a cmd: " + ChatColor.GRAY + "/camera config (addcmd/delcmd) </command>");
					}
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
							sender.sendMessage(ChatColor.AQUA + "to modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
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
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
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
							sender.sendMessage(ChatColor.AQUA + "To modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
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
							if (main.getConfig().getString("CameraMode.PlayersInCM.CanChangeWolrds").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.GRAY + "Silly you... xP I'm already disabled!");
							}else{
								main.reloadConfig();
								main.getConfig().set("CameraMode.PlayersInCM.CanChangeWorlds", false);
								sender.sendMessage(ChatColor.GREEN + "Changes Made!");
								main.saveConfig();
							}
						}else{
							sender.sendMessage(ChatColor.AQUA + "to modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
							sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
						}
   /*UseCommands*/
					}else if (args[1].equalsIgnoreCase("5")){
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
							sender.sendMessage(ChatColor.AQUA + "to modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
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
					}else{
						sender.sendMessage(ChatColor.AQUA + "to modify an option - " + ChatColor.DARK_GRAY + "/camera config (option#) (true/false)");
						if (main.getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == (false)){
							sender.sendMessage(ChatColor.GRAY + "To toggle a cmd: " + ChatColor.GRAY + "/camera config (addcmd/delcmd) </command>");
						}
						sender.sendMessage(ChatColor.AQUA + "To view available options and their #, " + ChatColor.DARK_AQUA + "/camera config");
					}
				}else if (!sender.hasPermission("cameramode.config") || sender instanceof Player){
					sender.sendMessage(ChatColor.RED + "You do not have permission.");
				}
			}
	//########################//
	//#####- CameraMode -#####//       //Add PvpTimer to this v
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
					Player p = (Player) sender;
					final String target = ((Player) sender).getUniqueId().toString();
					if(main.flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.SURVIVAL)) {
						((Player) sender).setAllowFlight(false);
						Location loc = main.locations.get(p.getUniqueId().toString());
						p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						CameraMode pInst = main;
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
							public void run() {
								main.flyplayers.remove(target);
							}
						}, 5L);
						((Player) sender).addPotionEffects(main.effects.get(((Player) sender).getUniqueId().toString()));
						sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						int Fireup = main.fireticks.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setFireTicks(Fireup);
						int air = main.breath.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setRemainingAir(air);
						((Player) sender).setVelocity(main.vel.get(((Player) sender).getUniqueId().toString()));
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(p);
						}
					}else if (main.flyplayers.contains(target) && ((Player) sender).getGameMode() == (GameMode.CREATIVE)) {
						CameraMode pInst = main;
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
						public void run() {
						main.flyplayers.remove(target);
						}
						}, 5L);
						int Fireup = main.fireticks.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setFireTicks(Fireup);
						int air = main.breath.get(((Player) sender).getUniqueId().toString()).intValue();
						((Player) sender).setRemainingAir(air);
						Location loc = main.locations.get(p.getUniqueId().toString());
						p.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						((Player) sender).setVelocity(main.vel.get(((Player) sender).getUniqueId().toString()));
						sender.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						((Player) sender).addPotionEffects(main.effects.get(((Player) sender).getUniqueId().toString()));
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(p);
						}
					}else{
						if (!(main.pvpTimer.contains(((Player) sender).getUniqueId().toString()))) {
							main.flyplayers.add(target);
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
						}else{
							sender.sendMessage(ChatColor.RED + "You cannot CameraMode while in battle!");
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
				if (sender.hasPermission("cameramode.other") || args[0].equalsIgnoreCase(((Player) sender).getUniqueId().toString())) {
					if (Bukkit.getServer().getPlayerExact(args[0]) != null){
						final Player targetPlayer = Bukkit.getServer().getPlayerExact(args[0]);
						final String superTarget = targetPlayer.getUniqueId().toString();
						if(main.flyplayers.contains(superTarget) && (targetPlayer).getGameMode() == (GameMode.SURVIVAL)) {
							targetPlayer.setAllowFlight(false);
							Location loc = main.locations.get(superTarget);
							targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
							CameraMode pInst = main;
							pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
							public void run() {
							main.flyplayers.remove(superTarget);
							}
							}, 5L);
							targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
							targetPlayer.setVelocity(main.vel.get(superTarget));
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
						}else if (main.flyplayers.contains(superTarget) && targetPlayer.getGameMode() == (GameMode.CREATIVE)) {
							CameraMode pInst = main;
							pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
								public void run() {
									main.flyplayers.remove(targetPlayer);
								}
							}, 5L);
							int Fireup = main.fireticks.get(superTarget).intValue();
							(targetPlayer).setFireTicks(Fireup);
							int air = main.breath.get(superTarget).intValue();
							(targetPlayer).setRemainingAir(air);
							Location loc = main.locations.get(superTarget);
							targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
							targetPlayer.setVelocity(main.vel.get(superTarget));
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
						}else{
							if (!(main.pvpTimer.contains(superTarget))){
							main.flyplayers.add(superTarget);
							main.locations.put(superTarget, targetPlayer.getLocation());
							targetPlayer.setAllowFlight(true);
							main.fireticks.put(superTarget, targetPlayer.getFireTicks());
							targetPlayer.setFireTicks(0);
							main.vel.put(superTarget, targetPlayer.getVelocity());
							main.breath.put(superTarget, targetPlayer.getRemainingAir());
							main.effects.put(superTarget, (List<PotionEffect>) targetPlayer.getActivePotionEffects());
							for (PotionEffect effect : targetPlayer.getActivePotionEffects())
						        targetPlayer.removePotionEffect(effect.getType());
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
							}else{
								sender.sendMessage(ChatColor.RED + "This player is currently currently battling.");
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
						if (!(main.pvpTimer.contains(superTarget))){
							main.flyplayers.add(superTarget);
							main.locations.put(superTarget, targetPlayer.getLocation());
							targetPlayer.setAllowFlight(true);
							main.fireticks.put(superTarget, targetPlayer.getFireTicks());
							targetPlayer.setFireTicks(0);
							main.vel.put(superTarget, targetPlayer.getVelocity());
							main.breath.put(superTarget, targetPlayer.getRemainingAir());
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
						}else{
							sender.sendMessage(ChatColor.RED + "This player is currently battling.");
						}
					}else if (main.flyplayers.contains(superTarget) && (targetPlayer).getGameMode() == (GameMode.SURVIVAL)) {
						targetPlayer.setAllowFlight(false);
						Location loc = main.locations.get(superTarget);
						targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						targetPlayer.setVelocity(main.vel.get(superTarget));
						CameraMode pInst = main;
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
						public void run() {
						main.flyplayers.remove(superTarget);
						}
						}, 5L);
						sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
						targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " +ChatColor.GRAY + ChatColor.ITALIC + "CONSOLE.");
						targetPlayer.addPotionEffects(main.effects.get(superTarget));
						int Fireup = main.fireticks.get(superTarget).intValue();
						targetPlayer.setFireTicks(Fireup);
						int air = main.breath.get(superTarget).intValue();
						(targetPlayer).setRemainingAir(air);
						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.showPlayer(targetPlayer);
						}
					}else{
						CameraMode pInst = main;
						pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
							public void run() {
								main.flyplayers.remove(targetPlayer);
							}
						}, 5L);
						int Fireup = main.fireticks.get(superTarget).intValue();
						(targetPlayer).setFireTicks(Fireup);
						int air = main.breath.get(superTarget).intValue();
						(targetPlayer).setRemainingAir(air);
						Location loc = main.locations.get(superTarget);
						targetPlayer.teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
						targetPlayer.setVelocity(main.vel.get(superTarget));
						sender.sendMessage(ChatColor.GOLD + targetPlayer.getName().toString() + " has been ejected from CameraMode");
						targetPlayer.sendMessage(ChatColor.RED +  "You are no longer in CameraMode!");
						targetPlayer.sendMessage(ChatColor.GOLD + "Courtacy of " +ChatColor.GRAY + ChatColor.ITALIC + "CONSOLE.");
						targetPlayer.addPotionEffects(main.effects.get(superTarget));
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

