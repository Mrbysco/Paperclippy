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
		this.add(PaperRegistry.BOING, definition()
				.subtitle(modSubtitle(PaperRegistry.BOING.getId()))
				.with(sound(modLoc("boing"))));
	}

	private String modSubtitle(ResourceLocation id) {
		return PaperClippyMod.MOD_ID + ".subtitle." + id.getPath();
	}

	private ResourceLocation modLoc(String name) {
		return new ResourceLocation(PaperClippyMod.MOD_ID, name);
	}
}
