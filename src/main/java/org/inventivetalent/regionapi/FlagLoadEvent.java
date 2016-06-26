package org.inventivetalent.regionapi;

import com.sk89q.worldguard.protection.flags.Flag;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;

public class FlagLoadEvent extends Event {

	private final Collection<Flag> flags;

	public FlagLoadEvent(Collection<Flag> flags) {
		this.flags = flags;
	}

	public Collection<Flag> getFlags() {
		return flags;
	}

	public void addFlag(Flag flag) {
		this.flags.add(flag);
	}

	private static HandlerList handlerList = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}
}
