package pl.komunikator.komunikator;

import android.app.Application;
import android.app.ProgressDialog;
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
    private SyncUser user;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        user = SyncUser.currentUser();
        if (user == null) {
            final ProgressDialog progressDialog = new ProgressDialog(KomunikatorApplication.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
            SyncCredentials syncCredentials = SyncCredentials.usernamePassword("user", "user", false);
            Log.e("LoginActivity", syncCredentials.getUserIdentifier());

            SyncUser.Callback callback = new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    progressDialog.dismiss();
                    onLoginSuccess(user);
                }

                @Override
                public void onError(ObjectServerError error) {
                    progressDialog.dismiss();
                    String errorMsg;
                    switch (error.getErrorCode()) {
                        case UNKNOWN_ACCOUNT:
                            errorMsg = "Account does not exists.";
                            break;
                        case INVALID_CREDENTIALS:
                            errorMsg = "User name and password does not match";
                            break;
                        default:
                            errorMsg = error.toString();
                    }
                    onLoginFailed(errorMsg);
                }
            };

            SyncUser.loginAsync(syncCredentials, KomunikatorApplication.authURL, callback);

        }
    }

    private void onLoginSuccess(SyncUser user) {
        this.user = user;
    }

    private void onLoginFailed(String errorMsg) {
        Log.e("KomunikatorApplication", errorMsg);
    }
}
