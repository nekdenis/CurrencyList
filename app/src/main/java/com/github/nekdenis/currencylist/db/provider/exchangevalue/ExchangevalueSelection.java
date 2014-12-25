package com.github.nekdenis.currencylist.db.provider.exchangevalue;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.github.nekdenis.currencylist.db.provider.base.AbstractSelection;

/**
 * Selection for the {@code exchangevalue} table.
 */
public class ExchangevalueSelection extends AbstractSelection<ExchangevalueSelection> {
    @Override
    public Uri uri() {
        return ExchangevalueColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code ExchangevalueCursor} object, which is positioned before the first entry, or null.
     */
    public ExchangevalueCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new ExchangevalueCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public ExchangevalueCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public ExchangevalueCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public ExchangevalueSelection id(long... value) {
        addEquals("exchangevalue." + ExchangevalueColumns._ID, toObjectArray(value));
        return this;
    }


    public ExchangevalueSelection pathval(String... value) {
        addEquals(ExchangevalueColumns.PATHVAL, value);
        return this;
    }

    public ExchangevalueSelection pathvalNot(String... value) {
        addNotEquals(ExchangevalueColumns.PATHVAL, value);
        return this;
    }

    public ExchangevalueSelection pathvalLike(String... value) {
        addLike(ExchangevalueColumns.PATHVAL, value);
        return this;
    }

    public ExchangevalueSelection title(String... value) {
        addEquals(ExchangevalueColumns.TITLE, value);
        return this;
    }

    public ExchangevalueSelection titleNot(String... value) {
        addNotEquals(ExchangevalueColumns.TITLE, value);
        return this;
    }

    public ExchangevalueSelection titleLike(String... value) {
        addLike(ExchangevalueColumns.TITLE, value);
        return this;
    }

    public ExchangevalueSelection rate(String... value) {
        addEquals(ExchangevalueColumns.RATE, value);
        return this;
    }

    public ExchangevalueSelection rateNot(String... value) {
        addNotEquals(ExchangevalueColumns.RATE, value);
        return this;
    }

    public ExchangevalueSelection rateLike(String... value) {
        addLike(ExchangevalueColumns.RATE, value);
        return this;
    }

    public ExchangevalueSelection date(long... value) {
        addEquals(ExchangevalueColumns.DATE, toObjectArray(value));
        return this;
    }

    public ExchangevalueSelection dateNot(long... value) {
        addNotEquals(ExchangevalueColumns.DATE, toObjectArray(value));
        return this;
    }

    public ExchangevalueSelection dateGt(long value) {
        addGreaterThan(ExchangevalueColumns.DATE, value);
        return this;
    }

    public ExchangevalueSelection dateGtEq(long value) {
        addGreaterThanOrEquals(ExchangevalueColumns.DATE, value);
        return this;
    }

    public ExchangevalueSelection dateLt(long value) {
        addLessThan(ExchangevalueColumns.DATE, value);
        return this;
    }

    public ExchangevalueSelection dateLtEq(long value) {
        addLessThanOrEquals(ExchangevalueColumns.DATE, value);
        return this;
    }

    public ExchangevalueSelection datehours(long... value) {
        addEquals(ExchangevalueColumns.DATEHOURS, toObjectArray(value));
        return this;
    }

    public ExchangevalueSelection datehoursNot(long... value) {
        addNotEquals(ExchangevalueColumns.DATEHOURS, toObjectArray(value));
        return this;
    }

    public ExchangevalueSelection datehoursGt(long value) {
        addGreaterThan(ExchangevalueColumns.DATEHOURS, value);
        return this;
    }

    public ExchangevalueSelection datehoursGtEq(long value) {
        addGreaterThanOrEquals(ExchangevalueColumns.DATEHOURS, value);
        return this;
    }

    public ExchangevalueSelection datehoursLt(long value) {
        addLessThan(ExchangevalueColumns.DATEHOURS, value);
        return this;
    }

    public ExchangevalueSelection datehoursLtEq(long value) {
        addLessThanOrEquals(ExchangevalueColumns.DATEHOURS, value);
        return this;
    }

    public ExchangevalueSelection ask(String... value) {
        addEquals(ExchangevalueColumns.ASK, value);
        return this;
    }

    public ExchangevalueSelection askNot(String... value) {
        addNotEquals(ExchangevalueColumns.ASK, value);
        return this;
    }

    public ExchangevalueSelection askLike(String... value) {
        addLike(ExchangevalueColumns.ASK, value);
        return this;
    }

    public ExchangevalueSelection bid(String... value) {
        addEquals(ExchangevalueColumns.BID, value);
        return this;
    }

    public ExchangevalueSelection bidNot(String... value) {
        addNotEquals(ExchangevalueColumns.BID, value);
        return this;
    }

    public ExchangevalueSelection bidLike(String... value) {
        addLike(ExchangevalueColumns.BID, value);
        return this;
    }
}
