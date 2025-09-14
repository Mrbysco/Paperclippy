package com.mrbysco.paperclippy.item;

import com.mrbysco.paperclippy.entity.Paperclip;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.function.Consumer;

public class PaperclipItem extends Item {
	public PaperclipItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		HitResult traceResult = getPlayerPOVHitResult(level, playerIn, Fluid.NONE);
		if (traceResult.getType() == HitResult.Type.MISS) {
			return InteractionResult.PASS;
		} else if (traceResult.getType() != HitResult.Type.BLOCK) {
			return InteractionResult.PASS;
		} else {
			BlockHitResult blockTraceResult = (BlockHitResult) traceResult;
			BlockPos blockpos = blockTraceResult.getBlockPos();
			Paperclip paperClippy = PaperRegistry.PAPERCLIPPY.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);
			if (paperClippy != null) {
				paperClippy.teleportTo(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
				if (!(playerIn instanceof FakePlayer)) {
					paperClippy.setOwner(playerIn);
				}
				level.addFreshEntity(paperClippy);
			}

			if (!playerIn.isCreative()) {
				itemstack.shrink(1);
			}
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
		tooltipAdder.accept(Component.translatable("paperclippy.paperclip.info").withStyle(ChatFormatting.YELLOW));
	}
}
