package com.mrbysco.paperclippy.client.model;

import com.google.common.collect.ImmutableList;
import com.mrbysco.paperclippy.entity.PaperclipEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PaperclipModel<T extends PaperclipEntity> extends SegmentedModel<T> {
	private final ModelRenderer root;

	public PaperclipModel() {
		texWidth = 64;
		texHeight = 64;

		root = new ModelRenderer(this);
		root.setPos(0.0F, 0.0F, 0.0F);


		ModelRenderer leftBrow = new ModelRenderer(this);
		leftBrow.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(leftBrow);
		leftBrow.texOffs(32, 26).addBox(-9.0F, -20.0F, -2.0F, 6.0F, 2.0F, 2.0F, 0.0F, true);
		leftBrow.texOffs(40, 22).addBox(-11.0F, -18.0F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);

		ModelRenderer leftEye = new ModelRenderer(this);
		leftEye.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(leftEye);
		leftEye.texOffs(32, 32).addBox(-9.0F, -15.0F, -2.0F, 6.0F, 6.0F, 2.0F, 0.0F, true);

		ModelRenderer clip = new ModelRenderer(this);
		clip.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(clip);
		clip.texOffs(0, 0).addBox(-5.0F, 12.0F, 0.0F, 8.0F, 2.0F, 2.0F, 0.0F, true);
		clip.texOffs(28, 0).addBox(-9.0F, 4.0F, 0.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);
		clip.texOffs(36, 0).addBox(-11.0F, 0.0F, 0.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
		clip.texOffs(0, 18).addBox(-7.0F, -20.0F, 0.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
		clip.texOffs(52, 0).addBox(5.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, 0.0F, true);
		clip.texOffs(16, 8).addBox(-3.0F, -24.0F, 0.0F, 6.0F, 2.0F, 2.0F, 0.0F, true);
		clip.texOffs(34, 12).addBox(3.0F, -4.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		clip.texOffs(20, 0).addBox(-7.0F, 10.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		clip.texOffs(8, 4).addBox(5.0F, -20.0F, 0.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		clip.texOffs(44, 4).addBox(-5.0F, 0.0F, 0.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);
		clip.texOffs(32, 8).addBox(-5.0F, -22.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		clip.texOffs(44, 0).addBox(3.0F, 10.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		clip.texOffs(15, 12).addBox(-3.0F, 6.0F, 0.0F, 4.0F, 2.0F, 2.0F, 0.0F, true);
		clip.texOffs(26, 12).addBox(1.0F, -2.0F, 0.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		clip.texOffs(0, 4).addBox(7.0F, -12.0F, 0.0F, 2.0F, 12.0F, 2.0F, 0.0F, true);
		clip.texOffs(16, 4).addBox(3.0F, -22.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);

		ModelRenderer rightEye = new ModelRenderer(this);
		rightEye.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(rightEye);
		rightEye.texOffs(16, 32).addBox(4.0F, -15.0F, -2.0F, 6.0F, 6.0F, 2.0F, 0.0F, true);

		ModelRenderer rightBrow = new ModelRenderer(this);
		rightBrow.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(rightBrow);
		rightBrow.texOffs(16, 26).addBox(4.0F, -20.0F, -2.0F, 6.0F, 2.0F, 2.0F, 0.0F, true);
		rightBrow.texOffs(16, 22).addBox(10.0F, -18.0F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.root);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}