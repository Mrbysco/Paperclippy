package com.mrbysco.paperclippy.item;

import com.mrbysco.paperclippy.Reference;
import com.mrbysco.paperclippy.entity.PaperclipEntity;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.List;

public class PaperclipItem extends Item {
	public PaperclipItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, FluidMode.NONE);
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
		if (ret != null) return ret;

		if (raytraceresult == null) {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else if (raytraceresult.getType() != Type.BLOCK) {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else {
			BlockRayTraceResult traceResult = (BlockRayTraceResult)raytraceresult;
			BlockPos blockpos = traceResult.getPos();
			PaperclipEntity clippy = PaperRegistry.PAPERCLIPPY.get().create(worldIn);
			if(clippy != null) {
				clippy.setPositionAndUpdate(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
				if(!(playerIn instanceof FakePlayer)) {
					clippy.setOwnerId(playerIn.getUniqueID());
				}
				worldIn.addEntity(clippy);
			}

			if (!playerIn.isCreative()) {
				itemstack.shrink(1);
			}
			return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".paperclip.info").mergeStyle(TextFormatting.YELLOW));
	}
}
