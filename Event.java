package Mopslon.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import Mopslon.main.Main;
import Mopslon.msql.Mysql;


public class Event  implements Listener{
	static FileConfiguration config = Main.getPlugin().getConfig();
	
    public static boolean isUserexsist(UUID uuid) {
    	try {
			PreparedStatement ps = Mysql.getConnection().prepareStatement("SELECT UnbanTimestamp FROM Deth_Ban WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
    }
    
    public static void setUnbanTimestamp(UUID uuid) {
		Integer time = config.getInt("Länge");
		long Time1 = System.currentTimeMillis() + time*1000;
    	if(!isUserexsist(uuid)) {
        	try {
        		PreparedStatement ps = Mysql.getConnection().prepareStatement("INSERT INTO Deth_Ban (UUID,UnbanTimestamp) VALUES (?,?)");
        		ps.setString(1,uuid.toString());
        		ps.setLong(2,Time1);
        		ps.executeUpdate();
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
    	}else {
        	try {
        		PreparedStatement ps = Mysql.getConnection().prepareStatement("UPDATE Deth_Ban SET UnbanTimestamp = ? Where UUID = ?");
        		ps.setString(2,uuid.toString());
        		ps.setLong(1,Time1);
        		ps.executeUpdate();
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
    	}
    }
    	
	
	public static long getUnbanTimestamp(UUID uuid) {
		try {
			PreparedStatement ps = Mysql.getConnection().prepareStatement("SELECT UnbanTimestamp FROM Deth_Ban WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return  rs.getLong("UnbanTimestamp");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public static long getDeaths(UUID uuid) {
		try {
			PreparedStatement ps = Mysql.getConnection().prepareStatement("SELECT Death FROM Deth_Ban WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return  rs.getLong("Death");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	

    public static void setDeaths(UUID uuid) {
    	try {
    		PreparedStatement ps = Mysql.getConnection().prepareStatement("UPDATE Deth_Ban SET Death = ? Where UUID = ?");
    		ps.setLong(1,getDeaths(uuid) + 1);
    		ps.setString(2,uuid.toString());
    		ps.executeUpdate();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    
	@EventHandler 
	public void onjoin(PlayerJoinEvent event) {
		if(!config.getString("MySQL.password").equals("null")) {
			Player player = event.getPlayer();
	    	if(getUnbanTimestamp(player.getUniqueId()) >= System.currentTimeMillis()) {
	    		long time1 = getUnbanTimestamp(player.getUniqueId()) - System.currentTimeMillis();
	    		long time = time1/1000 ;
		    	event.setJoinMessage(null);
				player.kickPlayer(ChatColor.DARK_RED + "You have been time Banned for "+ time +" seconds");
			}
		}else System.out.println(ChatColor.DARK_RED + "[MSQL] Setze das password etz in der config!");
	}
	
	@EventHandler
	public void OnDeth(PlayerDeathEvent event) {
		if(config.get("active").equals(true)) {
			if(!config.getString("MySQL.password").equals("null")) {
				Player player = event.getEntity();
				setDeaths(player.getUniqueId());
				if(!event.getDrops().isEmpty()) {
					player.getInventory().clear();
				}
				Integer time = config.getInt("Länge");
				setUnbanTimestamp(player.getUniqueId());
				player.kickPlayer(ChatColor.DARK_RED + "You have been time Banned for "+ time +" seconds!");
			}else System.out.println(ChatColor.DARK_RED + "[MSQL] Setze das password etz in der config!");
		}
	}
}
