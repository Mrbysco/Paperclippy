package com.mrbysco.paperclippy.datagen.server;

import com.google.gson.JsonObject;
import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PaperRecipeProvider extends RecipeProvider {
	public PaperRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PaperRegistry.PAPER_CLIP.get())
				.pattern("PN")
				.pattern("N ")
				.define('P', Items.PAPER)
				.define('N', Tags.Items.NUGGETS_IRON)
				.unlockedBy("has_paper", has(Items.PAPER))
				.unlockedBy("has_iron_nugget", has(Tags.Items.NUGGETS_IRON))
				.save(recipeOutput);
	}

	@Override
	protected @Nullable CompletableFuture<?> saveAdvancement(CachedOutput cachedOutput, FinishedRecipe recipe, JsonObject jsonObject) {
		return null;
	}
}
