package pl.tenek.realmtest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.SyncCredentials;
import io.realm.ObjectServerError;
import io.realm.SyncUser;
import io.realm.UserStore;
/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    EditText password;
    EditText email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);

        ((Button) findViewById(R.id.email_sign_in_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(true);
            }
        });
        ((Button) findViewById(R.id.login_sign_up_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(false);
            }
        });


    }


    private void login(boolean login) {


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String username = this.email.getText().toString();
        String password = this.password.getText().toString();

        SyncCredentials creds = SyncCredentials.usernamePassword(username, password,!login);
        String authUrl = "http://" + getString(R.string.object_server_address) + ":9080/auth";
        SyncUser.Callback callback = new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                progressDialog.dismiss();
                onLoginSuccess();
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

        SyncUser.loginAsync(creds, authUrl, callback);
    }

    private void onLoginFailed(String errorMsg) {
        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_LONG).show();
    }

    private void onLoginSuccess() {
        finish();

    }
}