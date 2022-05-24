package com.github.hibi_10000.plugins.autobackup;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
	private static FileConfiguration config;
	static void setPluginInstance(AutoBackup instance) {
		config = instance.getConfig();
	}

	public static boolean getBackupEnabled() {return config.getBoolean("Backup.enabled");}

	public static String getBackupTimingsType() {return config.getString("Backup.timings.type");}

	public static int getBackupTimingsIntervals() {return config.getInt("Backup.timings.intervals");}

	public static List<String> getBackupTimingsTimes_of_Day() {return config.getStringList("Backup.timings.times_of_day");}

	public static boolean getSavePlayerDataEnabled() {return config.getBoolean("Save.PlayerData.enabled");}

	public static int getSavePlayerDataInterval() {return config.getInt("Save.PlayerData.interval");}

	public static boolean getSaveWorldDataEnabled() {return config.getBoolean("Save.WorldData.enabled");}

	public static int getSaveWorldDataInterval() {return config.getInt("Save.WorldData.interval");}

	public static boolean getRestartEnabled() {return  config.getBoolean("Restart.enabled");}

	public static List<String> getRestartTimes() {return  config.getStringList("Restart.times_of_day");}

	public static boolean getRebootEnabled() {return config.getBoolean("Reboot.enabled");}

	public static String getRebootOSCommand() {return config.getString("Reboot.OS_Reboot_Command");}

	public static List<String> getRebootTimes() {return config.getStringList("Reboot.times_of_day");}
}
