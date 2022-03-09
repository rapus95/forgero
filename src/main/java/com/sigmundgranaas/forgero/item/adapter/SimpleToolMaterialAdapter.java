package com.sigmundgranaas.forgero.item.adapter;

import com.google.gson.JsonObject;
import com.sigmundgranaas.forgero.core.gem.EmptyGem;
import com.sigmundgranaas.forgero.core.material.material.EmptySecondaryMaterial;
import com.sigmundgranaas.forgero.core.material.material.PrimaryMaterial;
import com.sigmundgranaas.forgero.core.properties.AttributeType;
import com.sigmundgranaas.forgero.core.properties.Property;
import com.sigmundgranaas.forgero.core.properties.attribute.Target;
import com.sigmundgranaas.forgero.core.toolpart.head.HeadState;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class SimpleToolMaterialAdapter implements ToolMaterial {
    private final HeadState state;

    public SimpleToolMaterialAdapter(PrimaryMaterial material) {
        this.state = new HeadState(material, new EmptySecondaryMaterial(), EmptyGem.createEmptyGem());

    }

    @Override
    public int getDurability() {
        return (int) Property.stream(state.getProperties(Target.createEmptyTarget())).applyAttribute(Target.createEmptyTarget(), AttributeType.DURABILITY);
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return Property.stream(state.getProperties(Target.createEmptyTarget())).applyAttribute(Target.createEmptyTarget(), AttributeType.MINING_SPEED);
    }

    @Override
    public float getAttackDamage() {
        return Property.stream(state.getProperties(Target.createEmptyTarget())).applyAttribute(Target.createEmptyTarget(), AttributeType.ATTACK_DAMAGE);
    }

    @Override
    public int getMiningLevel() {
        return (int) Property.stream(state.getProperties(Target.createEmptyTarget())).applyAttribute(Target.createEmptyTarget(), AttributeType.MINING_LEVEL);
    }

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public Ingredient getRepairIngredient() {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", state.getPrimaryMaterial().getIngredient());
        return Ingredient.fromJson(ingredient);
    }
}
