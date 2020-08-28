package fr.frivec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.plugin.java.JavaPlugin;

import fr.frivec.commands.MoveBlockCommand;
import fr.frivec.movement.MoveSave;
import fr.frivec.utils.json.GsonManager;

public class MovingBlocks extends JavaPlugin {
	
	private static MovingBlocks instance;
	
	private GsonManager json;
	
	private Path saveDirectory;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		this.json = new GsonManager();
		
		//Commands
//		this.getCommand("dev").setExecutor(new DevCommand());
		this.getCommand("moveblock").setExecutor(new MoveBlockCommand());
		
		/*
		 * 
		 * Saving files
		 * 
		 */
		
		try {
			
			//Create main file directory
			if(Files.notExists(this.getDataFolder().toPath()))
			
				Files.createDirectory(this.getDataFolder().toPath());
			
			//Create the directory that will contains all the save's files.
			this.saveDirectory = Paths.get(this.getDataFolder().toPath() + "/Saves/");
			
			if(Files.notExists(this.saveDirectory))
				
				Files.createDirectory(this.saveDirectory);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MoveSave.loadMoves();
		
		log("§aPlugin MovingBlocks démarré avec succès !");
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		log("§aPlugin MovingBlock désactivé avec succès.");
		
		super.onDisable();
	}
	
	public static MovingBlocks getInstance() {
		return instance;
	}
	
	public Path getSaveDirectory() {
		return saveDirectory;
	}
	
	public GsonManager getJson() {
		return json;
	}
	
	public static void log(final String message) {
		
		MovingBlocks.getInstance().getServer().getConsoleSender().sendMessage(message);
		
	}

}
