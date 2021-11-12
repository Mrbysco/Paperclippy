package com.mrbysco.paperclippy.clickevent;

import com.mrbysco.paperclippy.entity.PaperclipEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.event.ClickEvent;

public class FightClickEvent extends ClickEvent {
	private final PaperclipEntity paperclip;

	public FightClickEvent(String commandValue, PaperclipEntity paperclipIn) {
		super(Action.RUN_COMMAND, commandValue);
		this.paperclip = paperclipIn;
	}

	@Override
	public String getValue() {
		LivingEntity owner = paperclip.getOwner();
		if(owner != null) {
			LivingEntity attackTarget = owner.getLastHurtMob();
			if(attackTarget != null && attackTarget != paperclip && attackTarget.isAlive()) {
				paperclip.setTarget(attackTarget);
			}
		}
		return super.getValue();
	}
}
