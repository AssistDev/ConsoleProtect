package me.avastprods.consoleprotect;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	private string pw = "":
	private boolean pwSet = false;
	public void onEnable() {
	    getServer().getPluginManager().registerEvents(this, this);
	    getConfig().options().copyDefaults(true);
	    saveConfig();
	    pw = decode(getConfig().getString("rconpassword"));
	    pwSet = getConfig().getBoolean("passwordset");
	    Filter f = new Filter(){
            public boolean isLoggable(LogRecord line) {
            if (line.getMessage().contains("rcon")) {
                return false;
            }
                return true;
            }
            public String doFilter(String arg0) {
                return null;
            }
            public String doFilterUrl(String arg0) {
                return null;
            }};
            log.setFilter(f);
	}
	public void onDisable(){
	    if (pwSet){
		getConfig().set("rconpassword", encode(pw));
		getConfig().set("passwordset", pwSet);
		saveConfig();
	    }
	}
	private String encode(String str) {
            BASE64Encoder encoder = new BASE64Encoder();
	    str = encoder.encodeBuffer(str.getBytes());
            return str;
	}

    	private String decode(String str) {
            BASE64Decoder decoder = new BASE64Decoder();
	    try {
                str = new String(decoder.decodeBuffer(str));
	    } catch (IOException e) {
        	e.printStackTrace();
	    }
            return str;
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
			if (!pwSet && args.length == 3){
					if (args[1].equalsIgnoreCase("setPW"){
						pw = args[2];
						cmdSender.sendMessage("[ConsoleProtect] Password Saved");
						pwSet = true;
					}
				}
			if(args.length < 3) {
				cmdSender.sendMessage("[ConsoleProtect] rcon <password> <command>");
				return;
			}
			
			if(args.length > 2) {
				
				if(args[1].equals(pw)) {
					String cmdToRun = StringUtils.join(args, ' ', 2, args.length);
					cmdSender.sendMessage("[ConsoleProtect] Password correct! Executing command: " + cmdToRun);
					e.setCommand(cmdToRun);
				}
			}
		}
	}
}
