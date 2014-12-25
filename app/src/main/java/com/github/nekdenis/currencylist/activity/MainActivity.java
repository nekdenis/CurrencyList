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

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.db.provider.currencies.CurrenciesContentValues;
import com.github.nekdenis.currencylist.fragment.CurrenciesFragment;
import com.github.nekdenis.currencylist.fragment.ExchangeDetailFragment;
import com.github.nekdenis.currencylist.fragment.dialog.AddCurrencyDialog;
import com.github.nekdenis.currencylist.sync.CurrenciesSyncAdapter;


public class MainActivity extends ActionBarActivity implements CurrenciesFragment.OnItemSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private boolean twoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.exchange_detail_container) != null) {
            twoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.exchange_detail_container, ExchangeDetailFragment.newInstance())
                        .commit();
            }
        } else {
            twoPane = false;
        }

        CurrenciesFragment currenciesFragment = ((CurrenciesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_currencies));
        if (currenciesFragment != null) {
            currenciesFragment.setUseTodayLayout(!twoPane);
        }

        initSync();

    }

    private void initSync() {
        CurrenciesSyncAdapter.initializeSyncAdapter(this);
    }

    private void reSync() {
        CurrenciesSyncAdapter.syncImmediately(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            AddCurrencyDialog.newInstance(onAddCurencyClickListener).show(getSupportFragmentManager(), "AddCurrencyDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private AddCurrencyDialog.OnDialogClickListener onAddCurencyClickListener = new AddCurrencyDialog.OnDialogClickListener() {
        @Override
        public void onAddClick(String from, String to, Dialog dialog) {
            CurrenciesContentValues currenciesContentValues = new CurrenciesContentValues();
            currenciesContentValues.putPath(from + to);
            currenciesContentValues.putName(getString(R.string.add_currency_name, from, to));
            currenciesContentValues.insert(getContentResolver());
            reSync();
        }
    };
}
