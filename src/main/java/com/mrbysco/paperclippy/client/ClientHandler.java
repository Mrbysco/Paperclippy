package com.mrbysco.paperclippy.client;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.client.model.PaperclipModel;
import com.mrbysco.paperclippy.client.renderer.PaperclipRenderer;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ClientHandler {
	public static final ModelLayerLocation PAPERCLIPPY = new ModelLayerLocation(new ResourceLocation(PaperClippyMod.MOD_ID, "paperclippy"), "main");

	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(PaperRegistry.PAPERCLIPPY.get(), PaperclipRenderer::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(PAPERCLIPPY, PaperclipModel::createBodyLayer);
	}
}