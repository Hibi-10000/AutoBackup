package com.github.hibi_10000.plugins.autobackup;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

public class Backup {
	AutoBackup plugin;
	Backup(AutoBackup instance) {
		plugin = instance;
	}


	public void backup() {
		Bukkit.broadcastMessage("§7[AutoBackup] §bBackuping Worlds & Plugin Data...");
		//plugin.getLogger().log(Level.INFO, "§bBackuping Worlds & Plugin Data...");
		plugin.save.saveAll();
		File server_folder = Bukkit.getServer().getWorldContainer();
		File backups_folder;
		File backup_folder;
		try {
			backups_folder = new File(server_folder.getCanonicalPath() + File.separator + "backups");
			if (!backups_folder.exists()) {
				Files.createDirectory(backups_folder.toPath());
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.JAPAN);
			backup_folder = new File(backups_folder.getCanonicalPath() + File.separator + sdf.format(new Date()));
			Files.createDirectory(backup_folder.toPath());
			Files.createDirectory(new File(backup_folder.getCanonicalPath() + File.separator + "worlds").toPath());
			for (World world : Bukkit.getWorlds()) {
				Files.createDirectory(new File(backup_folder.getCanonicalPath() + File.separator + "worlds" + File.separator + world.getName()).toPath());
				copy(world.getWorldFolder(), new File(backup_folder.getCanonicalPath()
						+ File.separator + "worlds" + File.separator + world.getName()));
			}
			Files.createDirectory(new File(backup_folder.getCanonicalPath() + File.separator + "plugins").toPath());
			copy(new File(server_folder.getCanonicalPath() + File.separator + "plugins"),
					new File(backup_folder.getCanonicalPath() + File.separator + "plugins"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Bukkit.broadcastMessage("§7[AutoBackup] §bBackup Finished");
		//plugin.getLogger().log(Level.INFO, "§bBackup Finished");
	}

	private void copy(File sourcefolder, File copyto) {
		for (File f : Objects.requireNonNull(sourcefolder.listFiles())) {
			if (!f.getName().equalsIgnoreCase("session.lock")) {
				try {
					Files.copy(f.toPath(), new File(copyto.getCanonicalPath() + File.separator + f.getName()).toPath(), StandardCopyOption.COPY_ATTRIBUTES);
					if (f.isDirectory()) {
						copy(f, new File(copyto.getCanonicalPath() + File.separator + f.getName()));
					}
				} catch (IOException e) {
					if (f.isDirectory()) {
						plugin.getLogger().log(Level.WARNING, "Failed to backup folder: " + f.getName(), e);
					} else {
						plugin.getLogger().log(Level.WARNING, "Failed to backup file: " + f.getName(), e);
					}
				}
			}
		}
	}

	public void schedule() {
		if (Config.getBackupTimingsType().equalsIgnoreCase("interval")) {
			scheduleInterval();
		} else if (Config.getBackupTimingsType().equalsIgnoreCase("times_of_day")) {
			scheduleTimes_of_Day();
		}
	}

	public void scheduleInterval() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (Config.getBackupEnabled()) {
					backup();
					schedule();
				}
			}
		}.runTaskLater(plugin, Config.getBackupTimingsIntervals());
	}

	public void scheduleTimes_of_Day() {
		try {
			SimpleDateFormat parsesdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat nowsdf = new SimpleDateFormat("yyyy/MM/dd");
			long nexttime = 0;
			for (String str : Config.getBackupTimingsTimes_of_Day()) {
				long time = parsesdf.parse(nowsdf.format(new Date()) + " " + str).getTime();
				if (nexttime == 0) {nexttime = time;}
				else if (new Date().getTime() < time && time < nexttime) {nexttime = time;}
			}
			SimpleDateFormat configsdf = new SimpleDateFormat("HH:mm:ss");
			int list_array_number = Config.getBackupTimingsTimes_of_Day().indexOf(configsdf.format(new Date(nexttime)));
			try {
				if (Config.getBackupTimingsTimes_of_Day().get(list_array_number) == null
						|| Config.getBackupTimingsTimes_of_Day().get(list_array_number).equalsIgnoreCase("")) {
					list_array_number = 0;
				}
			} catch (IndexOutOfBoundsException e) {
				list_array_number = 0;
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					if (Config.getBackupEnabled()) {
						backup();
						schedule();
					}
				}
			}.runTaskLater(plugin, (parsesdf.parse(nowsdf.format(new Date()) + " " + Config.getBackupTimingsTimes_of_Day().get(list_array_number)).getTime() - new Date().getTime()) / 50);
		} catch (ParseException e) {
			plugin.getLogger().log(Level.SEVERE, "Backup time setting is incorrect", e);
		}
	}
}
