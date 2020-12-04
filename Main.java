package Mopslon.main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Mopslon.command.Command;
import Mopslon.event.Event;
import Mopslon.msql.Mysql;

public class Main extends JavaPlugin{
	
	private static Main plugin;

	public void onEnable() {
		plugin = this;

		PluginManager PluginManiger = Bukkit.getPluginManager();
		PluginManiger.registerEvents( new Event(), this);
		
		getCommand("Deathban").setExecutor((CommandExecutor) new Command());
		
		FileConfiguration config = Main.getPlugin().getConfig();
		
		if(!config.contains("active")) {
		config.set("active", true);
		Main.getPlugin().saveConfig();
			}
		
		if(!config.contains("MySQL.host")) {
			config.set("MySQL.host", "127.0.0.1");
			Main.getPlugin().saveConfig();
			}
		if(!config.contains("MySQL.port")) {
			config.set("MySQL.port", "3306");
			Main.getPlugin().saveConfig();
			}
		if(!config.contains("MySQL.database")) {
			config.set("MySQL.database", "sys");
			Main.getPlugin().saveConfig();
			}
		if(!config.contains("MySQL.username")) {
			config.set("MySQL.username", "root");
			Main.getPlugin().saveConfig();
			}
		if(!config.contains("MySQL.password")) {
			config.set("MySQL.password", "null");
			Main.getPlugin().saveConfig();
			}
		
		if(!config.contains("Länge")) {
		config.set("Länge", 300);
		Main.getPlugin().saveConfig();
			}

		if(!config.getString("MySQL.password").equals("null")) {
			
			try {
				Mysql.connect();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			
			try {
				PreparedStatement ps = Mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Deth_Ban (UUID VARCHAR(100), UnbanTimestamp BIGINT, Death BIGINT);");
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else System.out.println(ChatColor.DARK_RED + "[MSQL] Setze das password etz in der config!");
	}
	
	public void onDisEnable() {
		Mysql.disconnect();
	}
	
	public static Main getPlugin() {
		return plugin;
	}
}