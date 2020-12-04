package Mopslon.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import Mopslon.main.Main;

public class Command implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("op")) {
				if(args.length == 0) {
					FileConfiguration config = Main.getPlugin().getConfig();
					
					if(!config.get("active").equals(true)) {
						
						config.set("active", true);
						Main.getPlugin().saveConfig();
						player.sendMessage("§3Deathban is active");
						
					}else {
						
						config.set("active", false);
						Main.getPlugin().saveConfig();
						player.sendMessage("§3Deathban isn't active");
						
					}
					
					
					
				}else
					player.sendMessage("§cBitte benutze §3/Deathban§c!");
			}else
				player.sendMessage("§cDu hast keine rechte dafür!");
		}
		return false;
	}
}