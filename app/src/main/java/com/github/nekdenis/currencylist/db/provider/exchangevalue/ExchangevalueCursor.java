package com.github.nekdenis.currencylist.db.provider.exchangevalue;

import android.database.Cursor;

import com.github.nekdenis.currencylist.db.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code exchangevalue} table.
 */
public class ExchangevalueCursor extends AbstractCursor {
    public ExchangevalueCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code pathval} value.
     * Cannot be {@code null}.
     */
    public String getPathval() {
        Integer index = getCachedColumnIndexOrThrow(ExchangevalueColumns.PATHVAL);
        return getString(index);
    }

    /**
     * Get the {@code title} value.
     * Cannot be {@code null}.
     */
    public String getTitle() {
        Integer index = getCachedColumnIndexOrThrow(ExchangevalueColumns.TITLE);
        return getString(index);
    }

    /**
     * Get the {@code rate} value.
     * Cannot be {@code null}.
     */
    public String getRate() {
        Integer index = getCachedColumnIndexOrThrow(ExchangevalueColumns.RATE);
        return getString(index);
    }

    /**
     * Get the {@code date} value.
     */
    public long getDate() {
        return getLongOrNull(ExchangevalueColumns.DATE);
    }

    /**
     * Get the {@code datehours} value.
     */
    public long getDatehours() {
        return getLongOrNull(ExchangevalueColumns.DATEHOURS);
    }

    /**
     * Get the {@code ask} value.
     * Cannot be {@code null}.
     */
    public String getAsk() {
        Integer index = getCachedColumnIndexOrThrow(ExchangevalueColumns.ASK);
        return getString(index);
    }

    /**
     * Get the {@code bid} value.
     * Cannot be {@code null}.
     */
    public String getBid() {
        Integer index = getCachedColumnIndexOrThrow(ExchangevalueColumns.BID);
        return getString(index);
    }
}
