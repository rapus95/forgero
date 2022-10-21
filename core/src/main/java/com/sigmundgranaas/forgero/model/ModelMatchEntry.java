package com.sigmundgranaas.forgero.model;

import com.sigmundgranaas.forgero.util.match.Context;
import com.sigmundgranaas.forgero.util.match.Matchable;

public record ModelMatchEntry(String entry) implements Matchable {
    @Override
    public boolean test(Matchable match, Context context) {
        if (match instanceof ModelMatchEntry entry) {
            if (entry.entry.equals(this.entry)) {
                return true;
            }
        }
        return false;
    }
}
