package org.inventivetalent.regionapi;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import org.inventivetalent.reflection.resolver.FieldResolver;

import java.lang.reflect.Field;

class FlagInjector {

	private static Field flagsList;

	protected static void injectFlag(Flag flag) {
		if (flag == null) { throw new IllegalArgumentException(); }

		Flag[] flagArray = new Flag[DefaultFlag.flagsList.length + 1];
		System.arraycopy(DefaultFlag.flagsList, 0, flagArray, 0, DefaultFlag.flagsList.length);
		flagArray[DefaultFlag.flagsList.length] = flag;

		try {
			flagsList.set(null, flagArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static {
		try {
			flagsList = new FieldResolver(DefaultFlag.class).resolve("flagsList");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
