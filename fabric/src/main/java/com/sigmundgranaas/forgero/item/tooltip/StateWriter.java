package com.sigmundgranaas.forgero.item.tooltip;

import com.sigmundgranaas.forgero.item.tooltip.writer.*;
import com.sigmundgranaas.forgero.state.Composite;
import com.sigmundgranaas.forgero.state.LeveledState;
import com.sigmundgranaas.forgero.state.State;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;

import java.util.List;

import static com.sigmundgranaas.forgero.type.Type.*;

public class StateWriter implements Writer {
    protected final State state;

    public StateWriter(State state) {
        this.state = state;
    }

    public static Writer of(State state) {
        if (state.test(AXE)) {
            return new AxeWriter(state);
        } else if (state.test(TOOL)) {
            return new ToolWriter(state);
        } else if (state.test(SWORD_BLADE)) {
            return new SwordBladeWriter(state);
        } else if (state.test(AXE_HEAD)) {
            return new SwordBladeWriter(state);
        } else if (state.test(TOOL_PART_HEAD)) {
            return new AxeHeadWriter(state);
        } else if (state.test(PART)) {
            return new PartWriter((state));
        } else if (state.test(SCHEMATIC)) {
            return new SchematicWriter(state);
        } else if (state.test(GEM) && state instanceof LeveledState leveledState) {
            return new GemWriter(leveledState);
        }
        return new StateWriter(state);
    }

    @Override
    public void write(List<Text> tooltip, TooltipContext context) {
        if (this.state instanceof Composite composite) {
            new CompositeWriter(composite).write(tooltip, context);
        }
        writePassives(tooltip, context);
    }

    private void writePassives(List<Text> tooltip, TooltipContext context) {
        var writer = new PassiveWriter();
        state.stream().getPassiveProperties().forEach(writer::addPassive);
        writer.write(tooltip, context);
    }
}
