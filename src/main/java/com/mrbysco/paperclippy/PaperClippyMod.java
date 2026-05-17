package com.mrbysco.paperclippy;

import com.mrbysco.paperclippy.commands.PaperclipCommands;
import com.mrbysco.paperclippy.event.CraftingHandler;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PaperClippyMod.MOD_ID)
public class PaperClippyMod {
	public static final String MOD_ID = "paperclippy";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public PaperClippyMod(IEventBus eventBus) {
		PaperRegistry.ITEMS.register(eventBus);
		PaperRegistry.ENTITY_TYPES.register(eventBus);
		PaperRegistry.SOUND_EVENTS.register(eventBus);

		eventBus.addListener(PaperRegistry::registerEntityAttributes);
		eventBus.addListener(this::addTabContents);

		NeoForge.EVENT_BUS.register(new CraftingHandler());
		NeoForge.EVENT_BUS.addListener(this::onCommandRegister);
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		PaperclipCommands.initializeCommands(event.getDispatcher(), event.getBuildContext());
	}

	private void addTabContents(final BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(PaperRegistry.PAPER_CLIP);
		}
	}

	public static Identifier modLoc(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
