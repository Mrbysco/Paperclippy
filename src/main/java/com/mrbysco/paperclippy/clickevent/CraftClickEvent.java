package com.mrbysco.paperclippy.clickevent;

import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.world.item.ItemStack;

public class CraftClickEvent extends ClickEvent {

	public CraftClickEvent(String commandValue, Paperclip paperclip, ItemStack result) {
		super(Action.RUN_COMMAND, commandValue);
	}
}
