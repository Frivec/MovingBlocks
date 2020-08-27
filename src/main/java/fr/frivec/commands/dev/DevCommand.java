package fr.frivec.commands.dev;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.frivec.MovingBlocks;
import fr.frivec.movement.Mouvement;
import fr.frivec.movement.MoveSave;

public class DevCommand implements CommandExecutor {
	
	private Location from, to;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(sender instanceof Player) {
			
			final Player player = (Player) sender;
			
			if(args.length > 0) {
				
				if(args[0].equalsIgnoreCase("move")) {
					
					final Block block = player.getTargetBlockExact(3);
					final FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(block.getLocation(), block.getBlockData());
					
					fallingBlock.setFallDistance(-1000000);
					
					block.breakNaturally();
					
					final Shulker shulker = (Shulker) player.getWorld().spawnEntity(block.getLocation(), EntityType.SHULKER);
					
					shulker.setAI(false);
					shulker.setInvulnerable(true);
					shulker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 0, true, false, false));
					shulker.setAI(false);
					
					final ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(block.getLocation().subtract(0, 1, 0), EntityType.ARMOR_STAND);
					
					armorStand.addPassenger(fallingBlock);
					armorStand.addPassenger(shulker);
					armorStand.setGravity(false);
					armorStand.setBasePlate(false);
					armorStand.setInvulnerable(true);
					armorStand.setVisible(false);
					armorStand.setAI(false);
					
					new BukkitRunnable() {
						
						private int timer = 500;
						private Location lastLocation = armorStand.getLocation();
						
						@Override
						public void run() {
							
							this.lastLocation.add(new Vector(0, 0.05, 0));
							
							for(Entity passengers : armorStand.getPassengers())
								
								armorStand.removePassenger(passengers);
							
							armorStand.teleport(this.lastLocation);
							
							armorStand.addPassenger(fallingBlock);
							armorStand.addPassenger(shulker);
							
							if(timer == 0) {
								
								shulker.remove();
								armorStand.remove();
								fallingBlock.remove();
								
								player.sendMessage("§AEnd of movement");
								
								cancel();
								
							}
								
							timer--;
							
						}
						
					}.runTaskTimer(MovingBlocks.getInstance(), 0l, 1l);
					
					player.sendMessage("§aMoved block");
					
					return true;
					
				}else if(args[0].equalsIgnoreCase("translation")) {
					
					final Block block = player.getTargetBlockExact(3);
					
					new Mouvement(block, this.from, this.to).run();
					
				}else if(args[0].equals("p1")) {
					
					this.from = player.getLocation();
					player.sendMessage("§aSet first location");
					
					final DustOptions option = new DustOptions(Color.fromRGB(255, 0, 0), 1);
					
					Bukkit.getScheduler().scheduleSyncRepeatingTask(MovingBlocks.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							
							from.getWorld().spawnParticle(Particle.REDSTONE, from, 10, option);
							
						}
						
					}, 0l, 2l);
				
				}else if(args[0].equalsIgnoreCase("p2")) {
					
					this.to = player.getLocation();
					player.sendMessage("§aSet second location");
					
					final DustOptions option = new DustOptions(Color.fromRGB(0, 0, 255), 1);
					
					Bukkit.getScheduler().scheduleSyncRepeatingTask(MovingBlocks.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							
							to.getWorld().spawnParticle(Particle.REDSTONE, to, 10, option);
							
						}
						
					}, 0l, 2l);
					
				}else if(args[0].equalsIgnoreCase("sinus")) {
					
					final Block block = player.getTargetBlockExact(3);
					
					new DevMouvement(new Block[] {block}, player.getLocation(), null).start();
					
				}else if(args[0].equalsIgnoreCase("selection")) {
					
//					final LocalSession session = MovingBlocks.getInstance().getWorldEditPlugin().getSession(player);
//					final World world = session.getSelectionWorld();
//					
//					try {
//						
//						final Region region = session.getSelection(world);
//						final ArrayList<Block> blocks = new ArrayList<>();
//						
//						if(region != null && region instanceof CuboidRegion) {
//							
//							final BlockVector3 pos1 = ((CuboidRegion) region).getPos1(),
//												pos2 = ((CuboidRegion) region).getPos2();
//							
//							int maxX, maxY, maxZ, minX, minY, minZ;
//							
//							if(pos1.getX() > pos2.getX()) {
//								
//								maxX = pos1.getBlockX();
//								minX = pos2.getBlockX();
//								
//							}else {
//								
//								maxX = pos2.getBlockX();
//								minX = pos1.getBlockX();
//								
//							}
//							
//							if(pos1.getY() > pos2.getY()) {
//								
//								maxY = pos1.getBlockY();
//								minY = pos2.getBlockY();
//								
//							}else {
//								
//								maxY = pos2.getBlockY();
//								minY = pos1.getBlockY();
//								
//							}
//							
//							if(pos1.getZ() > pos2.getZ()) {
//								
//								maxZ = pos1.getBlockZ();
//								minZ = pos2.getBlockZ();
//								
//							}else {
//								
//								maxZ = pos2.getBlockZ();
//								minZ = pos1.getBlockZ();
//								
//							}
//							
//							for(int x = minX; x < maxX; x++)
//								
//								for(int y = minY; y < maxY; y++)
//									
//									for(int z = minZ; z < maxZ; z++)
//										
//										blocks.add(player.getWorld().getBlockAt(x, y, z));
//							
////							new Mouvement(blocks, from, to).start();
//							
//						}						
//						
//					} catch (IncompleteRegionException e) {
//						e.printStackTrace();
//					}
					
				}else if(args[0].equalsIgnoreCase("testsave")) {
					
					final MoveSave moveSave = new MoveSave("DEV", player.getTargetBlockExact(3));
					
					moveSave.addPoint(player.getLocation());
					moveSave.addPoint(player.getLocation().clone().add(5, 5, 5));
					moveSave.addPoint(player.getLocation().clone().add(5, 5, 5).clone().add(5, 5, 5));
					
					try {
						moveSave.save();
					} catch (IOException e) {
						player.sendMessage("§cErreur lors de la sauvegarde du parcours.");
						e.printStackTrace();
					}
					
					moveSave.start();
					
					return true;
					
				}
					
			}
			
		}else
			
			sender.sendMessage("§cErreur. Il est nécessaire d'utiliser un compte joueur pour utiliser cette commande.");
		
		return false;
	}

}
