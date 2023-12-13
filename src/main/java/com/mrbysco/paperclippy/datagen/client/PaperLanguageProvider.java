package com.mrbysco.paperclippy.datagen.client;

import com.mrbysco.paperclippy.PaperClippyMod;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class PaperLanguageProvider extends LanguageProvider {
	public PaperLanguageProvider(DataGenerator gen) {
		super(gen, PaperClippyMod.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addItem(PaperRegistry.PAPER_CLIP, "PaperClippy");
		addEntityType(PaperRegistry.PAPERCLIPPY, "PaperClippy");

		addSubtitle(PaperRegistry.BOING, "PaperClippy Jumps");

		add("paperclippy.paperclip.info", "Need help with that?");

		add("paperclippy.line.hurt", "It looks like I'm getting hurt");
		add("paperclippy.line.fighting", "It looks like you're fighting, need help? ");
		add("paperclippy.line.crafting", "It looks like you're crafting, need help? ");
		add("paperclippy.line.death", "It looks like I turned back into an item");

		add("paperclippy.line.accept", "Ok, here I go");
		add("paperclippy.line.decline", "Ok, I'll leave you be");
	}

	/**
	 * Add a subtitle to a sound event
	 *
	 * @param sound The sound event registry object
	 * @param text  The subtitle text
	 */
	private void addSubtitle(RegistryObject<SoundEvent> sound, String text) {
		this.addSubtitle(sound.get(), text);
	}

	/**
	 * Add a subtitle to a sound event
	 *
	 * @param sound The sound event
	 * @param text  The subtitle text
	 */
	private void addSubtitle(SoundEvent sound, String text) {
		String path = PaperClippyMod.MOD_ID + ".subtitle." + sound.getLocation().getPath();
		this.add(path, text);
	}
}
