package com.mrbysco.paperclippy.datagen.client;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class PaperSoundProvider extends SoundDefinitionsProvider {

	public PaperSoundProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, PaperClippyMod.MOD_ID, helper);
	}

	@Override
	public void registerSounds() {
		this.add(PaperRegistry.PAPERCLIP_BOING, definition()
				.subtitle(modSubtitle(PaperRegistry.PAPERCLIP_BOING.getId()))
				.with(sound(modLoc("boing"))));

		this.add(PaperRegistry.PAPERCLIP_ATTACK, definition()
				.subtitle(modSubtitle(PaperRegistry.PAPERCLIP_ATTACK.getId()))
				.with(
						sound(new ResourceLocation("mob/slime/attack1")),
						sound(new ResourceLocation("mob/slime/attack2"))
				));
	}

	private String modSubtitle(ResourceLocation id) {
		return PaperClippyMod.MOD_ID + ".subtitle." + id.getPath();
	}

	private ResourceLocation modLoc(String name) {
		return new ResourceLocation(PaperClippyMod.MOD_ID, name);
	}
}
