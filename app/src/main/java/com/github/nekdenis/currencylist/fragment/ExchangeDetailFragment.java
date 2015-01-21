package com.github.nekdenis.currencylist.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.activity.DetailActivity;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesSelection;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueColumns;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueCursor;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueSelection;
import com.github.nekdenis.currencylist.util.Constants;
import com.github.nekdenis.currencylist.view.ColumnGraph;

import java.util.ArrayList;
import java.util.List;

public class ExchangeDetailFragment extends Fragment {

    private static final String TAG = ExchangeDetailFragment.class.getSimpleName();
    private static final String EXTRA_EXCHANGE_PATH = "EXTRA_EXCHANGE_PATH";

    private TextView currencyName;
    private TextView currencyCurrentValue;
    private View emptyView;
    private ColumnGraph graphView;

    private String exchangePath = "";
    private MenuItem shareMenuItem;
    private ShareActionProvider shareActionProvider;

    public static ExchangeDetailFragment newInstance(String exchangePath) {
        ExchangeDetailFragment fragment = new ExchangeDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_EXCHANGE_PATH, exchangePath);
        fragment.setArguments(args);
        return fragment;
    }

    public static ExchangeDetailFragment newInstance() {
        ExchangeDetailFragment fragment = new ExchangeDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        setHasOptionsMenu(true);
        if (args != null) {
            exchangePath = args.getString(EXTRA_EXCHANGE_PATH);
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
        graphView = (ColumnGraph) rootView.findViewById(R.id.exchange_detail_graph);
        emptyView = rootView.findViewById(R.id.exchange_detail_container);
        if (TextUtils.isEmpty(exchangePath)) {
            if (emptyView != null) {
                emptyView.setVisibility(View.VISIBLE);
            }
            currencyName.setVisibility(View.GONE);
            currencyCurrentValue.setVisibility(View.GONE);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        shareMenuItem = menu.findItem(R.id.action_share);
        shareMenuItem.setVisible(false);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            showDeleteDialog();
            return true;
        }
        return false;
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(getResources().getString(R.string.details_remove_currency_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteCurrency();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.details_remove_currency_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setMessage(getResources().getString(R.string.details_remove_currency_title));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteCurrency() {
        CurrenciesSelection currenciesSelection = new CurrenciesSelection();
        currenciesSelection.path(exchangePath);
        currenciesSelection.delete(getActivity().getContentResolver());
        if (getActivity() instanceof DetailActivity) {
            getActivity().finish();
        }
    }

    private Intent createShareForecastIntent(ExchangevalueCursor data) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_rate_string, data.getTitle(), data.getRate()));
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(Constants.LOADER_ID_DETAIL, null, changeRateCallback);
    }

    private void fillData(ExchangevalueCursor data) {
        currencyName.setText(data.getTitle());
        currencyCurrentValue.setText(getString(R.string.exchange_current_label, data.getRate()));
        if (shareActionProvider != null) {
            shareMenuItem.setVisible(true);
            shareActionProvider.setShareIntent(createShareForecastIntent(data));
        }
        List<Float> rateValues = new ArrayList<Float>();
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            rateValues.add(Float.parseFloat(data.getRate()));
        }
        graphView.setChartData(rateValues);
    }

    LoaderManager.LoaderCallbacks<Cursor> changeRateCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            ExchangevalueSelection selection = new ExchangevalueSelection();
            selection.pathval(exchangePath);
            return new CursorLoader(
                    getActivity(),
                    ExchangevalueColumns.CONTENT_URI,
                    null,
                    selection.sel(),
                    selection.args(),
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.moveToFirst()) {
                ExchangevalueCursor exchangevalueCursor = new ExchangevalueCursor(data);
                fillData(exchangevalueCursor);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };


}
