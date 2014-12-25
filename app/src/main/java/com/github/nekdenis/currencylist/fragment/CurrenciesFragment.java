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

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.adapter.RateAdapter;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueColumns;
import com.github.nekdenis.currencylist.db.provider.exchangevalue.ExchangevalueCursor;
import com.github.nekdenis.currencylist.sync.CurrenciesSyncAdapter;
import com.github.nekdenis.currencylist.util.Constants;

public class CurrenciesFragment extends Fragment {

    public static final String TAG = CurrenciesFragment.class.getSimpleName();

    private RateAdapter rateAdapter;

    private ListView listView;
    private int mPosition = ListView.INVALID_POSITION;
    private boolean useTodayLayout;

    public CurrenciesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_currencies, container, false);
        initList(rootView);
//        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
//            mPosition = savedInstanceState.getInt(SELECTED_KEY);
//        }

        rateAdapter.setUseTodayLayout(useTodayLayout);

        return rootView;
    }

    private void initList(View rootView) {
        rateAdapter = new RateAdapter(getActivity(), null, 0);
        listView = (ListView) rootView.findViewById(R.id.currencies_list);
        listView.setAdapter(rateAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ExchangevalueCursor exchangevalueCursor = new ExchangevalueCursor(rateAdapter.getCursor());
                if (exchangevalueCursor.moveToPosition(position)) {
                    ((OnItemSelectedListener) getActivity())
                            .onItemSelected(exchangevalueCursor.getPathval());
                }
                mPosition = position;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(Constants.LOADER_ID_RATES, null, ratesCallback);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateRates() {
        CurrenciesSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(Constants.LOADER_ID_RATES, null, ratesCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
//            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    LoaderCallbacks<Cursor> ratesCallback = new LoaderCallbacks<Cursor>() {
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
            rateAdapter.swapCursor(data);
            if (mPosition != ListView.INVALID_POSITION) {
                listView.smoothScrollToPosition(mPosition);
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
}
