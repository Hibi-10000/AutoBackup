package com.github.hibi_10000.plugins.autobackup;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class Restart {
	AutoBackup plugin;
	Restart(AutoBackup instance) {
		plugin = instance;
	}

	public void restart() {Bukkit.spigot().restart();}

	public void restart90(boolean reschedule) {
		Bukkit.broadcastMessage("§7[AutoBackup] §r90 seconds left until the server restarts...");
		//plugin.getLogger().log(Level.INFO, "§b90 seconds left until the server restarts...");
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.broadcastMessage("§7[AutoBackup] §r～ Server Restart Execution ～");
				plugin.getLogger().log(Level.INFO, "§bStarting Restart...");
				restart();
				if (reschedule) {schedule();}
			}
		}.runTaskLater(plugin, 1800);
	}

	public void schedule() {
		try {
			SimpleDateFormat parsesdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat nowsdf = new SimpleDateFormat("yyyy/MM/dd");
			long nexttime = 0;
			for (String str : Config.getRestartTimes()) {
				long time = parsesdf.parse(nowsdf.format(new Date()) + " " + str).getTime();
				if (nexttime == 0) {nexttime = time;}
				else if (new Date().getTime() < time && time < nexttime) {nexttime = time;}
			}
			SimpleDateFormat configsdf = new SimpleDateFormat("HH:mm:ss");
			int list_array_number = Config.getRestartTimes().indexOf(configsdf.format(new Date(nexttime)));
			try {
				if (Config.getRestartTimes().get(list_array_number) == null
						|| Config.getRestartTimes().get(list_array_number).equalsIgnoreCase("")) {
					list_array_number = 0;
				}
			} catch (IndexOutOfBoundsException e) {
				list_array_number = 0;
			}
			new BukkitRunnable() {
				@Override
				public void run() {
					if (Config.getRestartEnabled()) {
						restart90(true);
					}
				}
			}.runTaskLater(plugin, (parsesdf.parse(nowsdf.format(new Date()) + " " + Config.getRestartTimes().get(list_array_number)).getTime() - new Date().getTime()) / 50);
		} catch (ParseException e) {
			plugin.getLogger().log(Level.SEVERE, "Restart time setting is incorrect", e);
		}
	}

	/*
	public void restart() {
	  Runtime.getRuntime().addShutdownHook(new Thread(
				() -> {
					try {
						new ProcessBuilder().command(getRestartCommand()).inheritIO().start();
					} catch (Throwable e) {
						plugin.getLogger().log(Level.SEVERE, "Restart Failed", e);
					}
				}
		));
		Bukkit.shutdown();
	}

	public List<String> getRestartCommand() {
		File restartscript = new File(Bukkit.getServer().getWorldContainer().getPath() + Config.getRestartScriptPath().replaceFirst(".",""));
		try {
			return Collections.singletonList(createScript(restartscript.exists() && restartscript.isFile() ? Collections.singletonList(restartscript.getAbsolutePath()) : getJavaLaunchCommand()).getAbsolutePath());
		} catch (IOException | PlatformNotSupportedException e) {
			if (restartscript.exists() && restartscript.isFile()) {
				return Collections.singletonList(restartscript.getAbsolutePath());
			} else {
				throw new RuntimeException("Unable to create temporal restart script and server start script doesn't exist", e);
			}
		}
	}

	public static File createScript(List<String> command) throws IOException {
		command = new ArrayList<>(command);
		command.set(0, "\""+ command.get(0) +"\"");
		if (File.separatorChar == '/') {
			return createUnixRestartScrpt(command);
		} else if (File.separatorChar == '\\') {
			return createWindowsRestartScript(command);
		} else {
			throw new PlatformNotSupportedException();
		}
	}

	private List<String> getJavaLaunchCommand() {
		String jarfilename = Bukkit.class.getResource("").getFile();
		jarfilename = jarfilename.substring(0, jarfilename.indexOf(".jar"));
		jarfilename = new File(jarfilename).getName() + ".jar";
		List<String> command = new ArrayList<>();
		command.add("java");
		command.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
		command.add("-jar");
		command.add(jarfilename);
		return command;
	}

	private static File createUnixRestartScrpt(List<String> command) throws IOException {
		File file = new File("aswrestartscript.sh");
		file.createNewFile();
		try (PrintStream stream = new PrintStream(new FileOutputStream(file))) {
			stream.println("#!/bin/bash");
			stream.println("trap : SIGHUP");
			stream.println("while [ -d /proc/"+ ManagementFactory.getRuntimeMXBean().getName().split("@")[0] +" ]; do");
			stream.println("sleep 1");
			stream.println("done");
			stream.println("rm "+"\""+file.getAbsolutePath()+"\"");
			stream.println(StringUtils.join(command, " "));
		}
		file.setExecutable(true);
		return file;
	}

	private static File createWindowsRestartScript(List<String> command) throws IOException {
		File file = new File("aswrestartscript.bat");
		file.createNewFile();
		try (PrintStream stream = new PrintStream(new FileOutputStream(file))) {
			stream.println(":loop");
			stream.println("tasklist | find \" " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0] + " \" >nul");
			stream.println("if not errorlevel 1 (");
			stream.println("timeout /t 1 >nul");
			stream.println("goto :loop");
			stream.println(")");
			stream.println("del "+"\""+file.getAbsolutePath()+"\"");
			stream.println(StringUtils.join(command, " "));
		}
		file.setExecutable(true);
		return file;
	}

	static class PlatformNotSupportedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}*/
}