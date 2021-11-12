package com.mrbysco.paperclippy.client;

import com.mrbysco.paperclippy.client.renderer.PaperclipRenderer;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
	public static void onClientSetup(final FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(PaperRegistry.PAPERCLIPPY.get(), PaperclipRenderer::new);
	}
}
