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
import android.text.TextUtils;
import android.util.Log;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesColumns;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesContentValues;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesCursor;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesSelection;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueColumns;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueContentValues;
import com.github.nekdenis.currencylist.util.NotificationUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CurrenciesSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String TAG = CurrenciesSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL_MINUTES = 60;
    public static final int SYNC_INTERVAL = 60 * SYNC_INTERVAL_MINUTES;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 2 - 1;
    private static final int CURRENCY_NOTIFICATION_ID = 3004;
    public static final String WEBSERVICE_PREFIX = "https://query.yahooapis.com/v1/public/yql?";

    public CurrenciesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "Starting sync");

        String currenciesString = getCurrenciesString();
        if (!TextUtils.isEmpty(currenciesString)) {
            String responseString = makeRequest(currenciesString);
            try {
                List<ExchangevalueContentValues> exchangevalueContentValues = new ArrayList<ExchangevalueContentValues>();
                parseResponse(responseString, exchangevalueContentValues);
                if (exchangevalueContentValues.size() > 0) {
                    saveResponse(exchangevalueContentValues);
                    notifyUser();
                }
                Log.d(TAG, "Updated: " + exchangevalueContentValues.size() + " rates inserted");
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    private String makeRequest(String currenciesString) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        String requestParam = "select+*+from+yahoo.finance.xchange+where+pair+=+\"USDRUB,EURRUB,RUBRUB,RUBEUR\"";
        params.add(new BasicNameValuePair("q", requestParam));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("env", "store://datatables.org/alltableswithkeys"));
        String paramString = URLEncodedUtils.format(params, "utf-8");
        OkHttpClient client = new OkHttpClient();

//        String url = WEBSERVICE_PREFIX + paramString;

        String url = "https://query.yahooapis.com/v1/public/yql?q=select+*+from+yahoo.finance.xchange+where+pair+=+%22" + currenciesString + "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        Log.d(TAG, "request = " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        String responseString = "";
        try {
            Response response = client.newCall(request).execute();
            responseString = response.body().string();
            Log.d(TAG, "response = " + responseString);
            return responseString;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }

    private void parseResponse(String responseString, List<ExchangevalueContentValues> exchangevalueContentValues) throws JSONException {
        JSONObject responseJson = new JSONObject(responseString);
        JSONObject queryJson = responseJson.optJSONObject("query");
        JSONObject resultsJson = queryJson.optJSONObject("results");
        Object json = new JSONTokener(resultsJson.getString("rate")).nextValue();
        if (json instanceof JSONObject) {
            JSONObject rate = (JSONObject) json;
            ExchangevalueContentValues contentValues = parseJson(rate);
            exchangevalueContentValues.add(contentValues);
        } else if (json instanceof JSONArray) {
            JSONArray rateArray = (JSONArray) json;
            for (int i = 0; i < rateArray.length(); i++) {
                JSONObject rate = rateArray.getJSONObject(i);
                ExchangevalueContentValues contentValues = parseJson(rate);
                exchangevalueContentValues.add(contentValues);
            }
        }
    }


    private void saveResponse(List<ExchangevalueContentValues> exchangevalueContentValues) {
        List<ContentValues> contentValuesList = new ArrayList<ContentValues>(exchangevalueContentValues.size());
        for (ExchangevalueContentValues contentValues : exchangevalueContentValues) {
            contentValuesList.add(contentValues.values());
        }
        ContentValues[] valuesArray = contentValuesList.toArray(new ContentValues[contentValuesList.size()]);
        getContext().getContentResolver().bulkInsert(ExchangevalueColumns.CONTENT_URI, valuesArray);
    }


    private ExchangevalueContentValues parseJson(JSONObject rate) {
        ExchangevalueContentValues contentValues = new ExchangevalueContentValues();
        String pathVal = rate.optString("id");
        contentValues.putPathval(pathVal);
        contentValues.putTitle(rate.optString("Name"));
        String rateValue = rate.optString("Rate");
        contentValues.putRate(rateValue);
        parseDate(rate, contentValues);
        contentValues.putAsk(rate.optString("Ask"));
        contentValues.putBid(rate.optString("Bid"));
        updateLastRate(pathVal, rateValue);
        return contentValues;
    }

    private void parseDate(JSONObject rate, ExchangevalueContentValues contentValues) {
        //"Date":"12/25/2014","Time":"1:57am"
        SimpleDateFormat dateParser = new SimpleDateFormat("MM/dd/yyyy KK:mmaa Z", Locale.US);
        SimpleDateFormat timeParser = new SimpleDateFormat("");
        String dateString = rate.optString("Date");
        String timeString = rate.optString("Time");
        try {
            Date date = dateParser.parse(dateString + " " + timeString + " -0500");
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            contentValues.putDate(c.getTimeInMillis());
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MINUTE, 0);
            contentValues.putDatehours(c.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateLastRate(String currencyName, String value) {
        CurrenciesSelection currenciesSelection = new CurrenciesSelection().path(currencyName);
        CurrenciesCursor currenciesCursor = new CurrenciesCursor(currenciesSelection.query(getContext().getContentResolver()));
        if (currenciesCursor.moveToNext()) {
            CurrenciesContentValues currenciesContentValues = new CurrenciesContentValues();
            currenciesContentValues.putPath(currenciesCursor.getPath());
            currenciesContentValues.putName(currenciesCursor.getName());
            currenciesContentValues.putLastrate(value);
            currenciesContentValues.update(getContext().getContentResolver(), new CurrenciesSelection().path(currencyName));
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
        NotificationUtil.showNewUpdateNotification(getContext(), getContext().getString(R.string.notification_text));
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
