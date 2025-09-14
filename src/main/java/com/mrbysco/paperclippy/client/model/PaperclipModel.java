package com.mrbysco.paperclippy.client.model;

import com.mrbysco.paperclippy.client.state.PaperClipRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class PaperclipModel extends EntityModel<PaperClipRenderState> {

	public PaperclipModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition paperclippy = partdefinition.addOrReplaceChild("paperclippy", CubeListBuilder.create(), PartPose.ZERO);
		paperclippy.addOrReplaceChild("left_brow", CubeListBuilder.create()
						.texOffs(32, 26).addBox(-9.0F, -20.0F, -2.0F, 6.0F, 2.0F, 2.0F)
						.texOffs(40, 22).addBox(-11.0F, -18.0F, -2.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offset(1.0F, 10.0F, -1.0F));

		paperclippy.addOrReplaceChild("left_eye", CubeListBuilder.create()
						.texOffs(32, 32).addBox(-9.0F, -15.0F, -2.0F, 6.0F, 6.0F, 2.0F),
				PartPose.offset(1.0F, 10.0F, -1.0F));

		paperclippy.addOrReplaceChild("right_brow", CubeListBuilder.create()
						.texOffs(16, 26).addBox(4.0F, -20.0F, -2.0F, 6.0F, 2.0F, 2.0F)
						.texOffs(16, 22).addBox(10.0F, -18.0F, -2.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offset(1.0F, 10.0F, -1.0F));

		paperclippy.addOrReplaceChild("right_eye", CubeListBuilder.create()
						.texOffs(16, 32).addBox(4.0F, -15.0F, -2.0F, 6.0F, 6.0F, 2.0F),
				PartPose.offset(1.0F, 10.0F, -1.0F));

		paperclippy.addOrReplaceChild("clip", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0F, 12.0F, 0.0F, 8.0F, 2.0F, 2.0F)
						.texOffs(28, 0).addBox(-9.0F, 4.0F, 0.0F, 2.0F, 6.0F, 2.0F)
						.texOffs(36, 0).addBox(-11.0F, 0.0F, 0.0F, 2.0F, 4.0F, 2.0F)
						.texOffs(0, 18).addBox(-7.0F, -20.0F, 0.0F, 2.0F, 20.0F, 2.0F)
						.texOffs(52, 0).addBox(5.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F)
						.texOffs(16, 8).addBox(-3.0F, -24.0F, 0.0F, 6.0F, 2.0F, 2.0F)
						.texOffs(34, 12).addBox(3.0F, -4.0F, 0.0F, 2.0F, 2.0F, 2.0F)
						.texOffs(20, 0).addBox(-7.0F, 10.0F, 0.0F, 2.0F, 2.0F, 2.0F)
						.texOffs(8, 4).addBox(5.0F, -20.0F, 0.0F, 2.0F, 8.0F, 2.0F)
						.texOffs(44, 4).addBox(-5.0F, 0.0F, 0.0F, 2.0F, 6.0F, 2.0F)
						.texOffs(32, 8).addBox(-5.0F, -22.0F, 0.0F, 2.0F, 2.0F, 2.0F)
						.texOffs(44, 0).addBox(3.0F, 10.0F, 0.0F, 2.0F, 2.0F, 2.0F)
						.texOffs(15, 12).addBox(-3.0F, 6.0F, 0.0F, 4.0F, 2.0F, 2.0F)
						.texOffs(26, 12).addBox(1.0F, -2.0F, 0.0F, 2.0F, 8.0F, 2.0F)
						.texOffs(0, 4).addBox(7.0F, -12.0F, 0.0F, 2.0F, 12.0F, 2.0F)
						.texOffs(16, 4).addBox(3.0F, -22.0F, 0.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offset(1.0F, 10.0F, -1.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(PaperClipRenderState renderState) {
		super.setupAnim(renderState);
	}
}