package com.github.nekdenis.currencylist.db.provider.changerate;

import java.util.Date;

import android.database.Cursor;

import com.github.nekdenis.currencylist.db.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code changerate} table.
 */
public class ChangerateCursor extends AbstractCursor {
    public ChangerateCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code path} value.
     * Cannot be {@code null}.
     */
    public String getPath() {
        Integer index = getCachedColumnIndexOrThrow(ChangerateColumns.PATH);
        return getString(index);
    }

    /**
     * Get the {@code name} value.
     * Cannot be {@code null}.
     */
    public String getName() {
        Integer index = getCachedColumnIndexOrThrow(ChangerateColumns.NAME);
        return getString(index);
    }

    /**
     * Get the {@code rate} value.
     * Cannot be {@code null}.
     */
    public String getRate() {
        Integer index = getCachedColumnIndexOrThrow(ChangerateColumns.RATE);
        return getString(index);
    }

    /**
     * Get the {@code date} value.
     * Cannot be {@code null}.
     */
    public String getDate() {
        Integer index = getCachedColumnIndexOrThrow(ChangerateColumns.DATE);
        return getString(index);
    }

    /**
     * Get the {@code time} value.
     * Cannot be {@code null}.
     */
    public String getTime() {
        Integer index = getCachedColumnIndexOrThrow(ChangerateColumns.TIME);
        return getString(index);
    }

    /**
     * Get the {@code ask} value.
     * Cannot be {@code null}.
     */
    public String getAsk() {
        Integer index = getCachedColumnIndexOrThrow(ChangerateColumns.ASK);
        return getString(index);
    }

    /**
     * Get the {@code bid} value.
     * Cannot be {@code null}.
     */
    public String getBid() {
        Integer index = getCachedColumnIndexOrThrow(ChangerateColumns.BID);
        return getString(index);
    }
}
