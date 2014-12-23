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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.fragment.ExchangeDetailFragment;

public class DetailActivity extends ActionBarActivity {

    private static final String EXTRA_EXCHANGE_PATH = "EXTRA_EXCHANGE_PATH";

    public static final void startActivity(Context context, String exchangePath) {
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra(EXTRA_EXCHANGE_PATH, exchangePath);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            String exchangePath = getIntent().getStringExtra(EXTRA_EXCHANGE_PATH);
            ExchangeDetailFragment fragment = ExchangeDetailFragment.newInstance(exchangePath);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.exchange_detail_container, fragment)
                    .commit();
        }
    }
}
