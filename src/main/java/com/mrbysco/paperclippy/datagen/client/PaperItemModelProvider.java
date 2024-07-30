package com.mrbysco.paperclippy.datagen.client;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PaperItemModelProvider extends ItemModelProvider {
	public PaperItemModelProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, PaperClippyMod.MOD_ID, helper);
	}


	@Override
	protected void registerModels() {
		this.generatedItem(PaperRegistry.PAPER_CLIP.getId());
	}

	private void generatedItem(ResourceLocation location) {
		singleTexture(location.getPath(), ResourceLocation.withDefaultNamespace("item/generated"),
				"layer0", PaperClippyMod.modLoc("item/" + location.getPath()));
	}
}
