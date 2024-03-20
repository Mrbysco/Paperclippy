package com.mrbysco.paperclippy.event;

import com.mrbysco.paperclippy.clickevent.CraftClickEvent;
import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CraftingHandler {

	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event) {
		final Player player = event.getEntity();
		final Level level = player.level();
		TargetingConditions clippyPredicate = (TargetingConditions.forCombat()).range(12.0D).selector((livingEntity) ->
				livingEntity instanceof Paperclip paperclip && paperclip.getOwner() != null && paperclip.getOwner().getUUID().equals(player.getUUID()));
		Paperclip nearestClippy = level.getNearestEntity(Paperclip.class, clippyPredicate, player, player.getX(), player.getY(), player.getZ(), player.getBoundingBox().inflate(12D));
		if (!level.isClientSide && nearestClippy != null) {
			MutableComponent baseComponent = nearestClippy.getBaseChatComponent();
			MutableComponent textComponent = Component.translatable("paperclippy.line.crafting").withStyle(ChatFormatting.WHITE);
			MutableComponent yesComponent = Component.literal("Yes");
			MutableComponent acceptComponent = Component.translatable("paperclippy.line.accept");
			yesComponent.setStyle(textComponent.getStyle()
					.withClickEvent(new CraftClickEvent("/tellraw @a [\"\",{\"text\":\"" + nearestClippy.getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
							acceptComponent.getString() + "\"}]", nearestClippy, event.getCrafting().copy())));
			yesComponent.withStyle(ChatFormatting.GREEN);
			MutableComponent betweenComponent = Component.literal(", ");
			MutableComponent noComponent = Component.literal("No");
			MutableComponent declineComponent = Component.translatable("paperclippy.line.decline");
			noComponent.setStyle(textComponent.getStyle()
					.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tellraw @a [\"\",{\"text\":\"" + nearestClippy.getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
							declineComponent.getString() + "\"}]")));
			noComponent.withStyle(ChatFormatting.RED);
			baseComponent.append(textComponent).append(yesComponent).append(betweenComponent).append(noComponent);

			player.sendSystemMessage(baseComponent);

			nearestClippy.tipCooldown = 20;
		}
	}
}
