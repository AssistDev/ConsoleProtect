package me.avastprods.consoleprotect;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	@EventHandler
	public void onConsoleCommand(ServerCommandEvent e) {
		CommandSender cmdSender = e.getSender();
		String cmd = e.getCommand();
		String[] args = cmd.split(" ");
		
		if(!cmd.startsWith("rcon")) {
			cmdSender.sendMessage("[ConsoleProtect] rcon <password> <command>");
			e.setCommand("");
			return;
		} else {
			if(args.length < 3) {
				cmdSender.sendMessage("[ConsoleProtect] rcon <password> <command>");
				return;
			}
			
			if(args.length > 2) {
				if(args[1].equals(getConfig().getString("rconpassword"))) {
					String cmdToRun = StringUtils.join(args, ' ', 2, args.length);
					cmdSender.sendMessage("[ConsoleProtect] Password correct! Executing command: " + cmdToRun);
					e.setCommand(cmdToRun);
				}
			}
		}
	}
}
