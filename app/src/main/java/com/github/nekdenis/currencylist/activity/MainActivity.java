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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.fragment.ExchangeDetailFragment;
import com.github.nekdenis.currencylist.fragment.RatesFragment;
import com.github.nekdenis.currencylist.sync.CurrenciesSyncAdapter;


public class MainActivity extends ActionBarActivity implements RatesFragment.OnItemSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.rates_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rates_detail_container, new ExchangeDetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        RatesFragment forecastFragment =  ((RatesFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_rates));
//        forecastFragment.setUseTodayLayout(!mTwoPane);

        CurrenciesSyncAdapter.initializeSyncAdapter(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String date) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putString(DetailActivity.DATE_KEY, date);

            ExchangeDetailFragment fragment = new ExchangeDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rates_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailActivity.DATE_KEY, date);
            startActivity(intent);
        }
    }
}
