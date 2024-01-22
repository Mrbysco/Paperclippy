package com.mrbysco.paperclippy;

import com.mrbysco.paperclippy.client.ClientHandler;
import com.mrbysco.paperclippy.event.CraftingHandler;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PaperClippyMod.MOD_ID)
public class PaperClippyMod {
	public static final String MOD_ID = "paperclippy";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final TagKey<Item> BUCKETS = ItemTags.create(new ResourceLocation("paperclippy", "buckets"));

	public PaperClippyMod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		PaperRegistry.ITEMS.register(eventBus);
		PaperRegistry.ENTITY_TYPES.register(eventBus);
		PaperRegistry.SOUND_EVENTS.register(eventBus);

		eventBus.addListener(PaperRegistry::registerEntityAttributes);
		eventBus.addListener(this::addTabContents);

		NeoForge.EVENT_BUS.register(new CraftingHandler());

		if (FMLEnvironment.dist.isClient()) {
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
		}
	}

	private void addTabContents(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(PaperRegistry.PAPER_CLIP);
		}
	}
}
