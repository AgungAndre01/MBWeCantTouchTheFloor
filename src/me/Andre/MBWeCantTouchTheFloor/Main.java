package me.Andre.MBWeCantTouchTheFloor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Main extends JavaPlugin implements Listener {

    boolean enabled;
    List<Location> safeLocs;

    @Override
    public void onEnable() {
        enabled = false;
        safeLocs = new ArrayList<>();
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {


    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("startMBWeCantTouchTheFloor")){
            for(Player p: getServer().getOnlinePlayers()){
                safeLocs.add(p.getLocation().getBlock().getLocation().clone().subtract(0, 1, 0));
                if(p.getBedSpawnLocation() != null){
                    safeLocs.add(p.getBedSpawnLocation().clone().subtract(0, 1, 0).getBlock().getLocation());
                }
            }
            getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD +  "STARTED!");
            enabled = true;

        }
        else if(command.getName().equalsIgnoreCase("stopMBWeCantTouchTheFloor")){
            enabled = false;
            getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD +  "STOPPED!");
        }
        return false;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(enabled){
            Player player = event.getPlayer();
            Location locBelow = player.getLocation().clone().subtract(0, 1, 0);

            if(locBelow.getBlock().getType().isSolid()){
                if(!safeLocs.contains(locBelow.getBlock().getLocation())){
                    player.setHealth(0.0D);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(enabled){
            Block block = event.getBlock();
            if(!safeLocs.contains(block.getLocation())){
                safeLocs.add(block.getLocation());
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        safeLocs.add(player.getLocation().getBlock().getLocation().clone().subtract(0, 1, 0));
    }
}
