package com.mrbysco.paperclippy.datagen.server;

import com.mrbysco.paperclippy.PaperClippyMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PaperItemTagProvider extends ItemTagsProvider {
	public PaperItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(dataGenerator, blockTagsProvider, PaperClippyMod.MOD_ID, existingFileHelper);
	}


	@Override
	protected void addTags() {
		this.tag(PaperClippyMod.BUCKETS).add(Items.WATER_BUCKET, Items.LAVA_BUCKET, Items.MILK_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET, Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.AXOLOTL_BUCKET);
	}
}
