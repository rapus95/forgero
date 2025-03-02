package com.sigmundgranaas.forgero.fabric.resources.dynamic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sigmundgranaas.forgero.core.ForgeroStateRegistry;
import com.sigmundgranaas.forgero.core.resource.data.v2.data.IngredientData;
import com.sigmundgranaas.forgero.core.resource.data.v2.data.RecipeData;
import com.sigmundgranaas.forgero.core.state.composite.ConstructedState;
import com.sigmundgranaas.forgero.minecraft.common.recipe.customrecipe.RecipeTypes;
import com.sigmundgranaas.forgero.minecraft.common.recipe.implementation.RecipeUtils;
import com.sigmundgranaas.forgero.minecraft.common.recipe.implementation.generator.CompositeRecipeOptimiser;
import net.devtech.arrp.api.RuntimeResourcePack;

import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class PartToSchematicGenerator implements DynamicResourceGenerator {

	@Override
	public void generate(RuntimeResourcePack pack) {
		var recipes = parts().stream().map(this::createRecipe).flatMap(Optional::stream).toList();
		var optimiser = new CompositeRecipeOptimiser();
		optimiser.process(recipes).stream()
				.map(this::convertRecipeData)
				.forEach(recipe -> pack.addData(generateId(recipe), recipe.toString().getBytes()));
	}

	private List<ConstructedState> parts() {
		return ForgeroStateRegistry.STATES.all().stream()
				.map(Supplier::get)
				.filter(ConstructedState.class::isInstance)
				.map(ConstructedState.class::cast)
				.filter(comp -> comp.parts().stream().anyMatch(ingredient -> ingredient.name().contains("schematic")))
				.toList();
	}

	private Optional<RecipeData> createRecipe(ConstructedState construct) {
		var schematic = construct.parts().stream().filter(ingredient -> ingredient.name().contains("schematic")).findFirst();
		if (schematic.isPresent()) {
			var paper = IngredientData.builder().id("minecraft:paper").build();
			var constructIngredient = IngredientData.builder().id(construct.identifier()).build();
			var recipe = RecipeData.builder().ingredients(List.of(paper, constructIngredient)).target(schematic.get().identifier()).craftingType(RecipeTypes.TOOLPART_SCHEMATIC_RECIPE.toString()).build();
			return Optional.of(recipe);
		}
		return Optional.empty();
	}

	private JsonObject convertRecipeData(RecipeData construct) {
		var json = new JsonObject();
		json.addProperty("type", "minecraft:crafting_shapeless");
		var ingredients = new JsonArray();
		construct.ingredients().stream().map(RecipeUtils::ingredientsToJsonEntry).forEach(ingredients::add);
		json.add("ingredients", ingredients);
		var result = new JsonObject();
		result.addProperty("item", construct.target());
		json.add("result", result);
		return json;
	}

	private Identifier generateId(JsonObject recipe) {
		String output = recipe.getAsJsonObject("result").get("item").getAsString().split(":")[1];
		return new Identifier("forgero:recipes/" + output + "_recipe" + ".json");
	}
}
