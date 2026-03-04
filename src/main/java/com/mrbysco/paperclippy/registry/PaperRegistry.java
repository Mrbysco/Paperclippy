package com.mrbysco.paperclippy.registry;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.entity.Paperclip;
import com.mrbysco.paperclippy.item.PaperclipItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PaperRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PaperClippyMod.MOD_ID);
	public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(PaperClippyMod.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PaperClippyMod.MOD_ID);

	public static final DeferredItem<PaperclipItem> PAPER_CLIP = ITEMS.registerItem("paper_clip", PaperclipItem::new, () -> new Item.Properties().stacksTo(1));

	public static final Supplier<EntityType<Paperclip>> PAPERCLIPPY = ENTITY_TYPES.registerEntityType("paperclippy",
			Paperclip::new,
			MobCategory.AMBIENT,
			builder -> builder
					.sized(0.7F, 1.95F)
					.clientTrackingRange(10)
	);

	public static final DeferredHolder<SoundEvent, SoundEvent> PAPERCLIP_BOING = SOUND_EVENTS.register("paperclippy.jump", () ->
			SoundEvent.createVariableRangeEvent(PaperClippyMod.modLoc("paperclippy.jump")));
	public static final DeferredHolder<SoundEvent, SoundEvent> PAPERCLIP_ATTACK = SOUND_EVENTS.register("paperclippy.attack", () ->
			SoundEvent.createVariableRangeEvent(PaperClippyMod.modLoc("paperclippy.attack")));

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(PAPERCLIPPY.get(), Paperclip.registerAttributes().build());
	}

}
