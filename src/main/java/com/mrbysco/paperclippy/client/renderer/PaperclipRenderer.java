package com.mrbysco.paperclippy.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.client.ClientHandler;
import com.mrbysco.paperclippy.client.model.PaperclipModel;
import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PaperclipRenderer extends MobRenderer<Paperclip, PaperclipModel> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(PaperClippyMod.MOD_ID, "textures/entity/paperclippy.png");

	public PaperclipRenderer(EntityRendererProvider.Context context) {
		super(context, new PaperclipModel(context.bakeLayer(ClientHandler.PAPERCLIPPY)), 0.5F);
	}

	@Override
	protected void scale(Paperclip paperclip, PoseStack poseStack, float partialTickTime) {
		float f = 0.999F;
		poseStack.scale(f, f, f);
		poseStack.translate(0.0D, (double) 0.001F, 0.0D);
		float f1 = (float) 1;
		float f2 = Mth.lerp(partialTickTime, paperclip.prevJumpFactor, paperclip.jumpFactor) / (f1 * 0.5F + 1.0F);
		float f3 = 1.0F / (f2 + 1.0F);
		poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
	}

	public ResourceLocation getTextureLocation(Paperclip entity) {
		return TEXTURE;
	}
}