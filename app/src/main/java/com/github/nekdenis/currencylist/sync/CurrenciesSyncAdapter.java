package com.github.nekdenis.currencylist.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesColumns;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesCursor;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueColumns;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueContentValues;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CurrenciesSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String TAG = CurrenciesSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL_MINUTES = 180;
    public static final int SYNC_INTERVAL = 60 * SYNC_INTERVAL_MINUTES;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int CURRENCY_NOTIFICATION_ID = 3004;
    public static final String WEBSERVICE_PREFIX = "https://query.yahooapis.com/v1/public/yql?";

    public CurrenciesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Starting sync");
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        String requestParam = "select+*+from+yahoo.finance.xchange+where+pair+=+\"USDRUB,EURRUB,RUBRUB,RUBEUR\"";
        params.add(new BasicNameValuePair("q", requestParam));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("env", "store://datatables.org/alltableswithkeys"));
        String paramString = URLEncodedUtils.format(params, "utf-8");
        OkHttpClient client = new OkHttpClient();

//        String url = WEBSERVICE_PREFIX + paramString;
        String url = "https://query.yahooapis.com/v1/public/yql?q=select+*+from+yahoo.finance.xchange+where+pair+=+%22" + getCurrenciesString() + "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        Log.d(TAG, "request = " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        String responseString = "";
        try {
            Response response = client.newCall(request).execute();
            responseString = response.body().string();
            Log.d(TAG, "response = " + responseString);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            JSONObject responseJson = new JSONObject(responseString);
            JSONObject queryJson = responseJson.optJSONObject("query");
            JSONObject resultsJson = queryJson.optJSONObject("results");
            JSONArray rateArray = resultsJson.getJSONArray("rate");

            List<ExchangevalueContentValues> exchangevalueContentValues = new ArrayList<ExchangevalueContentValues>(rateArray.length());

            for (int i = 0; i < rateArray.length(); i++) {
                JSONObject rate = rateArray.getJSONObject(i);

                ExchangevalueContentValues contentValues = new ExchangevalueContentValues();
                contentValues.putPathval(rate.optString("id"));
                contentValues.putTitle(rate.optString("Name"));
                contentValues.putRate(rate.optString("Rate"));
                parseDate(rate, contentValues);
                contentValues.putAsk(rate.optString("Ask"));
                contentValues.putBid(rate.optString("Bid"));

                exchangevalueContentValues.add(contentValues);
            }
            if (exchangevalueContentValues.size() > 0) {
                List<ContentValues> contentValuesList = new ArrayList<ContentValues>(exchangevalueContentValues.size());
                for (ExchangevalueContentValues contentValues : exchangevalueContentValues) {
                    contentValuesList.add(contentValues.values());
                }
                ContentValues[] valuesArray = contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
                getContext().getContentResolver().bulkInsert(ExchangevalueColumns.CONTENT_URI, valuesArray);

                notifyUser();
            }
            Log.d(TAG, "Updated: " + exchangevalueContentValues.size() + " rates inserted");

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void parseDate(JSONObject rate, ExchangevalueContentValues contentValues) {
        //"Date":"12/25/2014","Time":"1:57am"
        SimpleDateFormat dateParser = new SimpleDateFormat("MM/dd/yyyy KK:mmaa Z");
        SimpleDateFormat timeParser = new SimpleDateFormat("");
        String dateString = rate.optString("Date");
        String timeString = rate.optString("Time");
        try {
            Date date = dateParser.parse(dateString + " " + timeString+" -0500");
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, 0);
            c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String getCurrenciesString() {
        StringBuilder stringBuilder = new StringBuilder();
        CurrenciesCursor currenciesCursor = new CurrenciesCursor(getContext().getContentResolver().query(CurrenciesColumns.CONTENT_URI, null, null, null, null));
        for (currenciesCursor.moveToFirst(); !currenciesCursor.isAfterLast(); currenciesCursor.moveToNext()) {
            stringBuilder.append(currenciesCursor.getPath());
            if (!currenciesCursor.isLast()) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    private void notifyUser() {
//        Context context = getContext();
//        //checking the last update and notify if it' the first of the day
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
//        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
//                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));
//
//        if (displayNotifications) {
//
//            String lastNotificationKey = context.getString(R.string.pref_last_notification);
//            long lastSync = prefs.getLong(lastNotificationKey, 0);
//
//            if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {
//                // Last sync was more than 1 day ago, let's send a notification with the weather.
//                String locationQuery = Utility.getPreferredLocation(context);
//
//                Uri weatherUri = WeatherEntry.buildWeatherLocationWithDate(locationQuery, WeatherContract.getDbDateString(new Date()));
//
//                // we'll query our contentProvider, as always
//                Cursor cursor = context.getContentResolver().query(weatherUri, NOTIFY_WEATHER_PROJECTION, null, null, null);
//
//                if (cursor.moveToFirst()) {
//                    int weatherId = cursor.getInt(INDEX_WEATHER_ID);
//                    double high = cursor.getDouble(INDEX_MAX_TEMP);
//                    double low = cursor.getDouble(INDEX_MIN_TEMP);
//                    String desc = cursor.getString(INDEX_SHORT_DESC);
//
//                    int iconId = Utility.getIconResourceForWeatherCondition(weatherId);
//                    String title = context.getString(R.string.app_name);
//
//                    // Define the text of the forecast.
//                    String contentText = String.format(context.getString(R.string.format_notification),
//                            desc,
//                            Utility.formatTemperature(context, high),
//                            Utility.formatTemperature(context, low));
//
//                    // NotificationCompatBuilder is a very convenient way to build backward-compatible
//                    // notifications.  Just throw in some data.
//                    NotificationCompat.Builder mBuilder =
//                            new NotificationCompat.Builder(getContext())
//                                    .setSmallIcon(iconId)
//                                    .setContentTitle(title)
//                                    .setContentText(contentText);
//
//                    // Make something interesting happen when the user clicks on the notification.
//                    // In this case, opening the app is sufficient.
//                    Intent resultIntent = new Intent(context, MainActivity.class);
//
//                    // The stack builder object will contain an artificial back stack for the
//                    // started Activity.
//                    // This ensures that navigating backward from the Activity leads out of
//                    // your application to the Home screen.
//                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//                    stackBuilder.addNextIntent(resultIntent);
//                    PendingIntent resultPendingIntent =
//                            stackBuilder.getPendingIntent(
//                                    0,
//                                    PendingIntent.FLAG_UPDATE_CURRENT
//                            );
//                    mBuilder.setContentIntent(resultPendingIntent);
//
//                    NotificationManager mNotificationManager =
//                            (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                    // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
//                    mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());
//
//
//                    //refreshing last sync
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putLong(lastNotificationKey, System.currentTimeMillis());
//                    editor.commit();
//                }
//            }
//        }

    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setExtras(new Bundle()).
                    setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        CurrenciesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


}
