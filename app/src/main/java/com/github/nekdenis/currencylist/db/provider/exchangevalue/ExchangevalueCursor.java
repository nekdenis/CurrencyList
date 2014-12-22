package com.github.nekdenis.currencylist.db.provider.exchangevalue;

import java.util.Date;

import android.database.Cursor;

import com.github.nekdenis.currencylist.db.provider.base.AbstractCursor;
import com.github.nekdenis.currencylist.db.provider.currencies.*;

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
     * Get the {@code path} value.
     * Cannot be {@code null}.
     */
    public String getCurrenciesPath() {
        Integer index = getCachedColumnIndexOrThrow(CurrenciesColumns.PATH);
        return getString(index);
    }

    /**
     * Get the {@code name} value.
     * Cannot be {@code null}.
     */
    public String getCurrenciesName() {
        Integer index = getCachedColumnIndexOrThrow(CurrenciesColumns.NAME);
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
     * Cannot be {@code null}.
     */
    public String getDate() {
        Integer index = getCachedColumnIndexOrThrow(ExchangevalueColumns.DATE);
        return getString(index);
    }

    /**
     * Get the {@code time} value.
     * Cannot be {@code null}.
     */
    public String getTime() {
        Integer index = getCachedColumnIndexOrThrow(ExchangevalueColumns.TIME);
        return getString(index);
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
