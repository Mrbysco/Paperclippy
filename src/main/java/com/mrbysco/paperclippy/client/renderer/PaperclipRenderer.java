package com.mrbysco.paperclippy.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.client.ClientHandler;
import com.mrbysco.paperclippy.client.model.PaperclipModel;
import com.mrbysco.paperclippy.client.state.PaperClipRenderState;
import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PaperclipRenderer extends MobRenderer<Paperclip, PaperClipRenderState, PaperclipModel> {
	private static final ResourceLocation TEXTURE = PaperClippyMod.modLoc("textures/entity/paperclippy.png");

	public PaperclipRenderer(EntityRendererProvider.Context context) {
		super(context, new PaperclipModel(context.bakeLayer(ClientHandler.PAPERCLIPPY)), 0.5F);
	}

	@Override
	public PaperClipRenderState createRenderState() {
		return new PaperClipRenderState();
	}

	@Override
	public void extractRenderState(Paperclip paperclip, PaperClipRenderState renderState, float partialTick) {
		super.extractRenderState(paperclip, renderState, partialTick);
		renderState.squish = Mth.lerp(partialTick, paperclip.prevJumpFactor, paperclip.jumpFactor);
	}

	@Override
	protected void scale(PaperClipRenderState renderState, PoseStack poseStack) {
		float f = 0.999F;
		poseStack.scale(f, f, f);
		poseStack.translate(0.0D, (double) 0.001F, 0.0D);
		float f1 = (float) 1;
		float f2 = renderState.squish / (f1 * 0.5F + 1.0F);
		float f3 = 1.0F / (f2 + 1.0F);
		poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
	}

	@Override
	public ResourceLocation getTextureLocation(PaperClipRenderState renderState) {
		return TEXTURE;
	}
}