package me.broswen.bthief;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener{
	public BThief plugin;
	
	public ItemStack friskstick = new ItemStack(Material.STICK);
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		Entity targetPlayer = event.getRightClicked();
		Player player = event.getPlayer();
		this.plugin = BThief.plugin;
		
		if(!(targetPlayer instanceof Player)){
			return;
		}
		
		if(player.getInventory().getItemInHand() == null || player.getInventory().getItemInHand().getItemMeta() == null || player.getInventory().getItemInHand().getItemMeta().getDisplayName() == null){
			return;
		}
		
		if(!(player.getInventory().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Bowl of Theft"))){
			player.sendMessage("false");
			return;
		}
		
		if(!player.hasPermission("bthief.thief")){
			player.sendMessage(plugin.prefix + ChatColor.GRAY + "You don't have permission to use this!");
			return;
		}
		
		String targetPlayerName = ((HumanEntity) targetPlayer).getName();
		Bukkit.dispatchCommand(player, "thief " + targetPlayerName);
	}
}
