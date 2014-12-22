package com.github.nekdenis.currencylist.db.provider.names;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.github.nekdenis.currencylist.db.provider.base.AbstractSelection;

/**
 * Selection for the {@code names} table.
 */
public class NamesSelection extends AbstractSelection<NamesSelection> {
    @Override
    public Uri uri() {
        return NamesColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code NamesCursor} object, which is positioned before the first entry, or null.
     */
    public NamesCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new NamesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public NamesCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public NamesCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public NamesSelection id(long... value) {
        addEquals("names." + NamesColumns._ID, toObjectArray(value));
        return this;
    }


    public NamesSelection names(String... value) {
        addEquals(NamesColumns.NAMES, value);
        return this;
    }

    public NamesSelection namesNot(String... value) {
        addNotEquals(NamesColumns.NAMES, value);
        return this;
    }

    public NamesSelection namesLike(String... value) {
        addLike(NamesColumns.NAMES, value);
        return this;
    }

    public NamesSelection name(String... value) {
        addEquals(NamesColumns.NAME, value);
        return this;
    }

    public NamesSelection nameNot(String... value) {
        addNotEquals(NamesColumns.NAME, value);
        return this;
    }

    public NamesSelection nameLike(String... value) {
        addLike(NamesColumns.NAME, value);
        return this;
    }
}
