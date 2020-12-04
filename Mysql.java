package Mopslon.msql;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.Connection;

import Mopslon.main.Main;

public class Mysql extends JavaPlugin{
	
	static FileConfiguration config = Main.getPlugin().getConfig();
	public static Connection con;
	
	public static void connect() throws ClassNotFoundException {
		if(!isConnected()) {
			String host = config.getString("MySQL.host");
			String port = config.getString("MySQL.port");
			String database = config.getString("MySQL.database");
			String username = config.getString("MySQL.username");
			String password = config.getString("MySQL.password");
			
			try {
				con = (Connection) DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username , password);
				System.out.println("[MSQL] verbindung wurde aufgebaut!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void disconnect() {
		if(isConnected()) {
			try {
				con.close();
				System.out.println("[MSQL] verbindung getrennt!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Boolean isConnected() {
		return(con == null ? false : true);
	}
	
	public static Connection getConnection() {
		return con;
	}

}
