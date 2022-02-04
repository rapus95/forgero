package com.sigmundgranaas.forgero.gametest;

import com.sigmundgranaas.forgero.Forgero;
import com.sigmundgranaas.forgero.core.tool.ForgeroToolCollection;
import com.sigmundgranaas.forgero.core.toolpart.ForgeroToolPartCollection;
import com.sigmundgranaas.forgero.item.ForgeroToolItem;
import com.sigmundgranaas.forgero.item.ToolPartItem;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.test.GameTest;
import net.minecraft.test.GameTestException;
import net.minecraft.test.TestContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistration {
    @GameTest(structureName = FabricGameTest.EMPTY_STRUCTURE, batchId = "Testing item registration")
    public void allToolsHaveBeenRegistered(TestContext context) {
        ForgeroToolCollection.INSTANCE.getTools().forEach(forgeroTool -> {
            Item checkedTool = Registry.ITEM.get(new Identifier(Forgero.MOD_NAMESPACE, forgeroTool.getShortToolIdentifierString()));
            if (checkedTool == Items.AIR && !(checkedTool instanceof ForgeroToolItem)) {
                String message = String.format("%s has not been registered correctly", forgeroTool.getToolIdentifierString());
                Forgero.LOGGER.error(message);
                throw new GameTestException(message);
            }

        });
        context.complete();
    }

    @GameTest(structureName = FabricGameTest.EMPTY_STRUCTURE, batchId = "Testing item registration")
    public void allToolPartsHaveBeenRegistered(TestContext context) {
        ForgeroToolPartCollection.INSTANCE.getToolParts().forEach(forgeroTool -> {
            Item checkedTool = Registry.ITEM.get(new Identifier(Forgero.MOD_NAMESPACE, forgeroTool.getToolPartIdentifier()));
            if (checkedTool == Items.AIR && !(checkedTool instanceof ToolPartItem)) {
                String message = String.format("%s has not been registered correctly", forgeroTool.getToolPartIdentifier());
                Forgero.LOGGER.error(message);
                throw new GameTestException(message);
            }
        });
        context.complete();
    }
}
