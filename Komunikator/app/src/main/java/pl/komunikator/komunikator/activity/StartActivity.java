package pl.komunikator.komunikator.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import pl.komunikator.komunikator.R;

public class StartActivity extends AppCompatActivity {

    public static String REALM_URL = "realm://81.2.250.203:9080/~/komunikator";
    public static String AUTH_URL = "http://81.2.250.203:9080/auth";
    public static SyncUser user = null;
    private static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this
                        , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        initRealm();
    }

    private void initRealm() {
        user = SyncUser.currentUser();
        if (user == null) {
            final ProgressDialog progressDialog = new ProgressDialog(StartActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
            SyncCredentials syncCredentials = SyncCredentials.usernamePassword("user", "user", false);
            Log.e("StartActivity", syncCredentials.getUserIdentifier());

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
                            errorMsg = "User nameTextView and password does not match";
                            break;
                        default:
                            errorMsg = error.toString();
                    }
                    onLoginFailed(errorMsg);
                }
            };

            SyncUser.loginAsync(syncCredentials, AUTH_URL, callback);

        } else {
            onLoginSuccess(user);
        }
    }

    private void onLoginSuccess(SyncUser user) {
        if (user == null) {
            Log.e("StartActivity", "Error while logging to realm");
            return;
        }

        StartActivity.user = user;
        SyncConfiguration config = new SyncConfiguration.Builder(user, REALM_URL).build();
        Realm.setDefaultConfiguration(config);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void onLoginFailed(String errorMsg) {
        Log.e("StartActivity", errorMsg);
    }
}
