package net.krlite.stenographer;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stenographer implements ModInitializer {
	public static final String MOD_ID = "stenographer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean done = false;

	public static void stenoDone() {
		done = true;
	}

	@Override
	public void onInitialize() {
	}
}
