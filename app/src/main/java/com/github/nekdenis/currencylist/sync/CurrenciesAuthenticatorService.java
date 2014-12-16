package com.github.nekdenis.currencylist.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CurrenciesAuthenticatorService extends Service {
    private CurrenciesAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new CurrenciesAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
