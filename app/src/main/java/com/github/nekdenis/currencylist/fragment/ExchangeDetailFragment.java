package com.github.nekdenis.currencylist.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueColumns;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueCursor;
import com.github.nekdenis.currencylist.util.Constants;

public class ExchangeDetailFragment extends Fragment {

    private static final String TAG = ExchangeDetailFragment.class.getSimpleName();
    private static final String EXTRA_EXCHANGE_ID = "EXTRA_EXCHANGE_ID";

    private TextView currencyName;
    private TextView currencyCurrentValue;

    private String exchangeId;
    private ShareActionProvider shareActionProvider;

    public static ExchangeDetailFragment newInstance(String exchangeId) {
        ExchangeDetailFragment fragment = new ExchangeDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_EXCHANGE_ID, exchangeId);
        fragment.setArguments(args);
        return fragment;
    }

    public ExchangeDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            exchangeId = args.getString(EXTRA_EXCHANGE_ID);
        }
        if (savedInstanceState != null) {
            //TODO:
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //TODO:
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exchange_detail, container, false);
        currencyName = (TextView) rootView.findViewById(R.id.exchange_detail_name);
        currencyCurrentValue = (TextView) rootView.findViewById(R.id.exchange_detail_current);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//        if (mForecast != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
//            mLocation = savedInstanceState.getString(LOCATION_KEY);
        }
        getLoaderManager().initLoader(Constants.LOADER_ID_DETAIL, null, changeRateCallback);
    }

    LoaderManager.LoaderCallbacks<Cursor> changeRateCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(
                    getActivity(),
                    ExchangevalueColumns.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.moveToFirst()) {
                ExchangevalueCursor exchangevalueCursor = new ExchangevalueCursor(data);
                currencyName.setText(exchangevalueCursor.getTitle());
                currencyCurrentValue.setText(exchangevalueCursor.getRate());
                if (data != null && data.moveToFirst()) {
                    if (shareActionProvider != null) {
                        shareActionProvider.setShareIntent(createShareForecastIntent());
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };
}
