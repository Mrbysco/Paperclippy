package com.mrbysco.paperclippy.datagen;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.datagen.client.PaperItemModelProvider;
import com.mrbysco.paperclippy.datagen.client.PaperLanguageProvider;
import com.mrbysco.paperclippy.datagen.client.PaperSoundProvider;
import com.mrbysco.paperclippy.datagen.server.PaperItemTagProvider;
import com.mrbysco.paperclippy.datagen.server.PaperLootProvider;
import com.mrbysco.paperclippy.datagen.server.PaperRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PaperDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new PaperRecipeProvider(generator));
			generator.addProvider(new PaperLootProvider(generator));
			BlockTagsProvider provider = new BlockTagsProvider(generator, PaperClippyMod.MOD_ID, helper) {
				@Override
				protected void addTags() {
				}
			};
			generator.addProvider(provider);
			generator.addProvider(new PaperItemTagProvider(generator, provider, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(new PaperLanguageProvider(generator));
			generator.addProvider(new PaperSoundProvider(generator, helper));
			generator.addProvider(new PaperItemModelProvider(generator, helper));
		}
	}
}
