package com.github.nekdenis.currencylist.db.provider.names;

import android.net.Uri;
import android.provider.BaseColumns;

import com.github.nekdenis.currencylist.db.provider.Provider;
import com.github.nekdenis.currencylist.db.provider.changerate.ChangerateColumns;
import com.github.nekdenis.currencylist.db.provider.names.NamesColumns;

/**
 * Columns for the {@code names} table.
 */
public class NamesColumns implements BaseColumns {
    public static final String TABLE_NAME = "names";
    public static final Uri CONTENT_URI = Uri.parse(Provider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = new String(BaseColumns._ID);

    public static final String NAMES = "names__names";

    public static final String NAME = "name";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            NAMES,
            NAME
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c == NAMES || c.contains("." + NAMES)) return true;
            if (c == NAME || c.contains("." + NAME)) return true;
        }
        return false;
    }

}
