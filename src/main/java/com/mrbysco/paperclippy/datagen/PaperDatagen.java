package com.mrbysco.paperclippy.datagen;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.datagen.client.PaperItemModelProvider;
import com.mrbysco.paperclippy.datagen.client.PaperLanguageProvider;
import com.mrbysco.paperclippy.datagen.client.PaperSoundProvider;
import com.mrbysco.paperclippy.datagen.server.PaperItemTagProvider;
import com.mrbysco.paperclippy.datagen.server.PaperLootProvider;
import com.mrbysco.paperclippy.datagen.server.PaperRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PaperDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		if (event.includeServer()) {
			generator.addProvider(true, new PaperRecipeProvider(packOutput));
			generator.addProvider(true, new PaperLootProvider(packOutput));
			BlockTagsProvider provider = new BlockTagsProvider(packOutput, lookupProvider, PaperClippyMod.MOD_ID, helper) {
				@Override
				protected void addTags(HolderLookup.Provider provider) {

				}
			};
			generator.addProvider(true, provider);
			generator.addProvider(true, new PaperItemTagProvider(packOutput, lookupProvider, provider, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new PaperLanguageProvider(packOutput));
			generator.addProvider(true, new PaperSoundProvider(packOutput, helper));
			generator.addProvider(true, new PaperItemModelProvider(packOutput, helper));
		}
	}
}
