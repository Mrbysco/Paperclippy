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

	public static final RegistryObject<Item> PAPER_CLIP = ITEMS.register("paper_clip", () -> new PaperclipItem(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1)));

	public static final RegistryObject<EntityType<PaperclipEntity>> PAPERCLIPPY = ENTITIES.register("paperclippy", () ->
			EntityType.Builder.<PaperclipEntity>create(PaperclipEntity::new, EntityClassification.AMBIENT)
					.size(0.7F, 1.95F)
					.trackingRange(10)
					.build("paperclippy"));

	public static final RegistryObject<SoundEvent> boing = SOUND_EVENTS.register("paperclippy.jump", () -> createSound("paperclippy.jump"));

	private static SoundEvent createSound(String name) {
		ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, name);
		SoundEvent sound = new SoundEvent(resourceLocation);
		return sound;
	}

	public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(PAPERCLIPPY.get(), PaperclipEntity.registerAttributes().create());
	}
}
