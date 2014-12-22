package com.github.nekdenis.currencylist.db.provider.currencies;

import android.net.Uri;
import android.provider.BaseColumns;

import com.github.nekdenis.currencylist.db.provider.Provider;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesColumns;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueColumns;

/**
 * Columns for the {@code currencies} table.
 */
public class CurrenciesColumns implements BaseColumns {
    public static final String TABLE_NAME = "currencies";
    public static final Uri CONTENT_URI = Uri.parse(Provider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = new String(BaseColumns._ID);

    public static final String PATH = "path";

    public static final String NAME = "name";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            PATH,
            NAME
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c == PATH || c.contains("." + PATH)) return true;
            if (c == NAME || c.contains("." + NAME)) return true;
        }
        return false;
    }

}
