package com.sigmundgranaas.forgero.core.material.material;

import com.google.gson.JsonObject;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PrimaryMaterialImpl extends AbstractMaterial implements PrimaryMaterial {
    private JsonObject ingredient;

    public PrimaryMaterialImpl(MaterialPOJO material) {
        super(material);
        ingredient = material.primary.repairIngredient;
    }

    @Override
    public int getRarity() {
        return 0;
    }

    @Override
    public @NotNull
    List<Identifier> getPaletteIdentifiers() {
        return null;
    }

    @Override
    public @NotNull
    List<Identifier> getPaletteExclusionIdentifiers() {
        return null;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public int getStiffNess() {
        return 0;
    }

    @Override
    public int getDurability() {
        return 0;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 0;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.fromJson(ingredient);
    }

    @Override
    public int getSharpness() {
        return 0;
    }

    @Override
    public int getFlexibility() {
        return 0;
    }
}
