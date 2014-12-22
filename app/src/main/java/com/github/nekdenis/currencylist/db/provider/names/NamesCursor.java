package com.github.nekdenis.currencylist.db.provider.names;

import java.util.Date;

import android.database.Cursor;

import com.github.nekdenis.currencylist.db.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code names} table.
 */
public class NamesCursor extends AbstractCursor {
    public NamesCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code names} value.
     * Cannot be {@code null}.
     */
    public String getNames() {
        Integer index = getCachedColumnIndexOrThrow(NamesColumns.NAMES);
        return getString(index);
    }

    /**
     * Get the {@code name} value.
     * Cannot be {@code null}.
     */
    public String getName() {
        Integer index = getCachedColumnIndexOrThrow(NamesColumns.NAME);
        return getString(index);
    }
}
