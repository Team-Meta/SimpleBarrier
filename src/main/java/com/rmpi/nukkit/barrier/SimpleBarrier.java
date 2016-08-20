package com.rmpi.nukkit.barrier;

import java.util.HashMap;

import com.rmpi.nukkit.barrier.barrier.BarrierManager;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

class PositionPair {
	public Position first = null;
	public Position second = null;
}

public class SimpleBarrier extends PluginBase implements Listener {
	private static SimpleBarrier instance;
	public BarrierManager barrierMgr;
	public HashMap<Player, PositionPair> selection = new HashMap<>();
	public HashMap<Player, Boolean> selectable = new HashMap<>();
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		instance = this;
		barrierMgr = BarrierManager.getInstance();
		barrierMgr.init();
		getLogger().info("SimpleBarrier enabled");
	}
	
	@Override
	public void onDisable() {
		barrierMgr.save();
		getLogger().info("SimpleBarrier disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (! sender.hasPermission(command.getPermission())) {
			noPermission(sender);
			return true;
		}
		
		if (args.length == 0) {
			sender.sendMessage(command.getUsage());
			return true;
		}
	
		switch (args[0]) {
			case "pos1":
				if (!(sender instanceof Player)) {
					noPermission(sender);
					return true;
				}

				if (! selectable.containsKey(sender)) selectable.put((Player) sender, false);
					else selectable.replace((Player) sender, false);
				sender.sendMessage("Select position 1");
				break;
			case "pos2":
				if (!(sender instanceof Player)) {
					noPermission(sender);
					return true;
				}
				
				if (! selectable.containsKey(sender)) selectable.put((Player) sender, true);
					else selectable.replace((Player) sender, true);
				sender.sendMessage("Select position 2");
				break;
			case "add":
				if (!(sender instanceof Player)) {
					noPermission(sender);
					return true;
				}
				
				if (args.length != 2) {
					sender.sendMessage(command.getUsage());
					return true;
				}
				
				if (!selection.containsKey(sender) ||
						selection.get(sender).first == null || selection.get(sender).second == null) {
					sender.sendMessage("Invalid area selection");
					return true;
				}
				
				PositionPair selected = selection.remove(sender);
				barrierMgr.addBarrier(selected.first, selected.second, args[1]);
				
				sender.sendMessage("Added barrier successfully: " + args[1]);
				break;
			case "del":
				if (args.length != 2) {
					sender.sendMessage(command.getUsage());
					return true;
				}
				
				sender.sendMessage(barrierMgr.deleteBarrier(args[1]) ? "Deleted barrier successfully: " + args[1] : "No barrier found with name: " + args[1]);
				break;
			case "delall":
				barrierMgr.clear();
				sender.sendMessage("Cleared barriers");
				break;
			case "list":
				// TBD
			default:
				sender.sendMessage(command.getUsage());
		}
		return true;
	};
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent e) {
		if (!e.getPlayer().hasPermission("barrier.thrw") && !barrierMgr.isInside(e.getFrom())&& barrierMgr.isInside(e.getTo())) e.setCancelled();
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player plr = e.getPlayer();
		
		if (selectable.containsKey(plr)) {
			if (!selection.containsKey(plr)) selection.put(plr, new PositionPair());
			boolean isFirst = !selectable.remove(plr);
			
			if (isFirst) {
				selection.get(plr).first = e.getBlock();
				plr.sendMessage("Position 1 selected");
			} else {
				selection.get(plr).second = e.getBlock();
				plr.sendMessage("Position 2 selected");
			}
		}
	}
	
	public static SimpleBarrier getInstance() {
		return instance;
	}
	
	private void noPermission(CommandSender sender) {
		sender.sendMessage(getServer().getLanguage().translateString(TextFormat.RED + "%commands.generic.permission"));
	}
}
