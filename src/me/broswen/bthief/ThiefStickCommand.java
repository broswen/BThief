package me.broswen.bthief;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ThiefStickCommand implements CommandExecutor{
	BThief plugin;
	public ItemStack thiefstick = new ItemStack(Material.BOWL);
	
	public ThiefStickCommand(BThief passedPlugin){
		this.plugin = passedPlugin;
		
		ItemMeta im = thiefstick.getItemMeta();
		String thiefItemName = ChatColor.RED + "Bowl of Theft";
		im.setDisplayName(thiefItemName);
		thiefstick.setItemMeta(im);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if(!sender.hasPermission("bthief.thiefstick")){
			sender.sendMessage(plugin.prefix + "You don't have permission!");
			return true;
		}
		
		if(sender instanceof Player){
			final Player player = (Player) sender;
			
			if(plugin.getConfig().getBoolean("use-thiefstick-delay")){
				if(plugin.thiefstickList.containsKey(player.getName())){
					player.sendMessage(plugin.prefix + ChatColor.GRAY + "You must wait " + plugin.getConfig().getInt("thiefstick-delay") + " seconds between each spawn!");
					return true;
				}
				
				plugin.thiefstickList.put(player.getName(), null);
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

					@Override
					public void run() {
						plugin.thiefstickList.remove(player.getName());
						
					}
					
				}, plugin.getConfig().getInt("thiefstick-delay") * 20);
			}
			
			player.sendMessage(plugin.prefix + "You recieved a Bowl of Theft!");
			player.getInventory().addItem(thiefstick);
		}else{
			plugin.getLogger().info("You must be a player to use this command!");
		}
		
		return true;
	}
}
