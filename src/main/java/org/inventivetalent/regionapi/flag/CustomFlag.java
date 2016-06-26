package org.inventivetalent.regionapi.flag;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroup;

@Deprecated
public abstract class CustomFlag<T> extends Flag<T> {
	public CustomFlag(String name, RegionGroup defaultGroup) {
		super(name, defaultGroup);
	}

	public CustomFlag(String name) {
		super(name);
	}
}
