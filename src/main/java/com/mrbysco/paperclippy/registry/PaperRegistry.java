package com.mrbysco.paperclippy.registry;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.entity.Paperclip;
import com.mrbysco.paperclippy.item.PaperclipItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PaperRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PaperClippyMod.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PaperClippyMod.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PaperClippyMod.MOD_ID);

	public static final RegistryObject<Item> PAPER_CLIP = ITEMS.register("paper_clip", () -> new PaperclipItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1)));

	public static final RegistryObject<EntityType<Paperclip>> PAPERCLIPPY = ENTITY_TYPES.register("paperclippy", () ->
			EntityType.Builder.<Paperclip>of(Paperclip::new, MobCategory.AMBIENT)
					.sized(0.7F, 1.95F)
					.clientTrackingRange(10)
					.build("paperclippy"));

	public static final RegistryObject<SoundEvent> PAPERCLIP_BOING = SOUND_EVENTS.register("paperclippy.jump", () ->
			new SoundEvent(new ResourceLocation(PaperClippyMod.MOD_ID, "paperclippy.jump")));
	public static final RegistryObject<SoundEvent> PAPERCLIP_ATTACK = SOUND_EVENTS.register("paperclippy.attack", () ->
			new SoundEvent(new ResourceLocation(PaperClippyMod.MOD_ID, "paperclippy.attack")));

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(PAPERCLIPPY.get(), Paperclip.registerAttributes().build());
	}
}
