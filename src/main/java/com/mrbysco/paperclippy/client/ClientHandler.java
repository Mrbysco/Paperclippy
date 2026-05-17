package com.mrbysco.paperclippy.client;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.client.model.PaperclipModel;
import com.mrbysco.paperclippy.client.renderer.PaperclipRenderer;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {
	public static final ModelLayerLocation PAPERCLIPPY = new ModelLayerLocation(PaperClippyMod.modLoc("paperclippy"), "main");

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(PaperRegistry.PAPERCLIPPY.get(), PaperclipRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(PAPERCLIPPY, PaperclipModel::createBodyLayer);
	}
}