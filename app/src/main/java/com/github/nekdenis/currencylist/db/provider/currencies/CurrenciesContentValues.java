package com.github.nekdenis.currencylist.db.provider.currencies;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import com.github.nekdenis.currencylist.db.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code currencies} table.
 */
public class CurrenciesContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return CurrenciesColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, CurrenciesSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public CurrenciesContentValues putPath(String value) {
        if (value == null) throw new IllegalArgumentException("value for path must not be null");
        mContentValues.put(CurrenciesColumns.PATH, value);
        return this;
    }



    public CurrenciesContentValues putName(String value) {
        if (value == null) throw new IllegalArgumentException("value for name must not be null");
        mContentValues.put(CurrenciesColumns.NAME, value);
        return this;
    }


}
