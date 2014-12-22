package com.github.nekdenis.currencylist.db.provider.changerate;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.github.nekdenis.currencylist.db.provider.base.AbstractSelection;
import com.github.nekdenis.currencylist.db.provider.names.*;

/**
 * Selection for the {@code changerate} table.
 */
public class ChangerateSelection extends AbstractSelection<ChangerateSelection> {
    @Override
    public Uri uri() {
        return ChangerateColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code ChangerateCursor} object, which is positioned before the first entry, or null.
     */
    public ChangerateCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new ChangerateCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public ChangerateCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public ChangerateCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public ChangerateSelection id(long... value) {
        addEquals("changerate." + ChangerateColumns._ID, toObjectArray(value));
        return this;
    }


    public ChangerateSelection names(String... value) {
        addEquals(ChangerateColumns.NAMES, value);
        return this;
    }

    public ChangerateSelection namesNot(String... value) {
        addNotEquals(ChangerateColumns.NAMES, value);
        return this;
    }

    public ChangerateSelection namesLike(String... value) {
        addLike(ChangerateColumns.NAMES, value);
        return this;
    }

    public ChangerateSelection namesNames(String... value) {
        addEquals(NamesColumns.NAMES, value);
        return this;
    }

    public ChangerateSelection namesNamesNot(String... value) {
        addNotEquals(NamesColumns.NAMES, value);
        return this;
    }

    public ChangerateSelection namesNamesLike(String... value) {
        addLike(NamesColumns.NAMES, value);
        return this;
    }

    public ChangerateSelection namesName(String... value) {
        addEquals(NamesColumns.NAME, value);
        return this;
    }

    public ChangerateSelection namesNameNot(String... value) {
        addNotEquals(NamesColumns.NAME, value);
        return this;
    }

    public ChangerateSelection namesNameLike(String... value) {
        addLike(NamesColumns.NAME, value);
        return this;
    }

    public ChangerateSelection title(String... value) {
        addEquals(ChangerateColumns.TITLE, value);
        return this;
    }

    public ChangerateSelection titleNot(String... value) {
        addNotEquals(ChangerateColumns.TITLE, value);
        return this;
    }

    public ChangerateSelection titleLike(String... value) {
        addLike(ChangerateColumns.TITLE, value);
        return this;
    }

    public ChangerateSelection rate(String... value) {
        addEquals(ChangerateColumns.RATE, value);
        return this;
    }

    public ChangerateSelection rateNot(String... value) {
        addNotEquals(ChangerateColumns.RATE, value);
        return this;
    }

    public ChangerateSelection rateLike(String... value) {
        addLike(ChangerateColumns.RATE, value);
        return this;
    }

    public ChangerateSelection date(String... value) {
        addEquals(ChangerateColumns.DATE, value);
        return this;
    }

    public ChangerateSelection dateNot(String... value) {
        addNotEquals(ChangerateColumns.DATE, value);
        return this;
    }

    public ChangerateSelection dateLike(String... value) {
        addLike(ChangerateColumns.DATE, value);
        return this;
    }

    public ChangerateSelection time(String... value) {
        addEquals(ChangerateColumns.TIME, value);
        return this;
    }

    public ChangerateSelection timeNot(String... value) {
        addNotEquals(ChangerateColumns.TIME, value);
        return this;
    }

    public ChangerateSelection timeLike(String... value) {
        addLike(ChangerateColumns.TIME, value);
        return this;
    }

    public ChangerateSelection ask(String... value) {
        addEquals(ChangerateColumns.ASK, value);
        return this;
    }

    public ChangerateSelection askNot(String... value) {
        addNotEquals(ChangerateColumns.ASK, value);
        return this;
    }

    public ChangerateSelection askLike(String... value) {
        addLike(ChangerateColumns.ASK, value);
        return this;
    }

    public ChangerateSelection bid(String... value) {
        addEquals(ChangerateColumns.BID, value);
        return this;
    }

    public ChangerateSelection bidNot(String... value) {
        addNotEquals(ChangerateColumns.BID, value);
        return this;
    }

    public ChangerateSelection bidLike(String... value) {
        addLike(ChangerateColumns.BID, value);
        return this;
    }
}
