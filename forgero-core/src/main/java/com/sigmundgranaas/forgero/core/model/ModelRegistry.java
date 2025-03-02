package com.sigmundgranaas.forgero.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.sigmundgranaas.forgero.core.resource.ResourceListener;
import com.sigmundgranaas.forgero.core.resource.data.v2.data.DataResource;
import com.sigmundgranaas.forgero.core.resource.data.v2.data.ModelData;
import com.sigmundgranaas.forgero.core.resource.data.v2.data.PaletteData;
import com.sigmundgranaas.forgero.core.state.Identifiable;
import com.sigmundgranaas.forgero.core.state.State;
import com.sigmundgranaas.forgero.core.type.TypeTree;
import com.sigmundgranaas.forgero.core.util.match.Context;

public class ModelRegistry {
	private final HashMap<String, ModelMatcher> modelMap;
	private final Map<String, PaletteTemplateModel> textures;

	private final Map<String, String> paletteRemapper;

	private final HashMap<String, ArrayList<ModelData>> delayedModels;

	private final HashMap<String, ModelData> generationModels;
	private TypeTree tree;

	public ModelRegistry(TypeTree tree) {
		this.tree = tree;
		this.modelMap = new HashMap<>();
		this.paletteRemapper = new HashMap<>();
		this.textures = new HashMap<>();
		this.delayedModels = new HashMap<>();
		this.generationModels = new HashMap<>();
	}

	public ModelRegistry() {
		this.tree = new TypeTree();
		this.modelMap = new HashMap<>();
		this.textures = new HashMap<>();
		this.delayedModels = new HashMap<>();
		this.paletteRemapper = new HashMap<>();
		this.generationModels = new HashMap<>();
	}

	public ResourceListener<List<DataResource>> modelListener() {
		return (resources, tree, idMapper) -> {
			this.tree = tree;
			resources.stream().filter(resource -> resource.models().size() > 0).forEach(this::register);
		};
	}

	public ResourceListener<List<DataResource>> paletteListener() {
		return (resources, tree, idMapper) -> resources.stream().filter(res -> res.palette().isPresent()).forEach(res -> paletteHandler(res, tree));
	}

	private void paletteHandler(DataResource resource, TypeTree tree) {
		var paletteData = resource.palette();
		if (paletteData.isPresent()) {
			tree.find(resource.type()).ifPresent(node -> node.addResource(paletteData.get().toBuilder().target(resource.name()).build(), PaletteData.class));
			if (!paletteData.get().getName().equals(resource.name())) {
				paletteRemapper.put(resource.name() + ".png", paletteData.get().getName() + ".png");
			}
		}
	}

	public void setTree(TypeTree tree) {
		this.tree = tree;
	}

	public ModelRegistry register(DataResource data) {
		var converter = new ModelConverter(tree, modelMap, textures, delayedModels, generationModels);
		converter.register(data);
		return this;
	}

	public Optional<ModelTemplate> find(State state) {
		var context = Context.of();
		if (modelMap.containsKey(state.identifier())) {
			return modelMap.get(state.identifier()).get(state, this::provider, Context.of());
		} else {
			var modelEntries = tree.find(state.type().typeName()).map(node -> node.getResources(ModelMatcher.class)).orElse(ImmutableList.<ModelMatcher>builder().build());
			return modelEntries.stream().sorted(ModelMatcher::comparator).filter(entry -> entry.match(state, context)).map(modelMatcher -> modelMatcher.get(state, this::provider, context)).flatMap(Optional::stream).findFirst();
		}
	}


	public Optional<ModelMatcher> provider(Identifiable id) {
		if (modelMap.containsKey(id.identifier())) {
			return Optional.ofNullable(modelMap.get(id.identifier()));
		} else if (modelMap.containsKey(id.name())) {
			return Optional.ofNullable(modelMap.get(id.name()));
		} else if (id instanceof State state) {
			return Optional.of(MultipleModelMatcher.of(tree.find(state.type().typeName()).map(node -> node.getResources(ModelMatcher.class)).orElse(ImmutableList.<ModelMatcher>builder().build())));
		}
		return Optional.empty();
	}

	public Map<String, PaletteTemplateModel> getTextures() {
		return textures;
	}

	public Map<String, String> getPaletteRemapper() {
		return paletteRemapper;
	}
}
