package midas.coinmarket.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AuthenticationService extends Service {
    public AuthenticationService() {
        // TODO Auto-generated constructor stub
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AppAuthentication(this).getIBinder();
    }
}
