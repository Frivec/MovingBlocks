package fr.frivec.movement;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Shulker;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import fr.frivec.MovingBlocks;

public class Mouvement {
	
	private Block block;
	
	private Location from, to;
	
	private transient int timer = 1000;
	private transient double speed = 0.05;
	
	private transient Shulker shulker;
	private transient FallingBlock fBlock;
	private transient ArmorStand stand;
	
	private transient boolean running;
	
	public Mouvement(Block block, final Location from, final Location to) {
		
		setFrom(from);
		setTo(to);
		
		this.block = block;
			
		final World world = block.getWorld();
		final FallingBlock fallingBlock = world.spawnFallingBlock(this.getFrom(), block.getBlockData());
			
		fallingBlock.setFallDistance(-1000000);
			
		this.fBlock = fallingBlock;
			
		this.block.setType(Material.AIR);
		
		final Shulker shulker = (Shulker) world.spawnEntity(this.getFrom(), EntityType.SHULKER);
			
		shulker.setAI(false);
		shulker.setInvulnerable(true);
		shulker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 0, true, false, false));
		
		this.shulker = shulker;
			
		final ArmorStand armorStand = (ArmorStand) world.spawnEntity(this.getFrom().subtract(0, 1, 0), EntityType.ARMOR_STAND);
			
		armorStand.setGravity(false);
		armorStand.setVisible(false);
		armorStand.setAI(false);
			
		armorStand.addPassenger(shulker);
		armorStand.addPassenger(fallingBlock);
			
		this.stand = armorStand;
		
	}
	
	public void start() {
		
		this.running = true;
		
	}
	
	public void stop() {
		
		this.running = false;
			
		for(Entity passengers : this.stand.getPassengers())
				
			this.stand.removePassenger(passengers);
			
		this.stand.remove();
			
		this.shulker.remove();
		
		this.fBlock.remove();
		
	}
	
	public void run() {
		
		if(!this.running) {
			
			stop();
			
			return;
			
		}
			
		for(Entity passengers : this.stand.getPassengers())
				
			this.stand.removePassenger(passengers);
			
		final Location teleportation = new Location(this.stand.getWorld(), this.stand.getLocation().getX(), this.stand.getLocation().getY(), this.stand.getLocation().getZ()),
							fBlockPosition = teleportation.clone().add(0, 1, 0);
		final Vector translation = new Vector(this.getTo().getX() - fBlockPosition.getX(), this.getTo().getY() - fBlockPosition.getY(), this.getTo().getZ() - fBlockPosition.getZ());
			
		final double distance = translation.length();
			
		if(Math.round(translation.getX()) == 0 && Math.round(translation.getY()) == 0 && Math.round(translation.getZ()) == 0) {
				
			Bukkit.broadcastMessage("§aEnd of mouvement.");
			this.stop();
				
		}
			
		if(translation.getX() != 0)
			
			teleportation.setX(teleportation.getX() + (double) translation.getX() / distance * this.speed);
				
		if(translation.getY() != 0)
					
			teleportation.setY(teleportation.getY() + (double) translation.getY() / distance * this.speed);
			
		if(translation.getZ() != 0)
					
			teleportation.setZ(teleportation.getZ() + (double) translation.getZ() / distance * this.speed);
			
		this.stand.teleport(teleportation);
			
		this.stand.addPassenger(this.shulker);
		this.stand.addPassenger(this.fBlock);
		
		if(this.timer == 0)
			
			this.stop();
		
		timer--;
		
	}
	
	public String toJson() {
		
		return MovingBlocks.getInstance().getJson().serializeObject(this);
		
	}
	
	public static Mouvement fromJson(final String json) {
		
		return (Mouvement) MovingBlocks.getInstance().getJson().deSeralizeJson(json, Mouvement.class);
		
	}
	
	public Block getBlock() {
		return block;
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}

	public Location getFrom() {
		return this.from;
	}

	public void setFrom(Location from) {
		
		this.from = from;
		
	}

	public Location getTo() {
		
		return this.to;
	
	}

	public void setTo(Location to) {
		
		this.to = to;
		
	}

	public int getTimer() {
		return timer;
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}
	
}
