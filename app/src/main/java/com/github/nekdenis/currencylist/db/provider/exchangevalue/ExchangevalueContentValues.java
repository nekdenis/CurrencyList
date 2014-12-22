package com.github.nekdenis.currencylist.db.provider.exchangevalue;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import com.github.nekdenis.currencylist.db.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code exchangevalue} table.
 */
public class ExchangevalueContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return ExchangevalueColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, ExchangevalueSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public ExchangevalueContentValues putPathval(String value) {
        if (value == null) throw new IllegalArgumentException("value for pathval must not be null");
        mContentValues.put(ExchangevalueColumns.PATHVAL, value);
        return this;
    }



    public ExchangevalueContentValues putTitle(String value) {
        if (value == null) throw new IllegalArgumentException("value for title must not be null");
        mContentValues.put(ExchangevalueColumns.TITLE, value);
        return this;
    }



    public ExchangevalueContentValues putRate(String value) {
        if (value == null) throw new IllegalArgumentException("value for rate must not be null");
        mContentValues.put(ExchangevalueColumns.RATE, value);
        return this;
    }



    public ExchangevalueContentValues putDate(String value) {
        if (value == null) throw new IllegalArgumentException("value for date must not be null");
        mContentValues.put(ExchangevalueColumns.DATE, value);
        return this;
    }



    public ExchangevalueContentValues putTime(String value) {
        if (value == null) throw new IllegalArgumentException("value for time must not be null");
        mContentValues.put(ExchangevalueColumns.TIME, value);
        return this;
    }



    public ExchangevalueContentValues putAsk(String value) {
        if (value == null) throw new IllegalArgumentException("value for ask must not be null");
        mContentValues.put(ExchangevalueColumns.ASK, value);
        return this;
    }



    public ExchangevalueContentValues putBid(String value) {
        if (value == null) throw new IllegalArgumentException("value for bid must not be null");
        mContentValues.put(ExchangevalueColumns.BID, value);
        return this;
    }


}
