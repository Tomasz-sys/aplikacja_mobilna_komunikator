package pl.komunikator.komunikator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class StartActivity extends AppCompatActivity {
    public static String REALM_URL = "realm://81.2.250.203:9080/~/komunikator";
    private static String authURL = "http://81.2.250.203:9080/auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Realm.init(this);
        SyncUser user = SyncUser.currentUser();
        Log.e("user", String.valueOf(user == null));
        if (user == null) {
            SyncCredentials syncCredentials = SyncCredentials.google("530614858352-f0g0cqkkpoui9926nki1dsjl8b300t90.apps.googleusercontent.com");
            Log.e("StartAcitivuty", "before loginAsync user = null");
            SyncUser.loginAsync(syncCredentials, authURL, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    Log.e("Suc", "suc");
                }

                @Override
                public void onError(ObjectServerError error) {
                    Log.e("Erro", error.getMessage());
                }
            });
        }
    }
}
