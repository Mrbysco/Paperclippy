package com.mrbysco.paperclippy.datagen.server;

import com.mrbysco.paperclippy.registry.PaperRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class PaperRecipeProvider extends RecipeProvider {
	public PaperRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		super(provider, recipeOutput);
	}

	@Override
	protected void buildRecipes() {
		shaped(RecipeCategory.MISC, PaperRegistry.PAPER_CLIP.get())
				.pattern("PN")
				.pattern("N ")
				.define('P', Items.PAPER)
				.define('N', Tags.Items.NUGGETS_IRON)
				.unlockedBy("has_paper", has(Items.PAPER))
				.unlockedBy("has_iron_nugget", has(Tags.Items.NUGGETS_IRON))
				.save(output);
	}


	public static class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new PaperRecipeProvider(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return "Paperclippy Recipes";
		}
	}
}
