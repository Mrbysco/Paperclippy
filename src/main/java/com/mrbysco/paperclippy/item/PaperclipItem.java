package com.mrbysco.paperclippy.item;

import com.mrbysco.paperclippy.entity.Paperclip;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.List;

public class PaperclipItem extends Item {
	public PaperclipItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		HitResult traceResult = getPlayerPOVHitResult(level, playerIn, Fluid.NONE);
		if (traceResult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemstack);
		} else if (traceResult.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(itemstack);
		} else {
			BlockHitResult blockTraceResult = (BlockHitResult) traceResult;
			BlockPos blockpos = blockTraceResult.getBlockPos();
			Paperclip paperClippy = PaperRegistry.PAPERCLIPPY.get().create(level);
			if (paperClippy != null) {
				paperClippy.teleportTo(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
				if (!(playerIn instanceof FakePlayer)) {
					paperClippy.setOwnerId(playerIn.getUUID());
				}
				level.addFreshEntity(paperClippy);
			}

			if (!playerIn.isCreative()) {
				itemstack.shrink(1);
			}
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
		tooltipComponents.add(Component.translatable("paperclippy.paperclip.info").withStyle(ChatFormatting.YELLOW));
	}
}
