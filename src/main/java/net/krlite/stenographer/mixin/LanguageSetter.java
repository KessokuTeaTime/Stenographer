package net.krlite.stenographer.mixin;

import net.krlite.stenographer.Stenographer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.SortedSet;

@Mixin(GameOptions.class)
public abstract class LanguageSetter {
	@Mutable @Shadow public String language;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;load()V", shift = At.Shift.AFTER))
	private void setLanguage(MinecraftClient client, File optionsFile, CallbackInfo ci) {
		String language = (Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry()).toLowerCase();
		if (this.language.equals("en_us") && !this.language.equals(language)) { // Only set language if it's English or another language differing from current
			/*
			Fix language in case it's not a valid language code(due to some launchers like prism launcher),
			but only works with certain countries
			Language codes are from Locale.class
			 */
			Locale[] locales = Arrays.stream(Locale.class.getFields()).filter(f -> f.getType().equals(Locale.class)).map(f -> {
				try {
					return (Locale) f.get(Locale.getDefault());
				} catch (IllegalAccessException e) {
					Stenographer.LOGGER.error("Failed to get locale from field " + f.getName(), e);
					return null;
				}
			}).filter(Objects::nonNull).filter(l -> !l.getLanguage().isEmpty() && !l.getCountry().isEmpty()).toArray(Locale[]::new); // Get preset locales from java
			String[] languageCodes = Arrays.stream(locales).map(l -> (l.getLanguage() + "_" + l.getCountry()).toLowerCase()).distinct().toArray(String[]::new); // Get preset language codes from locales
			String fixedFrom = language;
			if (Arrays.stream(languageCodes).anyMatch(l -> l.split("_")[1].equals(Locale.getDefault().getCountry().toLowerCase()))) {
				language = Arrays.stream(languageCodes).filter(l -> l.split("_")[1].equals(Locale.getDefault().getCountry().toLowerCase())).findFirst().orElse("en_us");
			}
			/* The language should be fixed */

			Stenographer.LOGGER.info("[" + Stenographer.MOD_NAME + "] Switching language to " + language + (!fixedFrom.equals(language) ? ", fixed from " + fixedFrom + "." /* Log the unfixed language */ : "..."));
			this.language = language;
		}
	}
}
