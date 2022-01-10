package com.sigmundgranaas.forgero.core.identifier.model.texture;

import com.sigmundgranaas.forgero.client.forgerotool.model.ToolPartModelType;
import com.sigmundgranaas.forgero.core.identifier.tool.ForgeroMaterialIdentifier;

public interface ToolPartModelIdentifier {
    String getIdentifier();

    ToolPartModelType getModelType();

    ForgeroMaterialIdentifier getMaterialIdentifier();

    TextureIdentifier getTextureIdentifier();
}
