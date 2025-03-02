package com.sigmundgranaas.forgero.core.resource.data.v2.loading;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.sigmundgranaas.forgero.core.Forgero;
import com.sigmundgranaas.forgero.core.resource.data.v2.DataResourceProvider;
import com.sigmundgranaas.forgero.core.resource.data.v2.data.ContextData;
import com.sigmundgranaas.forgero.core.resource.data.v2.data.DataResource;
import com.sigmundgranaas.forgero.core.util.loader.ClassLoader;
import com.sigmundgranaas.forgero.core.util.loader.InputStreamLoader;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public class FileResourceProvider implements DataResourceProvider {

	private final String path;

	private final InputStreamLoader streamLoader;

	public FileResourceProvider(String path, InputStreamLoader streamLoader) {
		this.path = path;
		this.streamLoader = streamLoader;
	}

	public FileResourceProvider(String path) {
		this.path = path;
		this.streamLoader = new ClassLoader();
	}

	@Override
	public Optional<DataResource> get() {
		Optional<InputStream> optDataStream = streamLoader.load(path);
		if (optDataStream.isPresent()) {
			try {
				InputStream stream = optDataStream.get();
				JsonReader reader = new JsonReader(new InputStreamReader(stream));
				var context = createContextFromPath(path);
				DataResource gson = new Gson().fromJson(reader, DataResource.class);
				if (gson != null) {
					var resource = gson.toBuilder().context(context).build();
					if (resource == null) {
						Forgero.LOGGER.error("Unable to load: {}, check if the file is valid", path);
					}
					return Optional.ofNullable(resource);
				}
				return Optional.empty();
			} catch (JsonSyntaxException e) {
				Forgero.LOGGER.error("Unable to parse: {}, check if the file is valid", path);
				Forgero.LOGGER.error(e);
				return Optional.empty();
			}
		} else {
			Forgero.LOGGER.error("Unable to load: {}", path);
			return Optional.empty();
		}
	}

	public ContextData createContextFromPath(String filePath) {
		var builder = ContextData.builder();
		String[] elements = filePath.split("\\" + File.separator);
		if (elements.length == 1) {
			elements = filePath.split("/");
		}
		var fileName = elements[elements.length - 1];
		var folder = elements[elements.length - 2];
		var path = filePath.replace(fileName, "");
		return builder.fileName(fileName).folder(folder).path(path).build();
	}
}
