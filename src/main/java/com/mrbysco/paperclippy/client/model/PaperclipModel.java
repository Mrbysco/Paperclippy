package com.mrbysco.paperclippy.client.model;

import com.google.common.collect.ImmutableList;
import com.mrbysco.paperclippy.entity.PaperclipEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PaperclipModel<T extends PaperclipEntity> extends SegmentedModel<T> {
	private final ModelRenderer root;
	private final ModelRenderer LeftBrow;
	private final ModelRenderer LeftEye;
	private final ModelRenderer Clip;
	private final ModelRenderer RightEye;
	private final ModelRenderer RightBrow;

	public PaperclipModel() {
		texWidth = 64;
		texHeight = 64;

		root = new ModelRenderer(this);
		root.setPos(0.0F, 0.0F, 0.0F);


		LeftBrow = new ModelRenderer(this);
		LeftBrow.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(LeftBrow);
		LeftBrow.texOffs(32, 26).addBox(-9.0F, -20.0F, -2.0F, 6.0F, 2.0F, 2.0F, 0.0F, true);
		LeftBrow.texOffs(40, 22).addBox(-11.0F, -18.0F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);

		LeftEye = new ModelRenderer(this);
		LeftEye.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(LeftEye);
		LeftEye.texOffs(32, 32).addBox(-9.0F, -15.0F, -2.0F, 6.0F, 6.0F, 2.0F, 0.0F, true);

		Clip = new ModelRenderer(this);
		Clip.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(Clip);
		Clip.texOffs(0, 0).addBox(-5.0F, 12.0F, 0.0F, 8.0F, 2.0F, 2.0F, 0.0F, true);
		Clip.texOffs(28, 0).addBox(-9.0F, 4.0F, 0.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);
		Clip.texOffs(36, 0).addBox(-11.0F, 0.0F, 0.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
		Clip.texOffs(0, 18).addBox(-7.0F, -20.0F, 0.0F, 2.0F, 20.0F, 2.0F, 0.0F, true);
		Clip.texOffs(52, 0).addBox(5.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, 0.0F, true);
		Clip.texOffs(16, 8).addBox(-3.0F, -24.0F, 0.0F, 6.0F, 2.0F, 2.0F, 0.0F, true);
		Clip.texOffs(34, 12).addBox(3.0F, -4.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		Clip.texOffs(20, 0).addBox(-7.0F, 10.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		Clip.texOffs(8, 4).addBox(5.0F, -20.0F, 0.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		Clip.texOffs(44, 4).addBox(-5.0F, 0.0F, 0.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);
		Clip.texOffs(32, 8).addBox(-5.0F, -22.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		Clip.texOffs(44, 0).addBox(3.0F, 10.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
		Clip.texOffs(15, 12).addBox(-3.0F, 6.0F, 0.0F, 4.0F, 2.0F, 2.0F, 0.0F, true);
		Clip.texOffs(26, 12).addBox(1.0F, -2.0F, 0.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);
		Clip.texOffs(0, 4).addBox(7.0F, -12.0F, 0.0F, 2.0F, 12.0F, 2.0F, 0.0F, true);
		Clip.texOffs(16, 4).addBox(3.0F, -22.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);

		RightEye = new ModelRenderer(this);
		RightEye.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(RightEye);
		RightEye.texOffs(16, 32).addBox(4.0F, -15.0F, -2.0F, 6.0F, 6.0F, 2.0F, 0.0F, true);

		RightBrow = new ModelRenderer(this);
		RightBrow.setPos(1.0F, 10.0F, -1.0F);
		root.addChild(RightBrow);
		RightBrow.texOffs(16, 26).addBox(4.0F, -20.0F, -2.0F, 6.0F, 2.0F, 2.0F, 0.0F, true);
		RightBrow.texOffs(16, 22).addBox(10.0F, -18.0F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
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