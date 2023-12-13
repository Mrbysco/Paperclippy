package com.mrbysco.paperclippy.clickevent;

import com.mrbysco.paperclippy.entity.Paperclip;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.ClickEvent;

public class FightClickEvent extends ClickEvent {
	private final Paperclip paperclip;

	public FightClickEvent(String commandValue, Paperclip paperclipIn) {
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
