package com.github.hibi_10000.plugins.autobackup;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class AutoBackup extends JavaPlugin {
	Backup backup;
	Save save;
	Reboot reboot;
	Restart restart;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		Config.setPluginInstance(this);
		backup = new Backup(this);
		save = new Save(this);
		reboot = new Reboot(this);
		restart = new Restart(this);
		backup.schedule();
		save.schedule();
		reboot.schedule();
		restart.schedule();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("autobackup")) {
			if (args.length == 0) {
				sender.sendMessage("[AutoBackup] §cIncorrect argument for command");
				return false;
			}
			if (args[0].equalsIgnoreCase("save")) {
				if (args.length != 2) {
					if (args.length == 1) {
						if (!sender.hasPermission("autobackup.save")) {
							sender.sendMessage("[AutoBackup] §cYou do not have permission to execute this command.");
							return false;
						}
						sender.sendMessage("§a[AutoBackup] §eStarting Save World & Player Data...");
						Bukkit.broadcastMessage("§7[AutoBackup] §eSaving World & Player Data...");
						getLogger().log(Level.INFO, "§e" + sender.getName() + " Starting Save World & Player Data...");
						save.saveAll();
						return true;
					}
					sender.sendMessage("§cUnknown or incomplete command.");
					return false;
				}
				if (!sender.hasPermission("autobackup.save")) {
					sender.sendMessage("[AutoBackup] §cYou do not have permission to execute this command.");
					return false;
				}
				if (args[1].equalsIgnoreCase("all")) {
					sender.sendMessage("§a[AutoBackup] §eStarting Save World & Player Data...");
					Bukkit.broadcastMessage("§7[AutoBackup] §eSaving World & Player Data...");
					getLogger().log(Level.INFO, "§e" + sender.getName() + " Starting Save World & Player Data...");
					save.saveAll();
					return true;
				}
				if (args[1].equalsIgnoreCase("player")) {
					sender.sendMessage("§a[AutoBackup] §eStarting Save Player Data...");
					Bukkit.broadcastMessage("§7[AutoBackup] §eSaving Player Data...");
					getLogger().log(Level.INFO, "§e" + sender.getName() + " Starting Save Player Data...");
					save.savePlayerData();
					return true;
				}
				if (args[1].equalsIgnoreCase("world")) {
					sender.sendMessage("§a[AutoBackup] §eStarting Save World Data...");
					Bukkit.broadcastMessage("§7[AutoBackup] §eSaving World Data...");
					getLogger().log(Level.INFO, "§e" + sender.getName() + " Starting Save World Data...");
					save.saveWorldData();
					return true;
				}
				sender.sendMessage("§cUnknown or incomplete command.");
				return false;
			}
			if (args[0].equalsIgnoreCase("backup")) {
				if (args.length != 1) {
					sender.sendMessage("§cUnknown or incomplete command.");
					return false;
				}
				if (!sender.hasPermission("autobackup.backup")) {
					sender.sendMessage("[AutoBackup] §cYou do not have permission to execute this command.");
					return false;
				}
				sender.sendMessage("§a[AutoBackup] §eStarting Backup Worlds & Plugin Data...");
				getLogger().log(Level.INFO, "§b" + sender.getName() + " Starting Backup");
				backup.backup();
				return true;
			}
			if (args[0].equalsIgnoreCase("restart")) {
				if (args.length != 1) {
					sender.sendMessage("§cUnknown or incomplete command.");
					return false;
				}
				if (!sender.hasPermission("autobackup.restart")) {
					sender.sendMessage("[AutoBackup] §cYou do not have permission to execute this command.");
					return false;
				}
				sender.sendMessage("§a[AutoBackup] §eStarting Restart Timer...");
				getLogger().log(Level.INFO, "§b" + sender.getName() + " Starting Restart Timer");
				restart.restart90(false);
				return true;
			}
			if (args[0].equalsIgnoreCase("reboot")) {
				if (args.length != 1) {
					sender.sendMessage("§cUnknown or incomplete command.");
					return false;
				}
				if (!sender.hasPermission("autobackup.reboot")) {
					sender.sendMessage("[AutoBackup] §cYou do not have permission to execute this command.");
					return false;
				}
				sender.sendMessage("§a[AutoBackup] §eStarting Physically Restart Timer...");
				getLogger().log(Level.INFO, "§b" + sender.getName() + " Starting Restart Timer");
				reboot.reboot90(false);
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (args.length != 1) {
					sender.sendMessage("§cUnknown or incomplete command.");
					return false;
				}
				if (!sender.hasPermission("autobackup.reload")) {
					sender.sendMessage("[AutoBackup] §cYou do not have permission to execute this command.");
					return false;
				}
				reloadConfig();
				Config.setPluginInstance(this);
				sender.sendMessage("§a[AutoBackup] §bConfig Reloaded.");
				getLogger().log(Level.INFO, "§b" + sender.getName() + " Reloaded Config");
				return true;
			}
			sender.sendMessage("[AutoBackup] §cUnknown or incomplete command.");
			return false;
		}
		return super.onCommand(sender, command, label, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("autobackup")) {
			if (args.length == 1) {
				List<String> list = new ArrayList<>();
				list.add("reload");
				list.add("save");
				list.add("backup");
				list.add("restart");
				list.add("reboot");
				return list;
			}
			if (args.length == 2 && args[0].equalsIgnoreCase("save")) {
				List<String> list = new ArrayList<>();
				list.add("all");
				list.add("player");
				list.add("world");
				return list;
			}
			return null;
		}
		return super.onTabComplete(sender, command, alias, args);
	}
}
