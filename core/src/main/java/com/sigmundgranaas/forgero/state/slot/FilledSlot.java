package com.sigmundgranaas.forgero.state.slot;

import com.sigmundgranaas.forgero.property.Attribute;
import com.sigmundgranaas.forgero.property.Property;
import com.sigmundgranaas.forgero.property.Target;
import com.sigmundgranaas.forgero.property.attribute.AttributeBuilder;
import com.sigmundgranaas.forgero.property.attribute.Category;
import com.sigmundgranaas.forgero.state.Composite;
import com.sigmundgranaas.forgero.state.Slot;
import com.sigmundgranaas.forgero.state.State;
import com.sigmundgranaas.forgero.type.Type;
import com.sigmundgranaas.forgero.util.match.Context;
import com.sigmundgranaas.forgero.util.match.Matchable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class FilledSlot extends AbstractTypedSlot {
    private final State upgrade;

    public FilledSlot(int index, Type type, State upgrade, String description, Set<Category> categories) {
        super(index, type, description, categories);
        this.upgrade = upgrade;
    }

    @Override
    public @NotNull List<Property> getRootProperties() {
        return filterProperties(Target.EMPTY);
    }

    @Override
    public @NotNull List<Property> applyProperty(Target target) {
        return filterProperties(target);
    }

    private List<Property> filterProperties(Target target) {
        var properties = upgrade.applyProperty(target);
        var otherProperties = properties.stream().filter(property -> !(property instanceof Attribute)).toList();
        var attributes = Property.stream(properties)
                .getAttributes()
                .filter(this::filterAttribute)
                .map(attribute -> AttributeBuilder.createAttributeBuilderFromAttribute(attribute).applyCategory(Category.ALL).build())
                .toList();
        return Stream.of(otherProperties, attributes)
                .flatMap(List::stream)
                .map(Property.class::cast)
                .toList();
    }

    private boolean filterAttribute(Attribute attribute) {
        if (attribute.getCategory() == Category.COMPOSITE && upgrade instanceof Composite) {
            return true;
        } else if (categories.contains(attribute.getCategory())) {
            return true;
        } else if (attribute.getCategory() == Category.ALL) {
            return true;
        } else if (attribute.getCategory() == Category.LOCAL) {
            return false;
        }
        return false;
    }

    @Override
    public boolean filled() {
        return true;
    }

    @Override
    public Optional<State> get() {
        return Optional.of(upgrade);
    }

    @Override
    public Slot empty() {
        return new EmptySlot(index(), type(), description(), categories);
    }

    @Override
    public Optional<Slot> fill(State slottable, Set<Category> categories) {
        return Optional.empty();
    }

    @Override
    public boolean test(Matchable match, Context context) {
        if (type().test(match, context)) {
            return true;
        } else {
            return upgrade.test(match, context);
        }
    }
}
