package com.mrbysco.paperclippy.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.paperclippy.Reference;
import com.mrbysco.paperclippy.client.model.PaperclipModel;
import com.mrbysco.paperclippy.entity.PaperclipEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class PaperclipRenderer extends MobRenderer<PaperclipEntity, PaperclipModel<PaperclipEntity>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/paperclippy.png");

	public PaperclipRenderer(EntityRendererManager EntityRendererManagerIn) {
		super(EntityRendererManagerIn, new PaperclipModel(), 0.5F);
	}

	@Override
	protected void scale(PaperclipEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		float f = 0.999F;
		matrixStackIn.scale(f, f, f);
		matrixStackIn.translate(0.0D, (double)0.001F, 0.0D);
		float f1 = (float)1;
		float f2 = MathHelper.lerp(partialTickTime, entitylivingbaseIn.prevJumpFactor, entitylivingbaseIn.jumpFactor) / (f1 * 0.5F + 1.0F);
		float f3 = 1.0F / (f2 + 1.0F);
		matrixStackIn.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
	}

	public ResourceLocation getTextureLocation(PaperclipEntity entity) {
		return TEXTURE;
	}
}