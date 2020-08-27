package fr.frivec.commands.dev;

import java.util.ArrayList;
import java.util.List;

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
import org.bukkit.scheduler.BukkitRunnable;

import fr.frivec.MovingBlocks;

public class DevMouvement extends BukkitRunnable {
	
	private Block[] blocks;
	private Location from;
	
	private int timer = 9000;
	private double speed = 0.10;
	
	private List<ArmorStand> stands;
	private List<FallingBlock> fBlocks;
	private List<Shulker> shulkers;
	
	public DevMouvement(final Block[] blocks, final Location from, final Location to) {
		
		this.blocks = blocks;
		this.from = from;
		
		this.shulkers = new ArrayList<>();
		this.fBlocks = new ArrayList<>();
		this.stands = new ArrayList<>();
		
		for(Block block : this.blocks) {
			
			final World world = block.getWorld();
			final FallingBlock fallingBlock = world.spawnFallingBlock(this.from, block.getBlockData());
			
			fallingBlock.setFallDistance(-1000000);
			
			this.fBlocks.add(fallingBlock);
			
			block.setType(Material.AIR);
			
			final Shulker shulker = (Shulker) world.spawnEntity(this.from, EntityType.SHULKER);
			
			shulker.setAI(false);
			shulker.setInvulnerable(true);
			shulker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 0, true, false, false));
			
			this.shulkers.add(shulker);
			
			final ArmorStand armorStand = (ArmorStand) world.spawnEntity(this.from.subtract(0, 1, 0), EntityType.ARMOR_STAND);
			
			armorStand.setGravity(false);
			armorStand.setVisible(false);
			armorStand.setAI(false);
			
			armorStand.addPassenger(shulker);
			armorStand.addPassenger(fallingBlock);
			
			this.stands.add(armorStand);
			
		}
		
	}
	
	public void start() {
		
		this.runTaskTimer(MovingBlocks.getInstance(), 0l, 1l);
		
	}
	
	public void stop() {
		
		this.cancel();
		
		for(ArmorStand armorStand : this.stands) {
			
			for(Entity passengers : armorStand.getPassengers())
				
				armorStand.removePassenger(passengers);
			
			armorStand.remove();
			
		}
		
		for(Shulker shulkers : this.shulkers)
			
			shulkers.remove();
		
		for(FallingBlock fallingBlock : this.fBlocks)
			
			fallingBlock.remove();
		
		this.stands.clear();
		this.shulkers.clear();
		this.fBlocks.clear();
		
	}
	
	@Override
	public void run() {
	
		for(int i = 0; i < this.blocks.length; i++) {
			
			final ArmorStand armorStand = this.stands.get(i);
			final Shulker shulker = this.shulkers.get(i);
			final FallingBlock fallingBlock = this.fBlocks.get(i);
			
			for(Entity passengers : armorStand.getPassengers())
				
				armorStand.removePassenger(passengers);
			
			final Location teleportation = new Location(armorStand.getWorld(), armorStand.getLocation().getX(), armorStand.getLocation().getY(), armorStand.getLocation().getZ());
			
			teleportation.add(speed, Math.sin(teleportation.getX() + speed), 0);
			
			armorStand.teleport(teleportation);
			
			armorStand.addPassenger(shulker);
			armorStand.addPassenger(fallingBlock);
			
		}
		
		if(this.timer == 0) {
			
			Bukkit.broadcastMessage("§cThe block hasn't been stopped in the right place. Timed out.");
			this.stop();
			
		}
		
		timer--;
		
	}

	public Block[] getBlocks() {
		return blocks;
	}

	public void setBlocks(Block[] blocks) {
		this.blocks = blocks;
	}

	public Location getFrom() {
		return from;
	}

	public void setFrom(Location from) {
		this.from = from;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}
	
}
