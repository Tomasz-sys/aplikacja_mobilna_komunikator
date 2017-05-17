package pl.komunikator.komunikator;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Tenek on 11.05.2017.
 */

public class KomunikatorApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
