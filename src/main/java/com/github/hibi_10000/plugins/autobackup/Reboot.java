package com.github.hibi_10000.plugins.autobackup;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class Reboot {
	AutoBackup plugin;
	Reboot(AutoBackup instance) {
		plugin = instance;
	}

	public void reboot() {
		Runtime.getRuntime().addShutdownHook(new Thread(
				() -> {
					try {
						Runtime.getRuntime().exec(Config.getRebootOSCommand());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}));
		Bukkit.shutdown();
	}

	public void reboot90(boolean schedule) {
		Bukkit.broadcastMessage("§7[AutoBackup] §r90 seconds left until the server physically restarts...");
		//plugin.getLogger().log(Level.INFO, "§b90 seconds left until the server physically restarts...");
		new BukkitRunnable() {
			public void run() {
				Bukkit.broadcastMessage("§7[AutoBackup] §r~ Server Physically Restart Execution ~");
				plugin.getLogger().log(Level.INFO, "§bStarting Physically Restart...");
				reboot();
				if (schedule) {schedule();}
			}
		}.runTaskLater(plugin, 1800);
	}

	public void schedule() {
		try {
			SimpleDateFormat parsesdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat nowsdf = new SimpleDateFormat("yyyy/MM/dd");
			long nexttime = 0;
			for (String str : Config.getRebootTimes()) {
				long time = parsesdf.parse(nowsdf.format(new Date()) + " " + str).getTime();
				if (nexttime == 0) {nexttime = time;}
				else if (new Date().getTime() < time && time < nexttime) {nexttime = time;}
			}
			SimpleDateFormat configsdf = new SimpleDateFormat("HH:mm:ss");
			int list_array_number = Config.getRebootTimes().indexOf(configsdf.format(new Date(nexttime)));
			try {
				if (Config.getRebootTimes().get(list_array_number) == null
						|| Config.getRebootTimes().get(list_array_number).equalsIgnoreCase("")) {
					list_array_number = 0;
				}
			} catch (IndexOutOfBoundsException e) {
				list_array_number = 0;
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					if (Config.getRebootEnabled()) {
						reboot90(true);
					}
				}
			}.runTaskLater(plugin, (parsesdf.parse(nowsdf.format(new Date()) + " " + Config.getRebootTimes().get(list_array_number)).getTime() - new Date().getTime()) / 50);
		} catch (ParseException e) {
			plugin.getLogger().log(Level.SEVERE, "Reboot time setting is incorrect", e);
		}
	}
}
