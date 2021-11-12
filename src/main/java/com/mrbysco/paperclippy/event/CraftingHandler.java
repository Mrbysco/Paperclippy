package com.mrbysco.paperclippy.event;

import com.mrbysco.paperclippy.clickevent.FightClickEvent;
import com.mrbysco.paperclippy.entity.PaperclipEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CraftingHandler {

	@SubscribeEvent
	public void worldTick(ItemCraftedEvent event) {
		final PlayerEntity player = event.getPlayer();
		final World world = player.getCommandSenderWorld();
		EntityPredicate clippyPredicate = (new EntityPredicate()).range(12.0D).allowInvulnerable().selector((clippy) -> ((PaperclipEntity)clippy).getOwner().getUUID().equals(player.getUUID()));
		PaperclipEntity nearestClippy = world.getNearestEntity(PaperclipEntity.class, clippyPredicate, player, player.getX(), player.getY(), player.getZ(), player.getBoundingBox().inflate(12D));
		if(!world.isClientSide && nearestClippy != null) {
			System.out.println("Found a clippy at " + nearestClippy.blockPosition());

			IFormattableTextComponent baseComponent = nearestClippy.getBaseChatComponent();
			IFormattableTextComponent textComponent = new TranslationTextComponent("paperclippy.line.crafting").withStyle(TextFormatting.WHITE);
			IFormattableTextComponent yesComponent = new StringTextComponent("Yes");
			yesComponent.setStyle(textComponent.getStyle()
					.withClickEvent(new FightClickEvent("/tellraw @a [\"\",{\"text\":\"" + nearestClippy.getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
							I18n.get("paperclippy.line.accept") + "\"}]", nearestClippy)));
			yesComponent.withStyle(TextFormatting.GREEN);
			IFormattableTextComponent betweenComponent = new StringTextComponent(", ");
			IFormattableTextComponent noComponent = new StringTextComponent("No");
			noComponent.setStyle(textComponent.getStyle()
					.withClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tellraw @a [\"\",{\"text\":\"" + nearestClippy.getChatName() + "\",\"color\":\"yellow\"},{\"text\":\" " +
							I18n.get("paperclippy.line.decline") + "\"}]")));
			noComponent.withStyle(TextFormatting.RED);
			baseComponent.append(textComponent).append(yesComponent).append(betweenComponent).append(noComponent);

			player.sendMessage(baseComponent, Util.NIL_UUID);

			nearestClippy.tipCooldown = 20;
		}
	}
}
