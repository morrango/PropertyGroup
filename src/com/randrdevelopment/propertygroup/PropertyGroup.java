package com.randrdevelopment.propertygroup;

import java.io.File;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;
import com.randrdevelopment.propertygroup.command.CommandManager;
import com.randrdevelopment.propertygroup.command.commands.AddToPropertyCommand;
import com.randrdevelopment.propertygroup.command.commands.CreateGroupCommand;
import com.randrdevelopment.propertygroup.command.commands.CreatePropertyCommand;
import com.randrdevelopment.propertygroup.command.commands.DeletePropertyCommand;
import com.randrdevelopment.propertygroup.command.commands.HideRegionCommand;
import com.randrdevelopment.propertygroup.command.commands.ListGroupsCommand;
import com.randrdevelopment.propertygroup.command.commands.ReloadConfigCommand;
import com.randrdevelopment.propertygroup.command.commands.SaveGroupCommand;
import com.randrdevelopment.propertygroup.command.commands.SetSizeCommand;
import com.randrdevelopment.propertygroup.command.commands.SetStartPointCommand;
import com.randrdevelopment.propertygroup.command.commands.ShowRegionCommand;
import com.randrdevelopment.propertygroup.command.commands.TeleportCommand;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class PropertyGroup extends JavaPlugin {
	Logger Log = Logger.getLogger("Minecraft");
	private static WorldEditPlugin pluginWorldEdit;
	private static Essentials pluginEssentials;
	private static WorldGuardPlugin pluginWorldGuard;
	private static PropertyGroup instance;
	private CommandManager commandManager;
	private File propertyConfigFile = null;
	private File defaultConfigFile = null;
	private String propertyGroup = null;
	private Set<int[]> blocks = null;
	private PropertyGroupConfig defaultConfig = null;
	private PropertyGroupConfig propertyConfig = null;
	
	public void onEnable(){ 
		instance = this;
		Log.info("[PropertyGroup] Starting Property Groups");
		
		loadConfiguration();
		registerCommands();
		loadEssentials();
		loadWorldEdit();
		loadWorldGuard();
		
		Log.info("[PropertyGroup] Property Groups plugin succesfully enabled!");
	}
	 
	public void onDisable(){ 
		Log.info("[PropertyGroup] Property Groups plugin succesfully disabled!");
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandManager.dispatch(sender, command, label, args, this);
    }
    
    private void loadConfiguration(){
    	// Setup YML Files
    	defaultConfigFile = new File(getDataFolder(), "config.yml");
    	propertyConfigFile = new File(getDataFolder(), "propertygroups.yml");
    	
    	// Setup default configuration
    	defaultConfig = new PropertyGroupConfig(defaultConfigFile);
    	defaultConfig.setTemplateName("/config.yml", PropertyGroup.class);
    	defaultConfig.load();
		
		// Setup property groups configuration
    	propertyConfig = new PropertyGroupConfig(propertyConfigFile);
    	propertyConfig.load();
    }
    
    public PropertyGroupConfig getDefaultConfig(){
    	return defaultConfig;
    }
    
    public PropertyGroupConfig getPropertyConfig(){
    	return propertyConfig;
    }
    
	private void registerCommands() {
        commandManager = new CommandManager();
        // Load Commands
        commandManager.addCommand(new AddToPropertyCommand(this));
        commandManager.addCommand(new CreateGroupCommand(this));
        commandManager.addCommand(new CreatePropertyCommand(this));
        commandManager.addCommand(new DeletePropertyCommand(this));
        //commandManager.addCommand(new HelpCommand(this));
        commandManager.addCommand(new HideRegionCommand(this));
        commandManager.addCommand(new ListGroupsCommand(this));
        commandManager.addCommand(new ReloadConfigCommand(this));
        commandManager.addCommand(new SaveGroupCommand(this));
        //commandManager.addCommand(new SetCommand(this));
        commandManager.addCommand(new SetSizeCommand(this));
        commandManager.addCommand(new SetStartPointCommand(this));
        commandManager.addCommand(new ShowRegionCommand(this));
        commandManager.addCommand(new TeleportCommand(this));
    }
	
	public static PropertyGroup getInstance() {
		return instance;
	}
	
	private void loadWorldEdit() {
		Plugin p = this.getServer().getPluginManager().getPlugin("WorldEdit");
		if (p != null && p instanceof WorldEditPlugin) {
			pluginWorldEdit = (WorldEditPlugin) p;
			//Cuboid.setWorldEdit(worldEditPlugin);
			Log.info("[PropertyGroup] WorldEdit plugin detected");
		} else {
			Log.info("[PropertyGroup] WorldEdit plugin not detected - This will cause problems...");
		}
	}
	
	private void loadWorldGuard() {
		Plugin p = this.getServer().getPluginManager().getPlugin("WorldGuard");
		if (p != null && p instanceof WorldGuardPlugin) {
			pluginWorldGuard = (WorldGuardPlugin) p;
			Log.info("[PropertyGroup] WorldGuard plugin detected");
		} else {
			Log.info("[PropertyGroup] WorldGuard plugin not detected, Region Support Disabled");
		}
	}
	
	private void loadEssentials(){
		Plugin p = this.getServer().getPluginManager().getPlugin("Essentials");
		if (p != null) {
			pluginEssentials = (Essentials) p;
			Log.info("[PropertyGroup] Essentials Found");
		}
		else
		{
			Log.info("[PropertyGroup] Essentials not found, Essentials Support Disabled");
		}
	}
	
	public static WorldEditPlugin getWorldEdit() {
		return pluginWorldEdit;
	}
	
	public static WorldGuardPlugin getWorldGuard() {
		return pluginWorldGuard;
	}
	
	public static Essentials getEssentials() {
		return pluginEssentials;
	}

	public String getTag() {
		String tag = ChatColor.GREEN+"[PropertyGroup] "+ChatColor.AQUA;
		return tag;
	}
	
	public void setPropertyName(String propertyName) {
		propertyGroup = propertyName;
	}
	
	public String getPropertyName() {
		return propertyGroup;
	}
	
	public void setBlockData(Set<int[]> blockdata) {
		blocks = blockdata;
	}
	
	public Set<int[]> getBlockData() {
		return blocks;
	}
}
