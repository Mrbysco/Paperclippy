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
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PaperDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(true, new PaperRecipeProvider(generator));
			generator.addProvider(true, new PaperLootProvider(generator));
			BlockTagsProvider provider = new BlockTagsProvider(generator, PaperClippyMod.MOD_ID, helper) {
				@Override
				protected void addTags() {
				}
			};
			generator.addProvider(true, provider);
			generator.addProvider(true, new PaperItemTagProvider(generator, provider, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(true, new PaperLanguageProvider(generator));
			generator.addProvider(true, new PaperSoundProvider(generator, helper));
			generator.addProvider(true, new PaperItemModelProvider(generator, helper));
		}
	}
}
