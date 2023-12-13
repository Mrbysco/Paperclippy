package com.mrbysco.paperclippy;

import com.mrbysco.paperclippy.client.ClientHandler;
import com.mrbysco.paperclippy.event.CraftingHandler;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PaperClippyMod.MOD_ID)
public class PaperClippyMod {
    public static final String MOD_ID = "paperclippy";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public PaperClippyMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PaperRegistry.ITEMS.register(eventBus);
        PaperRegistry.ENTITIES.register(eventBus);
        PaperRegistry.SOUND_EVENTS.register(eventBus);

        eventBus.addListener(PaperRegistry::registerEntityAttributes);

        MinecraftForge.EVENT_BUS.register(new CraftingHandler());

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::registerEntityRenders);
            eventBus.addListener(ClientHandler::registerLayerDefinitions);
        });
    }
}