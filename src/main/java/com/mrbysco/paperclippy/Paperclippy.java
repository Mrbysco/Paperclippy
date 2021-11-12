package com.mrbysco.paperclippy;

import com.mrbysco.paperclippy.client.ClientHandler;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class Paperclippy {
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    public Paperclippy() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        PaperRegistry.ITEMS.register(eventBus);
        PaperRegistry.ENTITIES.register(eventBus);
        PaperRegistry.SOUND_EVENTS.register(eventBus);

        eventBus.addListener(PaperRegistry::registerEntityAttributes);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
        });
    }
}
