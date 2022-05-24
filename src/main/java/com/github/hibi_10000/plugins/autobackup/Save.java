package com.github.hibi_10000.plugins.autobackup;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class Save {
	AutoBackup plugin;
	Save(AutoBackup instance) {
		plugin = instance;
	}

	public void saveAll() {
		savePlayerData();
		saveWorldData();
	}

	public void saveWorldData() {for (World world : Bukkit.getWorlds()) {world.save();}}

	public void savePlayerData() {plugin.getServer().savePlayers();}

	public void schedule() {
		schedulePlayerData();
		scheduleWorldData();
	}

	public void schedulePlayerData() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (Config.getSavePlayerDataEnabled()) {
					Bukkit.broadcastMessage("§7[AutoBackup] §eSaving Player Data...");
					//plugin.getLogger().log(Level.INFO, "§eSaving Player Data...");
					savePlayerData();
					schedulePlayerData();
				}
			}
		}.runTaskLater(plugin, Config.getSavePlayerDataInterval());
	}

	public void scheduleWorldData() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (Config.getSaveWorldDataEnabled()) {
					Bukkit.broadcastMessage("§7[AutoBackup] §eSaving Worlds...");
					//plugin.getLogger().log(Level.INFO, "§eSaving Worlds...");
					saveWorldData();
					scheduleWorldData();
				}
			}
		}.runTaskLater(plugin, Config.getSaveWorldDataInterval());
	}
}
