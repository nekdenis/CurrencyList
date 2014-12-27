package com.github.nekdenis.currencylist.db.provider.currencies;

import java.util.Date;

import android.database.Cursor;

import com.github.nekdenis.currencylist.db.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code currencies} table.
 */
public class CurrenciesCursor extends AbstractCursor {
    public CurrenciesCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code path} value.
     * Cannot be {@code null}.
     */
    public String getPath() {
        Integer index = getCachedColumnIndexOrThrow(CurrenciesColumns.PATH);
        return getString(index);
    }

    /**
     * Get the {@code name} value.
     * Cannot be {@code null}.
     */
    public String getName() {
        Integer index = getCachedColumnIndexOrThrow(CurrenciesColumns.NAME);
        return getString(index);
    }

    /**
     * Get the {@code lastrate} value.
     * Can be {@code null}.
     */
    public String getLastrate() {
        Integer index = getCachedColumnIndexOrThrow(CurrenciesColumns.LASTRATE);
        return getString(index);
    }
}
