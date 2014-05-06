package me.broswen.bthief;

import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BThief extends JavaPlugin{
	public static BThief plugin;
	PlayerListener playerlistener = new PlayerListener();
	public String prefix = ChatColor.RED + "[BThief] " + ChatColor.RESET;
	public static HashMap<String, Long> thiefList = new HashMap<String, Long>();
	public static HashMap<String, Long> thiefstickList = new HashMap<String, Long>();
	public static Economy econ = null;
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(playerlistener, this);
		this.getCommand("thief").setExecutor(new ThiefCommand(this));
		this.getCommand("bthief").setExecutor(new BThiefCommand(this));
		this.getCommand("thiefbowl").setExecutor(new ThiefStickCommand(this));
		
		this.plugin = this;
		loadConfig();
		
		if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - ECONOMY WILL NOT WORK, VAULT IS NOT FOUND!", getDescription().getName()));
            return;
        }
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return true;
	}
	
	private void loadConfig() {
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		getConfig().options().copyHeader(true);
		saveConfig();		
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public Economy getEconomy(){
		return this.econ;
	}
	
	public void giveMoney(Double money, Player player){
		econ.depositPlayer(player.getName(), money);
	}
	
	public boolean isClose(Player player, Player targetPlayer){
		
		if(player.getLocation().distance(targetPlayer.getLocation()) > 5){
			return false;
		}
		return true;
	}
	
	public boolean hasDrug(Player targetPlayer){
		Inventory inv = targetPlayer.getInventory();
		
		if(inv.contains(Material.SUGAR) || inv.contains(Material.NETHER_STALK) || inv.contains(Material.PUMPKIN_SEEDS) || inv.contains(Material.WHEAT) || inv.contains(Material.MELON_SEEDS) || hasItem(targetPlayer, Material.INK_SACK, (short) 2) || hasItem(targetPlayer, Material.INK_SACK, (short) 3)){
			return true;
		}
	
		return false;
	}

	public boolean hasItem(Player p, Material m, short s){
	    Inventory inv = p.getInventory();
	    for(ItemStack item : inv){
	        //This will return an NullPointerException if you do not have this if statement.
	        if(item != null){
	            if(item.getType() == m && item.getData().getData() == s){
	                return true;
	            }
	        }
	    }
	    return false;
	}

	public void moveDrugs(Player targetPlayer, Player player){
		
		Inventory targetPlayerInventory = targetPlayer.getInventory();
		int targetPlayerInventorySize = targetPlayerInventory.getSize();
		
		for(int i = 0 ; i < targetPlayerInventorySize ; i++) {
			ItemStack item = targetPlayerInventory.getItem(i);
			
			if(item != null){
			    if(item.getType() == Material.INK_SACK && item.getData().getData() == (short) 3 || item.getType() == Material.INK_SACK && item.getData().getData() == (short) 2 || item.getType() == Material.PUMPKIN_SEEDS || item.getType() == Material.MELON_SEEDS || item.getType() == Material.SUGAR || item.getType() == Material.NETHER_STALK || item.getType() == Material.WHEAT || item.getType() == Material.PUMPKIN || item.getType() == Material.SUGAR_CANE){
			    	int amount = item.getAmount();
			    	ItemStack item2 = new ItemStack(item.getType(), amount);
			    	
					short dura = item.getDurability();
					item2.setDurability(dura);
			    	
			    	targetPlayer.getInventory().setItem(i, null);
			    	//targetPlayer.getInventory().remove(item2);
			    	targetPlayer.updateInventory();
			    	
			    	player.getInventory().addItem(item2);
			    	player.updateInventory();
			    	
//			    	if(player.getInventory().firstEmpty() == -1){
//			    		player.getWorld().dropItemNaturally(player.getLocation(), item);
//			    	}else{
//			    		player.getInventory().addItem(item);
//				    	player.updateInventory();
//			    	}
			    }
			}
		}
	}
}
