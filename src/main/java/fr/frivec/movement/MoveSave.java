package fr.frivec.movement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import fr.frivec.MovingBlocks;

public class MoveSave extends BukkitRunnable {
	
	private transient static Path folder = MovingBlocks.getInstance().getSaveDirectory();
	
	private transient static Map<String, MoveSave> saves = new HashMap<>();
	
	private transient boolean running = false;
	private transient Mouvement currentMouvement = null;
	private transient int currentIndex = 0;	
	
	private Material originalBlockType;
	
	private String name;
	private Block block;
	
	private boolean autoStart = false, reverse = false;
	
	private HashMap<Integer, Point> points;
	
	public MoveSave(final String name, final Block block) {
		
		this.name = name;
		this.block = block;
		this.points = new HashMap<>();
		
		this.originalBlockType = block.getType();
		
		saves.put(this.name.toLowerCase(), this);
		
	}
	
	@Override
	public void run() {
		
		final int toIndex = this.currentIndex + 1;
		
		if(this.points.size() < toIndex) {
			
			this.stop();
			
			return;
			
		}
		
		if(this.currentMouvement == null) {
			
			final Point from = this.points.get(this.currentIndex),
						to = this.points.get(toIndex);
						
			this.currentMouvement = new Mouvement(this.block, from.getLocation(), to.getLocation());
			
			this.currentMouvement.start();
			this.currentMouvement.run();
			
		}else
			
			if(this.currentMouvement.isRunning())
				
				this.currentMouvement.run();
		
			else {
				
				this.currentMouvement = null;
				
				this.currentIndex++;
				this.block.setType(this.originalBlockType);
				
			}
		
	}
	
	public void stop() {
		
		this.running = false;
		this.cancel();
		
		if(this.reverse) {
			
			final HashMap<Integer, Point> newMap = new HashMap<>();
			
			int firstIndex = -1,
				lastIndex = -1;
			
			for(Entry<Integer, Point> entry : this.points.entrySet()) {
				
				if(firstIndex == -1)
					
					firstIndex = entry.getKey();
				
				if(lastIndex < entry.getKey())
					
					lastIndex = entry.getKey();
				
			}
			
			int newIndex = firstIndex;
			
			for(int i = lastIndex; i > firstIndex; i--) {
				
				newMap.put(newIndex, this.points.get(i));
				
				newIndex++;
				
			}	
			
			this.points.clear();
			this.points = newMap;
			
		}
		
		if(this.autoStart)
			
			this.start();
		
	}
	
	public void start() {
		
		if(!this.running) {
			
			for(Entry<Integer, Point> entry : this.points.entrySet()) {
				
				final int index = entry.getKey();
				
				if(this.currentIndex == 0) {
					
					this.currentIndex = index;
					break;
				
				}
				
			}
			
			this.running = true;
			this.runTaskTimer(MovingBlocks.getInstance(), 0l, 1l);
			
		}else
			
			return;
		
	}
	
	public void delete() throws IOException {
		
		final Path file = Paths.get(folder + "/" + this.name + ".json");
		
		if(Files.notExists(file))
			
			throw new FileNotFoundException();
		
		Files.delete(file);
		
		saves.remove(this.name.toLowerCase());
		
	}
	
	public void save() throws IOException {
		
		final Path file = Paths.get(folder + "/" + this.name + ".json");
		
		if(Files.notExists(file))
			
			Files.createFile(file);
		
		final BufferedWriter writer = Files.newBufferedWriter(file);
		final String json = MovingBlocks.getInstance().getJson().serializeObject(this);
		
		writer.write(json);
		writer.flush();
		writer.close();
		
		return;
		
	}
	
	public static void loadMoves() {
		
		try (final DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
			
			for(Path files : stream) {
				
				if(!Files.isDirectory(files)) {
				
					final BufferedReader reader = Files.newBufferedReader(files);
					final StringBuilder builder = new StringBuilder();
					
					String line = null;
					
					while((line = reader.readLine()) != null)
						
						builder.append(line);
					
					reader.close();
					
					final MoveSave save = (MoveSave) MovingBlocks.getInstance().getJson().deSeralizeJson(builder.toString(), MoveSave.class);
					
					saves.put(save.getName(), save);
					
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addPoint(final Location location) {
		
		setPoint(this.points.size() + 1, location);
		
	}
	
	public void removePoint(final int index, final boolean moveOthers) {
		
		this.points.remove(index);
		
		if(moveOthers) {
		
			for(int i = index; i < this.points.size(); i++) {
				
				final Point point = this.points.get(i);
				
				this.points.remove(i);
				this.points.put(i - 1, point);
				
			}
			
		}
		
	}
	
	public void setPoint(int index, final Location location) {
		
		if(this.points.containsKey(index))
			
			this.points.remove(index, false);
		
		this.points.put(index, new Point(location));
		
	}
	
	public Location getPointLocation(final int index) {
		
		if(this.points.size() <= index - 1)
			
			return null;
		
		return this.points.get(index - 1).getLocation();
		
	}
	
	public void setBlock(Block block) throws IOException {
	
		this.block = block;
		
		save();
		
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean isAutoStart() {
		return autoStart;
	}
	
	public boolean isReverse() {
		return reverse;
	}
	
	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}
	
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public String getName() {
		return name;
	}
	
	public HashMap<Integer, Point> getPoints() {
		return points;
	}
	
	public static Map<String, MoveSave> getSaves() {
		return saves;
	}
	
	private class Point {
		
		private String worldName;
		private double x, y, z;
		
		public Point(final Location location) {
			
			this.worldName = location.getWorld().getName();
			
			this.x = location.getX();
			this.y = location.getY();
			this.z = location.getZ();
			
		}
		
		@Override
		public String toString() {
			return "world: " + worldName + " | x: " + x + " y: " + y + " z: " + z;
		}
		
		public Location getLocation() {
			
			return new Location(Bukkit.getWorld(this.worldName), x, y, z);
			
		}
		
	}

}
