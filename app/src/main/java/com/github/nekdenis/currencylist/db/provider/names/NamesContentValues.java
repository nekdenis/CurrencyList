package com.github.nekdenis.currencylist.db.provider.names;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import com.github.nekdenis.currencylist.db.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code names} table.
 */
public class NamesContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return NamesColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, NamesSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public NamesContentValues putNames(String value) {
        if (value == null) throw new IllegalArgumentException("value for names must not be null");
        mContentValues.put(NamesColumns.NAMES, value);
        return this;
    }



    public NamesContentValues putName(String value) {
        if (value == null) throw new IllegalArgumentException("value for name must not be null");
        mContentValues.put(NamesColumns.NAME, value);
        return this;
    }


}
