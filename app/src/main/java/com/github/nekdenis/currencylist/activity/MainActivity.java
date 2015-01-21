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
package com.github.nekdenis.currencylist.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.ListView;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.fragment.CurrenciesFragment;
import com.github.nekdenis.currencylist.fragment.ExchangeDetailFragment;
import com.github.nekdenis.currencylist.sync.CurrenciesSyncAdapter;


public class MainActivity extends ActionBarActivity implements CurrenciesFragment.OnItemSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private boolean twoPane = false;
    CurrenciesFragment currenciesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExchangeDetailFragment exchangeDetailFragment;
        if (findViewById(R.id.exchange_detail_container) != null) {
            twoPane = true;
            if (savedInstanceState == null) {
                exchangeDetailFragment = ExchangeDetailFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.exchange_detail_container, exchangeDetailFragment)
                        .commit();
            }
        } else {
            twoPane = false;
        }

        currenciesFragment = ((CurrenciesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_currencies));
        if (currenciesFragment != null) {
            currenciesFragment.setUseTodayLayout(!twoPane);
            if (currenciesFragment.getSelectedPosition() == ListView.INVALID_POSITION) {
                currenciesFragment.getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (twoPane) {
                            String itemPath = currenciesFragment.getFirstItemPath();
                            if (!TextUtils.isEmpty(itemPath)) {
                                onItemSelected(itemPath);
                            }
                        }
                    }
                }, 200);
            }
        }

        initSync();
    }

    private void initSync() {
        CurrenciesSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public void onItemSelected(String exchangePath) {
        if (twoPane) {
            ExchangeDetailFragment fragment = ExchangeDetailFragment.newInstance(exchangePath);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.exchange_detail_container, fragment)
                    .commit();
        } else {
            DetailActivity.startActivity(this, exchangePath);
        }
    }
}