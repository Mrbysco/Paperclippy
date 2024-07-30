package com.mrbysco.paperclippy.datagen;

import com.mrbysco.paperclippy.datagen.client.PaperItemModelProvider;
import com.mrbysco.paperclippy.datagen.client.PaperLanguageProvider;
import com.mrbysco.paperclippy.datagen.client.PaperSoundProvider;
import com.mrbysco.paperclippy.datagen.server.PaperLootProvider;
import com.mrbysco.paperclippy.datagen.server.PaperRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class PaperDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		if (event.includeServer()) {
			generator.addProvider(true, new PaperRecipeProvider(packOutput, lookupProvider));
			generator.addProvider(true, new PaperLootProvider(packOutput, lookupProvider));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new PaperLanguageProvider(packOutput));
			generator.addProvider(true, new PaperSoundProvider(packOutput, helper));
			generator.addProvider(true, new PaperItemModelProvider(packOutput, helper));
		}
	}
}
