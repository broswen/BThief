package me.broswen.bthief;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ThiefCommand implements CommandExecutor{
	BThief plugin;
	
	public ThiefCommand(BThief passedPlugin){
		this.plugin = passedPlugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		Economy econ = plugin.getEconomy();
		
		if(!sender.hasPermission("bthief.thief")){
			sender.sendMessage(plugin.prefix + "You don't have permission!");
			return true;
		}
		
		if(!(sender instanceof Player)){
			plugin.getLogger().info("You must be a player to use that command!");
			return true;
		}
		
		final Player player = (Player) sender;
		
		if(args.length != 1){
			player.sendMessage(plugin.prefix + "Format: /thief 'playername'");
			return true;
		}
		
		Player targetPlayer = plugin.getServer().getPlayer(args[0]);
		
		if(targetPlayer == null){
			player.sendMessage(plugin.prefix + ChatColor.GRAY + "That player is not online!");
			return true;
		}
		
		if(targetPlayer == player){
			player.sendMessage(plugin.prefix + ChatColor.GRAY + "You may not steal from yourself!");
			return true;
		}
		
		if(targetPlayer.hasPermission("bthief.exempt")){
			player.sendMessage(plugin.prefix + ChatColor.GRAY + "That player may not be stolen from!");
			return true;
		}
		
		if(targetPlayer.hasPermission("bthief.police")){
			player.sendMessage(plugin.prefix + ChatColor.GRAY + "That player is not a corrupt police officer!");
			return true;
		}
		
		if(player.getLocation().distance(targetPlayer.getLocation()) > 5){
			player.sendMessage(plugin.prefix + ChatColor.GRAY + "That player is out of range!");
			return true;
		}
		
		if(plugin.getConfig().getBoolean("use-thief-delay")){
			if(plugin.thiefList.containsKey(player.getName())){
				player.sendMessage(plugin.prefix + ChatColor.GRAY + "You must wait " + plugin.getConfig().getInt("thief-delay") + " seconds between each theft!");
				return true;
			}
			
			plugin.thiefList.put(player.getName(), null);
			
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					plugin.thiefList.remove(player.getName());
				}
				
			}, plugin.getConfig().getInt("thief-delay") * 20);
			
		}
		
		if(plugin.hasDrug(targetPlayer)){
			plugin.moveDrugs(targetPlayer, player);
			
			player.sendMessage(plugin.prefix + "You stole some drugs from " + targetPlayer.getName() + "!");
			
			if(plugin.getConfig().getBoolean("use-economy")){
				player.sendMessage(plugin.prefix + "You stole $" + plugin.getConfig().getDouble("theft-reward")+ " from " + targetPlayer.getName() + "!");
				plugin.giveMoney(plugin.getConfig().getDouble("theft-reward"), player);
			}
			
			targetPlayer.sendMessage(plugin.prefix + player.getName() + " stole some drugs from you!");
			
			player.playSound(player.getLocation(), Sound.SHEEP_SHEAR, 2, -1);
			targetPlayer.playSound(targetPlayer.getLocation(), Sound.SHEEP_SHEAR, 2, -1);
		}else{
			player.sendMessage(plugin.prefix + targetPlayer.getName() + " didn't have any drugs! You were sent to jail for attempted theft!");
			
			if(plugin.getConfig().getBoolean("use-jail-plugin")){
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "jail " + player.getName() + " " + plugin.getConfig().getString("jail-name") + " " + plugin.getConfig().getInt("jail-time") + "s");
			}else{
				Location jailLocation = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("jail-location.world")), plugin.getConfig().getDouble("jail-location.x"), plugin.getConfig().getDouble("jail-location.y"), plugin.getConfig().getDouble("jail-location.z"));
				player.teleport(jailLocation);
			}
		}
		
		return true;
	}
}
