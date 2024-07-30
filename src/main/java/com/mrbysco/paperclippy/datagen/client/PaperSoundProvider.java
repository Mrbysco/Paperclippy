package com.mrbysco.paperclippy.datagen.client;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class PaperSoundProvider extends SoundDefinitionsProvider {

	public PaperSoundProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, PaperClippyMod.MOD_ID, helper);
	}

	@Override
	public void registerSounds() {
		this.add(PaperRegistry.PAPERCLIP_BOING, definition()
				.subtitle(modSubtitle(PaperRegistry.PAPERCLIP_BOING.getId()))
				.with(sound(PaperClippyMod.modLoc("boing"))));

		this.add(PaperRegistry.PAPERCLIP_ATTACK, definition()
				.subtitle(modSubtitle(PaperRegistry.PAPERCLIP_ATTACK.getId()))
				.with(
						sound(ResourceLocation.withDefaultNamespace("mob/slime/attack1")),
						sound(ResourceLocation.withDefaultNamespace("mob/slime/attack2"))
				));
	}

	private String modSubtitle(ResourceLocation id) {
		return PaperClippyMod.MOD_ID + ".subtitle." + id.getPath();
	}
}
