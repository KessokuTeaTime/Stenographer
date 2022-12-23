package net.krlite.stenographer.mixin;

import net.krlite.stenographer.Stenographer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Locale;

@Mixin(GameOptions.class)
public abstract class LanguageSetter {
	@Mutable @Shadow public String language;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;load()V", shift = At.Shift.AFTER))
	private void setLanguage(MinecraftClient client, File optionsFile, CallbackInfo ci) {
		Locale locale = Locale.getDefault();
		String language = (locale.getLanguage() + "_" + locale.getCountry()).toLowerCase();
		if (this.language.equals("en_us") && !this.language.equals(language)) { // Only set language if it's English or another language differing from current
			Stenographer.LOGGER.info("[" + Stenographer.MOD_NAME + "] Setting language to " + language + "...");
			this.language = language;
		}
	}
}
