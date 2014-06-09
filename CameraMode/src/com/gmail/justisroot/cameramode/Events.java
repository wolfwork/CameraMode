package com.gmail.justisroot.cameramode;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.projectiles.ProjectileSource;

public class Events implements Listener {

	CameraMode main;
	Updater updater;
	String reason = "You are in CameraMode!";

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			final Player player = (Player) e.getDamager();
			if(main.flyplayers.contains(player.getUniqueId().toString())){
				e.setCancelled(true);
						player.sendMessage(ChatColor.RED + reason);
			}else{
				if (main.getConfig().getLong("CameraMode.PvpTimer") != 0){
				 main.pvpTimer.add(e.getDamager().getUniqueId().toString());
				 CameraMode pInst = main;
					pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
						public void run() {
							main.pvpTimer.remove(player.getUniqueId().toString());
						}
					}, main.getConfig().getLong("CameraMode.PvpTimer"));
				}
			}
		}
		if (e.getEntity() instanceof Player){
			final Player entiti = (Player) e.getEntity();
			if(main.flyplayers.contains(entiti.getUniqueId().toString())){
				e.setCancelled(true);
			}else{
				if (main.getConfig().getLong("CameraMode.PvpTimer") != 0){
				 main.pvpTimer.add(e.getDamager().getUniqueId().toString());
				 CameraMode pInst = main;
					pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
						public void run() {
							main.pvpTimer.remove(entiti.getUniqueId().toString());
						}
					}, main.getConfig().getLong("CameraMode.PvpTimer"));
				}
			}
		}
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		if (main.biff.equals(Updater.UpdateResult.UPDATE_AVAILABLE)) {
			if (updater.getLatestType().toString().equalsIgnoreCase("release") && (main.getConfig().getBoolean("CameraMode.Updates.NotifyOps") == true)) {
				if (e.getPlayer().hasPermission("cameramode.update")){
					e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "CameraMode: " + ChatColor.AQUA + "New Update Available!");
					e.getPlayer().sendMessage(ChatColor.GRAY + updater.getLatestFileLink().toString());
				}
			}
		}
	}
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		ProjectileSource proj = e.getEntity().getShooter();
		if (proj instanceof Player){
			Player player =  (Player) proj;
			if (main.flyplayers.contains(player.getUniqueId().toString())){
			player.sendMessage(ChatColor.RED + reason);
			e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (main.flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
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
			if (main.flyplayers.contains(player.getUniqueId().toString())){
			player.sendMessage(ChatColor.RED + reason);
			e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		
	}
	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
	 if (!e.getPlayer().hasPermission("cameramode.fly") && (!(main.flyplayers.contains(e.getPlayer().getUniqueId().toString()))) && e.getPlayer().getGameMode() == (GameMode.SURVIVAL)) {
		 e.getPlayer().setAllowFlight(false);
		  }
	 if (e.getPlayer().hasPermission("cameramode.fly") && (main.flyplayers.contains(e.getPlayer().getUniqueId().toString())) && e.getPlayer().getAllowFlight() == (false)) {
		 e.getPlayer().setAllowFlight(true);
	 }
	}
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (main.flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
			e.getPlayer().sendMessage(ChatColor.RED + reason);
			e.setCancelled(true);
		}
    }
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (main.flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
			e.setCancelled(true);
		}
    }
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
		if (main.flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
			e.getPlayer().sendMessage(ChatColor.RED + reason);
			e.setCancelled(true);
		}
    }
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (main.getConfig().getBoolean("CameraMode.PlayersInCM.AreInvincible") == true) {
			if (e.getEntity() instanceof Player) {
				if (main.flyplayers.contains(e.getEntity().getUniqueId().toString())) {
					e.getEntity().setFireTicks(0);
					e.setCancelled(true);
				}	
			}
		}
    }
	@EventHandler
	public void onPlayerexpChange(PlayerExpChangeEvent e) {
		if (main.flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
			e.setAmount(0);
		}
    } 
	@EventHandler
	public void onEntityTarget(EntityTargetLivingEntityEvent e){
		if (main.getConfig().getBoolean("CameraMode.PlayersInCM.AreInvincible") == true) {
			if (e.getTarget() instanceof Player) {
				if (main.flyplayers.contains(e.getTarget().getUniqueId().toString())){
					e.setCancelled(true);
					e.setTarget(null);
				}
			}
		}
	}
	@EventHandler
	public void onEntityTarget(EntityTargetEvent e){
		if (main.getConfig().getBoolean("CameraMode.PlayersInCM.AreInvincible") == true) {
			if (e.getTarget() instanceof Player) {
				if (main.flyplayers.contains(e.getTarget().getUniqueId().toString())){
					e.setCancelled(true);
					e.setTarget(null);
				}
			}
		}
	}
	 @EventHandler
	 public void onFoodLevelChange(FoodLevelChangeEvent e) {
		 if (e.getEntity() instanceof Player) {
			if (main.flyplayers.contains(e.getEntity().getUniqueId().toString())) {
				e.setCancelled(true);
			}
		}
	}
	 @EventHandler
	 public void onPlayerRegainHealth(EntityRegainHealthEvent e) {
		 if (e.getEntity() instanceof Player) {
			 if (main.flyplayers.contains(e.getEntity().getUniqueId().toString())) {
				 if (e.getRegainReason() == RegainReason.SATIATED) {
					 e.setCancelled(true); 
				 }
	        }
		 }
	}
	 @EventHandler
	 public void onPlayerGameModeChange(final PlayerGameModeChangeEvent e) {
		final String player = e.getPlayer().getUniqueId().toString();
		if (main.flyplayers.contains(player)){
			Location loc = main.locations.get(e.getPlayer().getUniqueId().toString());
			if (!main.pause.contains(e.getPlayer().getUniqueId().toString())) {
				if (e.getPlayer().getWorld() == loc.getWorld()) {
					main.flyplayers.remove(player);
					e.getPlayer().teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
					CameraMode pInst = main;
					pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
					public void run() {
					e.getPlayer().sendMessage(ChatColor.RED + "You are no longer in CameraMode!");
					main.flyplayers.remove(player);
					}
					}, 5L);
					int Fireup = main.fireticks.get(e.getPlayer().getUniqueId().toString()).intValue();
					e.getPlayer().setFireTicks(Fireup);
					if (e.getNewGameMode() == GameMode.SURVIVAL) {
					e.getPlayer().setAllowFlight(false);
					}
				}
			}else{
				CameraMode pInst = main;
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
		 if (main.getConfig().getBoolean("CameraMode.PlayersInCM.CanChangeWorlds") == false) {
			 if (main.flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
				Location loc = main.locations.get(e.getPlayer().getUniqueId().toString());
				e.getPlayer().teleport(new Location (loc.getWorld(),loc.getX(),loc.getY(),loc.getZ(),loc.getYaw(),loc.getPitch()));
				if (!(e.getFrom() == loc.getWorld())) {
					e.getPlayer().sendMessage(ChatColor.RED +  "You Cannot Change Worlds While In CameraMode!");
				}
			}
		}else{
			CameraMode pInst = main;
			main.pause.add(e.getPlayer().getUniqueId().toString());
			pInst.getServer().getScheduler().scheduleSyncDelayedTask(pInst, new Runnable(){
			public void run() {
				main.pause.remove(e.getPlayer().getUniqueId().toString());
			}
			}, 50L);
		}
	
    } 
	 @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProccess(PlayerCommandPreprocessEvent e) {
        if (main.getConfig().getBoolean("CameraMode.PlayersInCM.CanUseCommands") == false) {
                if (main.flyplayers.contains(e.getPlayer().getUniqueId().toString())) {
                        if (main.commandIsWhitelisted(e.getMessage()) == false) {
                                e.setCancelled(true);
                                e.getPlayer().sendMessage(ChatColor.RED + reason);
                                return;
                        }
                }
        }
    }

}
