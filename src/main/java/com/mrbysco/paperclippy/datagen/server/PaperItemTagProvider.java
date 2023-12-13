package com.mrbysco.paperclippy.datagen.server;

import com.mrbysco.paperclippy.PaperClippyMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class PaperItemTagProvider extends ItemTagsProvider {
	public PaperItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider,
								TagsProvider<Block> blockTagProvider, ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, blockTagProvider.contentsGetter(), PaperClippyMod.MOD_ID, existingFileHelper);
	}


	@Override
	public void addTags(HolderLookup.Provider lookupProvider) {
		this.tag(PaperClippyMod.BUCKETS).add(Items.WATER_BUCKET, Items.LAVA_BUCKET, Items.MILK_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET, Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.AXOLOTL_BUCKET);
	}
}
