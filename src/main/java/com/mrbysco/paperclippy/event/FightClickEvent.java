package com.mrbysco.paperclippy.event;

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
		LivingEntity attackTarget = owner.getLastAttackedEntity();
		if(attackTarget != null && attackTarget != paperclip && attackTarget.isAlive()) {
			paperclip.setAttackTarget(attackTarget);
		}
		return super.getValue();
	}
}
