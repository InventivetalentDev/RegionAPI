package org.inventivetalent.regionapi;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.apihelper.API;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class RegionAPI implements API {
	private static final Map<String, Flag> customFlagMap = new HashMap<>();

	@Deprecated
	private static final Set<FlagLoader> preLoadFlags = new HashSet<>();

	@Deprecated
	public static void registerCustomFlag(@Nonnull FlagLoader loader) {
		if (loader == null) { throw new IllegalArgumentException("loader cannot be null"); }
		preLoadFlags.add(loader);
	}

	@Override
	public void init(final Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void on(PluginEnableEvent event) {
				if ("WorldGuard".equals(event.getPlugin().getName())) {
					plugin.getLogger().info("Loading flags");

					// Deprecated flags
					Set<Flag> flags = new HashSet<>();
					for (FlagLoader loader : preLoadFlags) {
						flags.add(loader.load());
					}
					preLoadFlags.clear();

					FlagLoadEvent flagLoadEvent = new FlagLoadEvent(flags);
					Bukkit.getPluginManager().callEvent(flagLoadEvent);
					for (Flag flag : flagLoadEvent.getFlags()) {
						try {
							registerCustomFlag(plugin, flag);
						} catch (Throwable throwable) {
							plugin.getLogger().log(Level.SEVERE, "Unexpected exception while registering flag", throwable);
						}
					}
				}
			}
		}, plugin);
	}

	/**
	 * Register a custom region flag
	 *
	 * @param plugin plugin host
	 * @param flag   flag to register
	 */
	protected static void registerCustomFlag(Plugin plugin, @Nonnull Flag flag) {
		if (flag == null) { throw new IllegalArgumentException("flag cannot be null"); }
		if (customFlagMap.containsKey(flag.getName())) {
			throw new IllegalArgumentException("Flag with name '" + flag.getName() + "' is already registered");
		}
		customFlagMap.put(flag.getName(), flag);

		FlagInjector.injectFlag(flag);
		plugin.getLogger().log(Level.INFO, "Injected custom Flag '" + flag.getName() + "' for class " + flag.getClass().getName());
	}

	/**
	 * @return Set of registered flag names
	 */
	@Nonnull
	public static Set<String> getCustomFlags() {
		return new HashSet<>(customFlagMap.keySet());
	}

	/**
	 * Get a custom flag by its name
	 *
	 * @param name Name of the flag
	 * @return the flag, or null if it is not registered
	 */
	@Nullable
	public static Flag getCustomFlag(@Nonnull String name) {
		if (customFlagMap.containsKey(name)) {
			return customFlagMap.get(name);
		}
		return null;
	}

	/**
	 * Get the region manager for the specified world
	 *
	 * @param world {@link World} to get the manager for
	 * @return the {@link RegionManager}, or null
	 */
	@Nullable
	public static RegionManager getRegionManager(@Nonnull World world) {
		return getWorldGuard().getRegionManager(world);
	}

	/**
	 * Check if a player is inside of a region
	 *
	 * @param player {@link Player} to check
	 * @param region {@link ProtectedRegion} to check
	 * @return <code>true</code> if the player is in the region
	 */
	public static boolean isPlayerInRegion(@Nonnull Player player, @Nonnull ProtectedRegion region) {
		return isInRegion(player.getLocation(), region);
	}

	/**
	 * Check if a region contains a location
	 *
	 * @param location {@link Location} to check
	 * @param region   {@link ProtectedRegion} to check
	 * @return <code>true</code> if the location is inside of the region
	 */
	public static boolean isInRegion(@Nonnull Location location, @Nonnull ProtectedRegion region) {
		return region.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	/**
	 * Get all regions a player is currently in
	 *
	 * @param player {@link Player} to check
	 * @return A {@link Set} of regions
	 */
	@Nonnull
	public static Set<ProtectedRegion> getRegions(Player player) {
		return getRegions(player.getLocation());
	}

	/**
	 * Get all regions at a location
	 *
	 * @param location {@link Location} to check
	 * @return A {@link Set} of regions
	 */
	@Nonnull
	public static Set<ProtectedRegion> getRegions(Location location) {
		Set<ProtectedRegion> regions = new HashSet<>();
		RegionManager manager = getRegionManager(location.getWorld());
		if (manager == null) { return regions; }
		for (ProtectedRegion region : manager.getRegions().values()) {
			if (isInRegion(location, region)) {
				regions.add(region);
			}
		}
		return regions;
	}

	/**
	 * @return the {@link WorldGuardPlugin}
	 */
	public static WorldGuardPlugin getWorldGuard() {
		return ((WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard"));
	}

	@Override
	public void load() {
	}

	@Override
	public void disable(Plugin plugin) {
	}
}
