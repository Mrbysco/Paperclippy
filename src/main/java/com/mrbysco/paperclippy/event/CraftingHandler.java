package com.mrbysco.paperclippy.event;

import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class CraftingHandler {

	@SubscribeEvent
	public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		final Player player = event.getEntity();
		final Level level = player.level();
		TargetingConditions clippyPredicate = (TargetingConditions.forCombat()).range(12.0D).selector((livingEntity, serverLevel) ->
				livingEntity instanceof Paperclip paperclip && paperclip.getOwner() != null && paperclip.getOwner().getUUID().equals(player.getUUID()));
		if (!level.isClientSide()) {
			Paperclip nearestClippy = ((ServerLevel) level).getNearestEntity(Paperclip.class, clippyPredicate, player,
					player.getX(), player.getY(), player.getZ(), player.getBoundingBox().inflate(12D));
			if (nearestClippy != null) {
				String clippyUUID = nearestClippy.getUUID().toString();
				String itemName = event.getCrafting().getItem().builtInRegistryHolder().getKey().identifier().toString();

				MutableComponent baseComponent = nearestClippy.getBaseChatComponent();
				MutableComponent textComponent = Component.translatable("paperclippy.line.crafting").withStyle(ChatFormatting.WHITE);
				MutableComponent yesComponent = Component.literal("Yes");
				yesComponent.setStyle(textComponent.getStyle()
						.withClickEvent(new ClickEvent.RunCommand(
								"/paperclippy set_crafting " + clippyUUID + " " + itemName
						))
				);
				yesComponent.withStyle(ChatFormatting.GREEN);
				MutableComponent betweenComponent = Component.literal(", ");
				MutableComponent noComponent = Component.literal("No");
				noComponent.setStyle(textComponent.getStyle()
						.withClickEvent(new ClickEvent.RunCommand(
								"/paperclippy clear_crafting " + clippyUUID
						))
				);
				noComponent.withStyle(ChatFormatting.RED);
				baseComponent.append(textComponent).append(yesComponent).append(betweenComponent).append(noComponent);

				player.displayClientMessage(baseComponent, false);

				nearestClippy.tipCooldown = 20;
			}
		}
	}
}
