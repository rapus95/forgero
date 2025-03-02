package com.sigmundgranaas.forgero.minecraft.common.item;

import java.util.List;

import com.sigmundgranaas.forgero.core.customdata.DataContainer;
import com.sigmundgranaas.forgero.core.property.PropertyContainer;
import com.sigmundgranaas.forgero.core.state.LeveledState;
import com.sigmundgranaas.forgero.core.state.State;
import com.sigmundgranaas.forgero.core.type.Type;
import com.sigmundgranaas.forgero.core.util.match.Context;
import com.sigmundgranaas.forgero.core.util.match.Matchable;
import com.sigmundgranaas.forgero.minecraft.common.conversion.StateConverter;
import com.sigmundgranaas.forgero.minecraft.common.tooltip.Writer;
import com.sigmundgranaas.forgero.minecraft.common.tooltip.v2.DefaultWriter;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class GemItem extends Item implements StateItem, State {
	private final State DEFAULT;

	public GemItem(Settings settings, State defaultState) {
		super(settings);
		this.DEFAULT = defaultState;
	}

	@Override
	public State defaultState() {
		return DEFAULT;
	}

	@Override
	public Text getName() {
		return Writer.nameToTranslatableText(DEFAULT);
	}

	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		new DefaultWriter(dynamicState(itemStack)).write(tooltip, tooltipContext);
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
	}

	@Override
	public Text getName(ItemStack stack) {
		var state = StateConverter.of(stack).orElse(DEFAULT);
		var text = Text.empty();
		if (state instanceof LeveledState leveledState) {
			text.append(Text.literal(String.format("Level %s ", leveledState.level())));
		}
		text.append(Writer.nameToTranslatableText(DEFAULT));
		return text;
	}


	@Override
	public String name() {
		return DEFAULT.name();
	}

	@Override
	public String nameSpace() {
		return DEFAULT.nameSpace();
	}

	@Override
	public Type type() {
		return DEFAULT.type();
	}

	@Override
	public boolean test(Matchable match, Context context) {
		return DEFAULT.test(match, context);
	}

	@Override
	public PropertyContainer dynamicProperties(ItemStack stack) {
		return dynamicState(stack);
	}

	@Override
	public PropertyContainer defaultProperties() {
		return DEFAULT;
	}

	@Override
	public boolean isEquippable() {
		return false;
	}

	@Override
	public DataContainer customData() {
		return defaultState().customData();
	}
}
