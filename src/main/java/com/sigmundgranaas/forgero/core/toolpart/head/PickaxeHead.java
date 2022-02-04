package com.sigmundgranaas.forgero.core.toolpart.head;

import com.sigmundgranaas.forgero.core.tool.ForgeroToolTypes;
import com.sigmundgranaas.forgero.core.toolpart.ForgeroToolPartTypes;

public class PickaxeHead extends AbstractToolPartHead {
    public PickaxeHead(ToolPartHeadStrategy headStrategy) {
        super(headStrategy);
    }

    @Override
    public int getDurability() {
        return getPrimaryMaterial().getDurability();
    }


    @Override
    public String getToolTypeName() {
        return this.getToolType().getToolName();
    }

    @Override
    public String getToolPartIdentifier() {
        return getPrimaryMaterial().getName() + "_" + getToolPartName();
    }

    @Override
    public ForgeroToolPartTypes getToolPartType() {
        return ForgeroToolPartTypes.HEAD;
    }


    @Override
    public ForgeroToolTypes getToolType() {
        return ForgeroToolTypes.PICKAXE;
    }

    @Override
    public float getAttackSpeed() {
        return headStrategy.getAttackSpeed() - 2.8f;
    }

    @Override
    public float getAttackDamage() {
        return 1 + headStrategy.getAttackDamage();
    }
}
