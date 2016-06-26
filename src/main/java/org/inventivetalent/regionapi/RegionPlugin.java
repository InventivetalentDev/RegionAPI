package org.inventivetalent.regionapi;

import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.apihelper.APIManager;

public class RegionPlugin extends JavaPlugin {

	@Override
	public void onLoad() {
		APIManager.registerAPI(new RegionAPI());
	}

	@Override
	public void onEnable() {
		APIManager.initAPI(RegionAPI.class);
	}
}
