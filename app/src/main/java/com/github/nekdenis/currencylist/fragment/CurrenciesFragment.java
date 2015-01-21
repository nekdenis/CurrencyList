/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nekdenis.currencylist.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.adapter.RateAdapter;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesColumns;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesContentValues;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesCursor;
import com.github.nekdenis.currencylist.fragment.dialog.AddCurrencyDialog;
import com.github.nekdenis.currencylist.sync.CurrenciesSyncAdapter;
import com.github.nekdenis.currencylist.util.Constants;

public class CurrenciesFragment extends Fragment {

    public static final String TAG = CurrenciesFragment.class.getSimpleName();

    private static final String SELECTED_KEY = "SELECTED_KEY";
    private static final int MSG_SHOW_DIALOG = 23;
    public static final String ADD_CURRENCY_DIALOG = "AddCurrencyDialog";

    private RateAdapter rateAdapter;

    private ListView listView;
    private int selectedPosition = ListView.INVALID_POSITION;
    private boolean useTodayLayout;

    public CurrenciesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            selectedPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        setHasOptionsMenu(true);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_currencies, container, false);
        initList(rootView);

        rateAdapter.setUseTodayLayout(useTodayLayout);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            showAddCurrencyDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddCurrencyDialog() {
        if (getFragmentManager().findFragmentByTag(ADD_CURRENCY_DIALOG) == null) {
            AddCurrencyDialog.newInstance(onAddCurencyClickListener).show(getActivity().getSupportFragmentManager(), ADD_CURRENCY_DIALOG);
        }
    }

    private void initList(View rootView) {
        rateAdapter = new RateAdapter(getActivity(), null, 0);
        listView = (ListView) rootView.findViewById(R.id.currencies_list);
        listView.setAdapter(rateAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CurrenciesCursor currenciesCursor = new CurrenciesCursor(rateAdapter.getCursor());
                if (currenciesCursor.moveToPosition(position)) {
                    ((OnItemSelectedListener) getActivity())
                            .onItemSelected(currenciesCursor.getPath());
                }
                selectedPosition = position;
                listView.setItemChecked(position, true);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(Constants.LOADER_ID_RATES, null, ratesCallback);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(Constants.LOADER_ID_RATES, null, ratesCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (selectedPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, selectedPosition);
        }
        super.onSaveInstanceState(outState);
    }

    private void updateRates() {
        CurrenciesSyncAdapter.syncImmediately(getActivity());
    }

    LoaderCallbacks<Cursor> ratesCallback = new LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(
                    getActivity(),
                    CurrenciesColumns.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            rateAdapter.swapCursor(data);
            if (selectedPosition != ListView.INVALID_POSITION) {
                listView.smoothScrollToPosition(selectedPosition);
                listView.setItemChecked(selectedPosition, true);
            }
            if (rateAdapter.getCount() == 0) {
                handler.sendEmptyMessage(MSG_SHOW_DIALOG);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            rateAdapter.swapCursor(null);
        }
    };

    public void setUseTodayLayout(boolean useTodayLayout) {
        this.useTodayLayout = useTodayLayout;
        if (rateAdapter != null) {
            rateAdapter.setUseTodayLayout(useTodayLayout);
        }
    }

    public interface OnItemSelectedListener {
        public void onItemSelected(String exchangePath);
    }

    public String getFirstItemPath() {
        if (rateAdapter.getCount() > 0) {
            CurrenciesCursor currenciesCursor = new CurrenciesCursor((Cursor) rateAdapter.getItem(0));
            return currenciesCursor.getPath();
        }
        return "";
    }

    private AddCurrencyDialog.OnDialogClickListener onAddCurencyClickListener = new AddCurrencyDialog.OnDialogClickListener() {
        @Override
        public void onAddClick(String from, String to, Dialog dialog) {
            addCurrency(from, to, dialog);
        }
    };

    private void addCurrency(String from, String to, Dialog dialog) {
        CurrenciesContentValues currenciesContentValues = new CurrenciesContentValues();
        currenciesContentValues.putPath(from + to);
        currenciesContentValues.putName(getString(R.string.add_currency_name, from, to));
        currenciesContentValues.insert(getActivity().getContentResolver());
        reSync();
        dialog.dismiss();
    }

    private void reSync() {
        CurrenciesSyncAdapter.syncImmediately(getActivity());
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SHOW_DIALOG) {
                showAddCurrencyDialog();
            }
        }
    };
}
