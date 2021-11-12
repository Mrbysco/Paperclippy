package com.mrbysco.paperclippy.clickevent;

import com.mrbysco.paperclippy.entity.PaperclipEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.util.text.event.ClickEvent;

public class CraftClickEvent extends ClickEvent {
	private final PaperclipEntity paperclip;
	private final ICraftingRecipe recipe;

	public CraftClickEvent(String commandValue, PaperclipEntity paperclipIn, ICraftingRecipe recipe) {
		super(Action.RUN_COMMAND, commandValue);
		this.paperclip = paperclipIn;
		this.recipe = recipe;
	}

	@Override
	public String getValue() {
		LivingEntity owner = paperclip.getOwner();
		if(owner != null) {
//			TODO: implement crafting helper
		}
		return super.getValue();
	}
}
