package com.mrbysco.paperclippy.event;

import com.mrbysco.paperclippy.clickevent.CraftClickEvent;
import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CraftingHandler {

	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event) {
		final Player player = event.getPlayer();
		final Level level = player.getLevel();
		TargetingConditions clippyPredicate = (TargetingConditions.forCombat()).range(12.0D).selector((livingEntity) ->
				livingEntity instanceof Paperclip paperclip && paperclip.getOwner() != null && paperclip.getOwner().getUUID().equals(player.getUUID()));
		Paperclip nearestClippy = level.getNearestEntity(Paperclip.class, clippyPredicate, player, player.getX(), player.getY(), player.getZ(), player.getBoundingBox().inflate(12D));
		if (!level.isClientSide && nearestClippy != null) {
			System.out.println("Found a clippy at " + nearestClippy.blockPosition());

			MutableComponent baseComponent = nearestClippy.getBaseChatComponent();
			MutableComponent textComponent = new TranslatableComponent("paperclippy.line.crafting").withStyle(ChatFormatting.WHITE);
			MutableComponent yesComponent = new TextComponent("Yes");
			yesComponent.setStyle(textComponent.getStyle()
					.withClickEvent(new CraftClickEvent("/tellraw @a [\"\",{\"text\":\"" + nearestClippy.getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
							I18n.get("paperclippy.line.accept") + "\"}]", nearestClippy, event.getCrafting().copy())));
			yesComponent.withStyle(ChatFormatting.GREEN);
			MutableComponent betweenComponent = new TextComponent(", ");
			MutableComponent noComponent = new TextComponent("No");
			noComponent.setStyle(textComponent.getStyle()
					.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tellraw @a [\"\",{\"text\":\"" + nearestClippy.getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
							I18n.get("paperclippy.line.decline") + "\"}]")));
			noComponent.withStyle(ChatFormatting.RED);
			baseComponent.append(textComponent).append(yesComponent).append(betweenComponent).append(noComponent);

			player.sendMessage(baseComponent, Util.NIL_UUID);

			nearestClippy.tipCooldown = 20;
		}
	}
}
