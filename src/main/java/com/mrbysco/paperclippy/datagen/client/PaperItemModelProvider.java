package com.mrbysco.paperclippy.datagen.client;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PaperItemModelProvider extends ItemModelProvider {
	public PaperItemModelProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, PaperClippyMod.MOD_ID, helper);
	}


	@Override
	protected void registerModels() {
		this.generatedItem(PaperRegistry.PAPER_CLIP.getId());
	}

	private void generatedItem(ResourceLocation location) {
		singleTexture(location.getPath(), new ResourceLocation("item/generated"),
				"layer0", new ResourceLocation(PaperClippyMod.MOD_ID, "item/" + location.getPath()));
	}
}
