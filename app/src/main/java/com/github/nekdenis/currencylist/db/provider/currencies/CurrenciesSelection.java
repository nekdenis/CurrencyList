package com.github.nekdenis.currencylist.db.provider.currencies;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.github.nekdenis.currencylist.db.provider.base.AbstractSelection;

/**
 * Selection for the {@code currencies} table.
 */
public class CurrenciesSelection extends AbstractSelection<CurrenciesSelection> {
    @Override
    public Uri uri() {
        return CurrenciesColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code CurrenciesCursor} object, which is positioned before the first entry, or null.
     */
    public CurrenciesCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new CurrenciesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public CurrenciesCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public CurrenciesCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public CurrenciesSelection id(long... value) {
        addEquals("currencies." + CurrenciesColumns._ID, toObjectArray(value));
        return this;
    }


    public CurrenciesSelection path(String... value) {
        addEquals(CurrenciesColumns.PATH, value);
        return this;
    }

    public CurrenciesSelection pathNot(String... value) {
        addNotEquals(CurrenciesColumns.PATH, value);
        return this;
    }

    public CurrenciesSelection pathLike(String... value) {
        addLike(CurrenciesColumns.PATH, value);
        return this;
    }

    public CurrenciesSelection name(String... value) {
        addEquals(CurrenciesColumns.NAME, value);
        return this;
    }

    public CurrenciesSelection nameNot(String... value) {
        addNotEquals(CurrenciesColumns.NAME, value);
        return this;
    }

    public CurrenciesSelection nameLike(String... value) {
        addLike(CurrenciesColumns.NAME, value);
        return this;
    }

    public CurrenciesSelection lastrate(String... value) {
        addEquals(CurrenciesColumns.LASTRATE, value);
        return this;
    }

    public CurrenciesSelection lastrateNot(String... value) {
        addNotEquals(CurrenciesColumns.LASTRATE, value);
        return this;
    }

    public CurrenciesSelection lastrateLike(String... value) {
        addLike(CurrenciesColumns.LASTRATE, value);
        return this;
    }
}
