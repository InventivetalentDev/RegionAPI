package org.inventivetalent.regionapi;

import com.sk89q.worldguard.protection.flags.Flag;

import javax.annotation.Nonnull;

@Deprecated
public interface FlagLoader {

	/**
	 * Called when WorldGuard is ready to get flags loaded
	 *
	 * @return a {@link Flag} instance
	 */
	@Nonnull
	Flag load();

}
