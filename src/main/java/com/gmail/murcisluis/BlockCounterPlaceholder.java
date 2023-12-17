package com.gmail.murcisluis;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class BlockCounterPlaceholder extends PlaceholderExpansion {

    private WorldGuardPlugin worldGuard;

    public BlockCounterPlaceholder() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // Check if WorldGuard plugin is found
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            throw new IllegalStateException("WorldGuard plugin not found or not loaded");
        }

        worldGuard = (WorldGuardPlugin) plugin;    }

    @Override
    public String getIdentifier() {
        return "blockcounter";
    }

    @Override
    public String getAuthor() {
        return "murcis_luis";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if(player == null) {
            return "";
        }

        // Ejemplo de identificador: "blockcounter_<worldName>_<regionName>_<material>"
        String[] args = identifier.split("_", 3);
        if(args.length < 3) return "Incorrect format";

        World world = Bukkit.getWorld(args[0]);
        String regionName = args[1];
        Material material = Material.matchMaterial(args[2]);

        if(world == null || material == null) return "Error in the parameters";

        return String.valueOf(countBlocks(world, regionName, material));
    }

    private int countBlocks(World world, String regionName, Material material) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(world));
        if (regions == null) return 0;

        ProtectedRegion region = regions.getRegion(regionName);
        if (region == null) return 0;

        int count = 0;

        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = Bukkit.getWorld(world.getName()).getBlockAt(x, y, z);
                    if (block.getType() == material) {
                        count++;
                    }
                }
            }
        }

        return count;
    }
}
