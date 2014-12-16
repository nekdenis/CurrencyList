package com.github.nekdenis.currencylist.db.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.github.nekdenis.currencylist.BuildConfig;
import com.github.nekdenis.currencylist.db.provider.changerate.ChangerateColumns;

public class SQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "currencylist.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteOpenHelper sInstance;
    private final Context mContext;
    private final SQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    private static final String SQL_CREATE_TABLE_CHANGERATE = "CREATE TABLE IF NOT EXISTS "
            + ChangerateColumns.TABLE_NAME + " ( "
            + ChangerateColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ChangerateColumns.PATH + " TEXT NOT NULL, "
            + ChangerateColumns.NAME + " TEXT NOT NULL, "
            + ChangerateColumns.RATE + " TEXT NOT NULL, "
            + ChangerateColumns.DATE + " TEXT NOT NULL, "
            + ChangerateColumns.TIME + " TEXT NOT NULL, "
            + ChangerateColumns.ASK + " TEXT NOT NULL, "
            + ChangerateColumns.BID + " TEXT NOT NULL "
            + ", CONSTRAINT unique_time UNIQUE (name, date, time) ON CONFLICT REPLACE"
            + " );";

    // @formatter:on

    public static SQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static SQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */

    private static SQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new SQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    private SQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        mOpenHelperCallbacks = new SQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static SQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new SQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private SQLiteOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new SQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_CHANGERATE);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
