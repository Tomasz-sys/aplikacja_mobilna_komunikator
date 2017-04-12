package pl.komunikator.komunikator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;
import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 23.03.2017.
 */

public class SignupActivity extends AppCompatActivity {

    private EditText login, email, password, retypePassword;
    private CheckBox rules, policy;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SyncConfiguration config = new SyncConfiguration.Builder(SyncUser.currentUser(), KomunikatorApplication.REALM_URL).build();

        realm = Realm.getInstance(config);

        login = (EditText) findViewById(R.id.signupLoginEditText);
        email = (EditText) findViewById(R.id.signupEmailEditText);
        password = (EditText) findViewById(R.id.signupPasswordEditText);
        retypePassword = (EditText) findViewById(R.id.signupRetypePasswordEditText);
        rules = (CheckBox) findViewById(R.id.signupCheckboxRules);
        policy = (CheckBox) findViewById(R.id.signupCheckboxPolicy);
        Button register = (Button) findViewById(R.id.signupButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginText = login.getText().toString();
                String passwordText = password.getText().toString();
                String retypeStringText = retypePassword.getText().toString();
                String emailText = email.getText().toString();
                boolean result = true;
                if (loginText.isEmpty()) {
                    login.setError("Pole wymagane");
                    result = false;
                }
                if (emailText.isEmpty()) {
                    email.setError("Pole wymagane");
                    result = false;
                }
                if (!passwordText.equals(retypeStringText)) {
                    retypePassword.setError("Hasła się nie zgadzają");
                }
                if (!rules.isChecked()) {
                    rules.setError("Pole wymagane");
                    result = false;
                }

                if (!policy.isChecked()) {
                    policy.setError("Pole wymagane");
                    result = false;
                }

                if (result) {
                    register(loginText, passwordText, emailText);
                }

            }
        });

    }

    private void register(final String loginText, final String passwordText, final String emailText) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.createObject(User.class, realm.where(User.class).max("id").longValue() + 1);
                user.setUsername(loginText);
                user.setPassword(Hashing.sha1().hashString(passwordText, Charsets.UTF_8).toString());
                user.setEmail(emailText);
                User.setLoggedUser(user);

            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Snackbar.make(password, "Coś poszło nie tak", Snackbar.LENGTH_SHORT).show();
                Log.e("Sign up", error.getMessage());
                User.logout();
            }
        });
    }

}
