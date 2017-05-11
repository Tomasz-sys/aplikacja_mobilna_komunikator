package pl.komunikator.komunikator;

import android.content.Intent;
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
import io.realm.RealmObject;
import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 23.03.2017.
 */

public class SignupActivity extends AppCompatActivity {

    private EditText login, email, password, retypePassword;
    private CheckBox rules, policy;
    private Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
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
                    login.setError(getString(R.string.register_required_field));
                    result = false;
                }
                if (emailText.isEmpty()) {
                    email.setError(getString(R.string.register_required_field));
                    result = false;
                }
                if (!passwordText.equals(retypeStringText)) {
                    retypePassword.setError(getString(R.string.register_password_different));
                    result = false;
                }
                if (!rules.isChecked()) {
                    rules.setError(getString(R.string.register_required_field));
                    result = false;
                }

                if (!policy.isChecked()) {
                    policy.setError(getString(R.string.register_required_field));
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
                RealmObject object = realm.where(User.class).equalTo("username", loginText).or().equalTo("email", emailText).findFirst();
                if (object == null) {
                    Number id = realm.where(User.class).max("id");
                    User user = realm.createObject(User.class, (id == null) ? 1 : id.longValue() + 1);
                    user.setUsername(loginText);
                    user.setPassword(Hashing.sha1().hashString(passwordText, Charsets.UTF_8).toString());
                    user.setEmail(emailText);
                    User.setLoggedUser(user);
                } else {
                    Snackbar.make(login, R.string.register_login_email_used, Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                if (User.getLoggedUser() != null) {
                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(intent);

                    finish();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Snackbar.make(password, R.string.register_something_goes_wrong, Snackbar.LENGTH_SHORT).show();
                Log.e("Sign Up",error.getMessage(),error);
                User.logout();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
