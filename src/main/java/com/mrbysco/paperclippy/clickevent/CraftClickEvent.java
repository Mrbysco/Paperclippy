package com.mrbysco.paperclippy.clickevent;

import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.network.chat.ClickEvent;

public class CraftClickEvent extends ClickEvent {
	private final Paperclip paperclip;
	private final CraftingRecipe recipe;

	public CraftClickEvent(String commandValue, Paperclip paperclipIn, CraftingRecipe recipe) {
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
