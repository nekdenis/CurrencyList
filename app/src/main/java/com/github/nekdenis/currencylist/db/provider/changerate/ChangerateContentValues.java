package com.github.nekdenis.currencylist.db.provider.changerate;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import com.github.nekdenis.currencylist.db.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code changerate} table.
 */
public class ChangerateContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return ChangerateColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, ChangerateSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public ChangerateContentValues putPath(String value) {
        if (value == null) throw new IllegalArgumentException("value for path must not be null");
        mContentValues.put(ChangerateColumns.PATH, value);
        return this;
    }



    public ChangerateContentValues putName(String value) {
        if (value == null) throw new IllegalArgumentException("value for name must not be null");
        mContentValues.put(ChangerateColumns.NAME, value);
        return this;
    }



    public ChangerateContentValues putRate(String value) {
        if (value == null) throw new IllegalArgumentException("value for rate must not be null");
        mContentValues.put(ChangerateColumns.RATE, value);
        return this;
    }



    public ChangerateContentValues putDate(String value) {
        if (value == null) throw new IllegalArgumentException("value for date must not be null");
        mContentValues.put(ChangerateColumns.DATE, value);
        return this;
    }



    public ChangerateContentValues putTime(String value) {
        if (value == null) throw new IllegalArgumentException("value for time must not be null");
        mContentValues.put(ChangerateColumns.TIME, value);
        return this;
    }



    public ChangerateContentValues putAsk(String value) {
        if (value == null) throw new IllegalArgumentException("value for ask must not be null");
        mContentValues.put(ChangerateColumns.ASK, value);
        return this;
    }



    public ChangerateContentValues putBid(String value) {
        if (value == null) throw new IllegalArgumentException("value for bid must not be null");
        mContentValues.put(ChangerateColumns.BID, value);
        return this;
    }


}
