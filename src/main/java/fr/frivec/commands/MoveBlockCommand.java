package fr.frivec.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.frivec.movement.MoveSave;

public class MoveBlockCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(sender instanceof Player) {
			
			final Player player = (Player) sender;
			
			if(args.length > 0) {
				
				if(args.length == 1) {
					
					player.sendMessage(argsError());
					player.sendMessage("§cAide: /moveblock <create/remove/addpoint/removepoint/setpoint/run> <NomDuTrajet>");
					
					return false;
					
				}
				
				final String subCommand = args[0];
				final String moveName = args[1].toLowerCase();
				
				if(subCommand.equalsIgnoreCase("create")) {
					
					if(MoveSave.getSaves().containsKey(moveName)) {
						
						player.sendMessage("§cErreur. Ce trajet existe déjà.");
						
						return false;
						
					}
					
					final MoveSave moveSave = new MoveSave(moveName, player.getTargetBlockExact(5));
					
					try {
						
						moveSave.save();
						
						player.sendMessage("§aLe trajet §6" + moveName + " §aa bien été enregistré !");
						
						return true;
						
					} catch (IOException e) {
					
						e.printStackTrace();
						player.sendMessage("§4Une erreur est survenue lors de la savegarde du trajet: §6" + moveName + "§4. Veuillez regarder la console pour plus d'informations.");
						
						return false;
						
					}
					
				}else if(subCommand.equalsIgnoreCase("remove")) {
					
					final MoveSave moveSave = getMoveSave(player, moveName);
					
					if(moveSave == null)
						
						return false;
					
					if(runCheck(moveSave, player))
						
						return false;
					
					try {
						
						moveSave.delete();
						
						player.sendMessage("§aLe trajet §6" + moveName + " §aa bien été supprimé !");
						
						return true;
						
					}catch (FileNotFoundException e) {
						
						e.printStackTrace();
						player.sendMessage("§cLe fichier recherché: " + moveName + " n'a pas été trouvé.");
						
						return false;
						
					} catch (IOException e) {
					
						e.printStackTrace();
						player.sendMessage("§4Une erreur est survenue lors de la savegarde du trajet: §6" + moveName + "§4. Veuillez regarder la console pour plus d'informations.");
						
						return false;
						
					}
					
				}else if(subCommand.equalsIgnoreCase("addpoint")) {
					
					final MoveSave moveSave = getMoveSave(player, moveName);
					
					if(moveSave == null)
						
						return false;
					
					if(runCheck(moveSave, player))
						
						return false;
					
					moveSave.addPoint(player.getLocation());
					
					try {
						
						moveSave.save();
						
						player.sendMessage("§aVotre position actuelle a bien été ajoutée aux points de passage du trajet: §6" + moveName + "§a.");
						
						return true;
						
					} catch (IOException e) {
					
						e.printStackTrace();
						player.sendMessage("§4Une erreur est survenue lors de la savegarde du trajet: §6" + moveName + "§4. Veuillez regarder la console pour plus d'informations.");
						
						return false;
						
					}
					
				}else if(subCommand.equalsIgnoreCase("removepoint")) {
					
					if(args.length < 3) {
						
						player.sendMessage(argsError());
						player.sendMessage("§cAide: /moveblock removepoint <NomDuTrajet> <index>");
						
						return false;
						
					}
					
					final MoveSave moveSave = getMoveSave(player, moveName);
					
					if(moveSave == null)
						
						return false;
					
					if(runCheck(moveSave, player))
						
						return false;
					
					try {
						
						final int index = Integer.parseInt(args[2]);
						
						moveSave.removePoint(index, true);
						
						moveSave.save();
						
						player.sendMessage("§aVotre position actuelle a bien été ajoutée aux points de passage du trajet: §6" + moveName + "§a.");
						
						player.sendMessage("§aLe point n°" + index + " a bien été supprimé.");
						
						return true;
						
					} catch (NumberFormatException e) {
						
						player.sendMessage("§cErreur. L'index renseigné n'est pas un nombre reconnu.");
						
						return false;
						
					} catch (IOException e) {
						
						e.printStackTrace();
						player.sendMessage("§4Une erreur est survenue lors de la savegarde du trajet: §6" + moveName + "§4. Veuillez regarder la console pour plus d'informations.");
						
						return false;
						
					}
					
				}else if(subCommand.equalsIgnoreCase("setpoint")) {
					
					if(args.length < 3) {
						
						player.sendMessage(argsError());
						player.sendMessage("§cAide: /moveblock setpoint <NomDuTrajet> <index>");
						
						return false;
						
					}
					
					final MoveSave moveSave = getMoveSave(player, moveName);
					
					if(moveSave == null)
						
						return false;
					
					if(runCheck(moveSave, player))
						
						return false;
					
					try {
						
						final int index = Integer.parseInt(args[2]);
						
						moveSave.setPoint(index, player.getLocation());
						moveSave.save();
						
						player.sendMessage("§aVotre position actuelle a bien été ajoutée aux points de passage du trajet: §6" + moveName + "§a. Emplacement choisi: n°" + index + ".");
						
						return true;
						
					} catch (NumberFormatException e) {
						
						player.sendMessage("§cErreur. L'index renseigné n'est pas un nombre reconnu.");
						
						return false;
						
					} catch (IOException e) {
						
						e.printStackTrace();
						player.sendMessage("§4Une erreur est survenue lors de la savegarde du trajet: §6" + moveName + "§4. Veuillez regarder la console pour plus d'informations.");
						
						return false;
						
					}
					
				}else if(subCommand.equalsIgnoreCase("run")) {
					
					final MoveSave moveSave = getMoveSave(player, moveName);
					
					if(moveName == null)
						
						return false;
					
					if(runCheck(moveSave, player))
						
						return false;
					
					moveSave.start();
					
					player.sendMessage("§aLe trajet §6" + moveName + " §adémarre.");
					
					return true;
					
				}else if(subCommand.equalsIgnoreCase("autostart")) {
					
					if(args.length < 3) {
						
						player.sendMessage(argsError());
						player.sendMessage("§cAide: /moveblock autostart <NomDuTrajet> <true/false>");
						
						return false;
						
					}
					
					final MoveSave moveSave = getMoveSave(player, moveName);
					
					if(moveSave == null)
						
						return false;
					
					if(runCheck(moveSave, player))
						
						return false;
					
					final boolean autoStart = Boolean.parseBoolean(args[2]);
					
					moveSave.setAutoStart(autoStart);
					
					try {
					
						moveSave.save();
						
						player.sendMessage("§aL'option autostart du trajet §6" + moveName + " §aa bien été mise sur §b" + autoStart + "§a.");
						
						return true;
					
					} catch (IOException e) {
						
						e.printStackTrace();
						player.sendMessage("§4Une erreur est survenue lors de la savegarde du trajet: §6" + moveName + "§4. Veuillez regarder la console pour plus d'informations.");
						
						return false;
					
					}
					
				}else if(subCommand.equalsIgnoreCase("reverse")) {
					
					if(args.length < 3) {
						
						player.sendMessage(argsError());
						player.sendMessage("§cAide: /moveblock reverse <NomDuTrajet> <true/false>");
						
						return false;
						
					}
					
					final MoveSave moveSave = getMoveSave(player, moveName);
					
					if(moveSave == null)
						
						return false;
					
					if(runCheck(moveSave, player))
						
						return false;
					
					final boolean reverse = Boolean.parseBoolean(args[2]);
					
					moveSave.setReverse(reverse);
					
					try {
					
						moveSave.save();
						
						player.sendMessage("§aL'option reverse du trajet §6" + moveName + " §aa bien été mise sur §b" + reverse + "§a.");
						
						return true;
					
					} catch (IOException e) {
						
						e.printStackTrace();
						player.sendMessage("§4Une erreur est survenue lors de la savegarde du trajet: §6" + moveName + "§4. Veuillez regarder la console pour plus d'informations.");
						
						return false;
					
					}
					
				}
				
			}else {
				
				player.sendMessage(argsError());
				
				return false;
				
			}
			
		}else
			
			sender.sendMessage("§cErreur. Vous devez êtes connecté sur le serveur pour utiliser cette commande.");
		
		return false;
	}
	
	private boolean runCheck(final MoveSave moveSave, final Player player) {
		
		if(moveSave.isRunning()) {
			
			player.sendMessage("§cErreur. Ce trajet est déjà en train d'être parcouru. Veuillez attendre qu'il se termine.");
			
			return true;
			
		}
		
		return false;
		
	}
	
	private MoveSave getMoveSave(final Player player, final String moveName) {
		
		if(!MoveSave.getSaves().containsKey(moveName)) {
			
			player.sendMessage("§cErreur. Ce trajet n'existe pas.");
			
			return null;
			
		}
		
		final MoveSave moveSave = MoveSave.getSaves().get(moveName);
		
		return moveSave;
		
	}
	
	private String argsError() {
		
		return "§cErreur. Il manque des arguments.";
		
	}

}
