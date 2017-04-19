package pl.komunikator.komunikator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    private EditText login, password;
    private Realm realm;
    private SyncUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = SyncUser.currentUser();
        SyncConfiguration config = new SyncConfiguration.Builder(user, KomunikatorApplication.REALM_URL).build();

        realm = Realm.getInstance(config);


        login = (EditText) findViewById(R.id.loginEditText);
        password = (EditText) findViewById(R.id.passwordEditText);

    }


    public void signIn(View view) {
        SyncConfiguration config = new SyncConfiguration.Builder(user, KomunikatorApplication.REALM_URL)
                .build();

        // This will automatically sync all changes in the background for as long as the Realm is open
        realm = Realm.getInstance(config);
        User user = realm.where(User.class)
                .equalTo("username", login.getText().toString())
                .equalTo("password", Hashing.sha1().hashString(password.getText().toString(), Charsets.UTF_8).toString())
                .findFirst();
        if (user == null) {
            login.setError(getString(R.string.login_check_username));
            password.setError(getString(R.string.login_check_password));
        } else {
            User.setLoggedUser(user);
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(intent);
        }

    }

    public void signUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }
}
