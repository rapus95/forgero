package com.sigmundgranaas.forgero.item.items.tool;

import com.sigmundgranaas.forgero.ForgeroInitializer;
import com.sigmundgranaas.forgero.core.properties.attribute.Target;
import com.sigmundgranaas.forgero.core.tool.ForgeroTool;
import com.sigmundgranaas.forgero.core.tool.ForgeroToolTypes;
import com.sigmundgranaas.forgero.core.toolpart.handle.ToolPartHandle;
import com.sigmundgranaas.forgero.core.toolpart.head.ToolPartHead;
import com.sigmundgranaas.forgero.item.ForgeroToolItem;
import com.sigmundgranaas.forgero.item.adapter.DescriptionWriter;
import com.sigmundgranaas.forgero.item.adapter.FabricToForgeroToolAdapter;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;
import java.util.Locale;

public class ForgeroAxeItem extends AxeItem implements ForgeroToolItem, DynamicAttributeTool {

    final Tag<Item> toolType = FabricToolTags.AXES;
    private final FabricToForgeroToolAdapter toolAdapter = FabricToForgeroToolAdapter.createAdapter();
    private final ForgeroTool tool;

    public ForgeroAxeItem(ToolMaterial toolMaterial, Settings settings, ForgeroTool tool) {
        super(toolMaterial, (int) tool.getAttackDamage(Target.createEmptyTarget()), tool.getAttackSpeed(Target.createEmptyTarget()), settings);

        this.tool = tool;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        ForgeroTool forgeroTool = toolAdapter.getTool(itemStack).orElse(tool);
        forgeroTool.createToolDescription(new DescriptionWriter(tooltip));
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
    }


    @Override
    public Identifier getIdentifier() {
        return new Identifier(ForgeroInitializer.MOD_NAMESPACE, tool.getToolIdentifierString());
    }

    @Override
    public ForgeroToolTypes getToolType() {
        return ForgeroToolTypes.AXE;
    }

    @Override
    public ForgeroTool getTool() {
        return tool;
    }

    @Override
    public ToolPartHead getHead() {
        return tool.getToolHead();
    }

    @Override
    public ToolPartHandle getHandle() {
        return tool.getToolHandle();
    }

    @Override
    public FabricToForgeroToolAdapter getToolAdapter() {
        return toolAdapter;
    }

    @Override
    public Tag<Item> getToolTags() {
        return toolType;
    }

    @Override
    protected String getOrCreateTranslationKey() {
        return String.format("item.%s.%s_%s", ForgeroInitializer.MOD_NAMESPACE, tool.getToolHead().getPrimaryMaterial().getName(), getToolType().toString().toLowerCase(Locale.ROOT));
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return getCustomItemBarStep(stack);
    }


}
