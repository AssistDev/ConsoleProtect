package me.avastprods.consoleprotect;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class Main extends JavaPlugin implements Listener {
	private String pw = "";
	private boolean pwSet = false;

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
		pw = decode(getConfig().getString("rconpassword"));
		pwSet = getConfig().getBoolean("passwordset");
	}

	public void onDisable() {
		if (pwSet) {
			getConfig().set("rconpassword", encode(pw));
			getConfig().set("passwordset", pwSet);
			saveConfig();
		}
	}

	private String encode(String str) {
		str = Base64Coder.encodeString(str.toString());
		System.out.println(str.charAt(2));
		return str;
	}

	private String decode(String str) {
		str = new String(Base64Coder.decode(str));
		return str;
	}

	@EventHandler
	public void onConsoleCommand(ServerCommandEvent e) {
		CommandSender cmdSender = e.getSender();
		String cmd = e.getCommand();
		String[] args = cmd.split(" ");

		if (!cmd.startsWith("rcon")) {
			cmdSender.sendMessage("[ConsoleProtect] rcon <password> <command>");
			e.setCommand("");
			return;
			
		} else {
			if (!pwSet && args.length == 3) {
				if (args[1].equalsIgnoreCase("setpw")) {
					pw = args[2];
					cmdSender.sendMessage("[ConsoleProtect] Password saved");
					pwSet = true;
				}
			}
			
			if (args.length < 3) {
				cmdSender.sendMessage("[ConsoleProtect] rcon <password> <command>");
				return;
			}

			if (args.length > 2) {
				if (args[1].equals(pw)) {
					String cmdToRun = StringUtils.join(args, ' ', 2, args.length);
					cmdSender.sendMessage("[ConsoleProtect] Password correct! Executing command: " + cmdToRun);
					e.setCommand(cmdToRun);
					
				}
			}
		}
	}
}
