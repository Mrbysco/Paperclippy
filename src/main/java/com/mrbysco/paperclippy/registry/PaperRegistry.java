package com.mrbysco.paperclippy.registry;

import com.mrbysco.paperclippy.Reference;
import com.mrbysco.paperclippy.entity.PaperclipEntity;
import com.mrbysco.paperclippy.item.PaperclipItem;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PaperRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);

	public static final RegistryObject<Item> PAPER_CLIP = ITEMS.register("paper_clip", () -> new PaperclipItem(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(1)));

	public static final RegistryObject<EntityType<PaperclipEntity>> PAPERCLIPPY = ENTITIES.register("paperclippy", () ->
			EntityType.Builder.<PaperclipEntity>of(PaperclipEntity::new, EntityClassification.AMBIENT)
					.sized(0.7F, 1.95F)
					.clientTrackingRange(10)
					.build("paperclippy"));

	public static final RegistryObject<SoundEvent> boing = SOUND_EVENTS.register("paperclippy.jump", () -> createSound("paperclippy.jump"));

	private static SoundEvent createSound(String name) {
		ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, name);
		return new SoundEvent(resourceLocation);
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(PAPERCLIPPY.get(), PaperclipEntity.registerAttributes().build());
	}
}
