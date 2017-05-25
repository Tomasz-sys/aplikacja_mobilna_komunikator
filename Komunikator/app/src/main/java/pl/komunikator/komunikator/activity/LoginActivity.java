package pl.komunikator.komunikator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import io.realm.Realm;
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 23.03.2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEditText, mPasswordEditText;
    private Realm mRealm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginEditText = (EditText) findViewById(R.id.loginEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);

    }

    public void signIn(View view) {
        User user = mRealm.where(User.class)
                .equalTo("username", mLoginEditText.getText().toString())
                .equalTo("password", Hashing.sha1().hashString(mPasswordEditText.getText().toString(), Charsets.UTF_8).toString())
                .findFirst();
        if (user == null) {
            mLoginEditText.setError(getString(R.string.login_check_username));
            mPasswordEditText.setError(getString(R.string.login_check_password));
        } else {
            User.setLoggedUser(user);
            Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void signUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }
}
