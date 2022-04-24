package com.sigmundgranaas.forgero.core.property.active;

import com.sigmundgranaas.forgero.core.property.attribute.Target;

public record VeinBreaking(int depth) implements ActiveProperty {
    @Override
    public ActivePropertyType getActiveType() {
        return ActivePropertyType.VEIN_MINING_PATTERN;
    }
}
