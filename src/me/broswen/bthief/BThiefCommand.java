package me.broswen.bthief;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class BThiefCommand implements CommandExecutor{
	BThief plugin;
	
	public BThiefCommand(BThief passedPlugin){
		this.plugin = passedPlugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("help")){
				if(!sender.hasPermission("bthief.help")){
					sender.sendMessage(plugin.prefix + "You don't have permission!");
					return true;
				}
				
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					player.sendMessage(plugin.prefix + "/thief 'player' - steals from a player.");
					player.sendMessage(plugin.prefix + "/bthief 'reload' - reloads the plugin.");
					player.sendMessage(plugin.prefix + "/bthief 'info' - shows info about the plugin.");
					player.sendMessage(plugin.prefix + "/bthief 'help' - shows this help screen.");
				}else{
					plugin.getLogger().info("/frisk 'player' - steals from a player.");
					plugin.getLogger().info("/bthief 'reload' - reloads the plugin.");
					plugin.getLogger().info("/bthief 'info' - shows info about the plugin.");
					plugin.getLogger().info("/bthief 'help' - shows this help screen.");
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("info")){
				if(!sender.hasPermission("bthief.info")){
					sender.sendMessage(plugin.prefix + "You don't have permission!");
					return true;
				}
				
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					player.sendMessage(plugin.prefix + "Version: " + pdfFile.getVersion());
					player.sendMessage(plugin.prefix + "Author: " + pdfFile.getAuthors());
					player.sendMessage(plugin.prefix + "About: " + pdfFile.getDescription());
				}else{
					
					plugin.getLogger().info("Version: " + pdfFile.getVersion());
					plugin.getLogger().info("Author: " + pdfFile.getAuthors());
					plugin.getLogger().info("About: " + pdfFile.getDescription());
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload")){
				if(!sender.hasPermission("bthief.reload")){
					sender.sendMessage(plugin.prefix + "You don't have permission!");
					return true;
				}
				
				plugin.reloadConfig();
				
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					player.sendMessage(plugin.prefix + "The config was reloaded!");
				}else{
					sender.sendMessage("The config was reloaded!");
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("setjail")){
				if(!sender.hasPermission("bthief.setjail")){
					sender.sendMessage(plugin.prefix + "You don't have permission!");
					return true;
				}
				
				if(sender instanceof Player){
					Player player = (Player) sender;
					String worldName = player.getWorld().getName();
					Double playerX = player.getLocation().getX();
					Double playerY = player.getLocation().getY();
					Double playerZ = player.getLocation().getZ();
					
					plugin.getConfig().set("jail-location.world", worldName);
					plugin.getConfig().set("jail-location.x", playerX);
					plugin.getConfig().set("jail-location.y", playerY);
					plugin.getConfig().set("jail-location.z", playerZ);
					
					plugin.saveConfig();
					plugin.reloadConfig();
					
					player.sendMessage(plugin.prefix + "The jail coordinates were set to your current position!");
				}else{
					plugin.getLogger().info("You must be a player to use this command!");
				}
				return true;
			}
		}
		
		if(sender instanceof Player){
			Player player = (Player) sender;
			
			player.sendMessage(plugin.prefix + "type '/bthief help' for more commands.");
			
		}else{
			plugin.getLogger().info("type '/bthief help' for more commands.");
		}
		
		return true;
	}

}
