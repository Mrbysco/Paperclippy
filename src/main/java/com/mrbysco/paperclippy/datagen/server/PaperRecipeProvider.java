package com.mrbysco.paperclippy.datagen.server;

import com.google.gson.JsonObject;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.nio.file.Path;
import java.util.function.Consumer;

public class PaperRecipeProvider extends RecipeProvider {
	public PaperRecipeProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(PaperRegistry.PAPER_CLIP.get())
				.pattern("PN")
				.pattern("N ")
				.define('P', Items.PAPER)
				.define('N', Tags.Items.NUGGETS_IRON)
				.unlockedBy("has_paper", has(Items.PAPER))
				.unlockedBy("has_iron_nugget", has(Tags.Items.NUGGETS_IRON))
				.save(consumer);
	}

	@Override
	protected void saveAdvancement(HashCache cache, JsonObject p_126015_, Path path) {

	}
}
