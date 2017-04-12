package pl.komunikator.komunikator;

import android.app.Application;
import android.util.Log;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by Tenek on 11.04.2017.
 */

public class KomunikatorApplication extends Application {
    public static String REALM_URL = "realm://81.2.250.203:9080/~/komunikator";
    public static String authURL = "http://81.2.250.203:9080/auth";

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
