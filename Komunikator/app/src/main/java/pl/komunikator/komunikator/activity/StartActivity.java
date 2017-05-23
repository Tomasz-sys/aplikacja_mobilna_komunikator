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

    private static final String sREALM_URL = "realm://81.2.250.203:9080/~/komunikator";
    private static final String sAUTH_URL = "http://81.2.250.203:9080/auth";
    private static SyncUser sUser = null;
    private static final int sMY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this
                        , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, sMY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        initRealm();
    }

    private void initRealm() {
        sUser = SyncUser.currentUser();
        if (sUser == null) {
            final ProgressDialog progressDialog = new ProgressDialog(StartActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
            SyncCredentials syncCredentials = SyncCredentials.usernamePassword("sUser", "sUser", false);
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

            SyncUser.loginAsync(syncCredentials, sAUTH_URL, callback);

        } else {
            onLoginSuccess(sUser);
        }
    }

    private void onLoginSuccess(SyncUser user) {
        if (user == null) {
            Log.e("StartActivity", "Error while logging to realm");
            return;
        }

        StartActivity.sUser = user;
        SyncConfiguration config = new SyncConfiguration.Builder(user, sREALM_URL).build();
        Realm.setDefaultConfiguration(config);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void onLoginFailed(String errorMsg) {
        Log.e("StartActivity", errorMsg);
    }
}
