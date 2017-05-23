package pl.komunikator.komunikator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 23.03.2017.
 */

public class SignupActivity extends AppCompatActivity {

    private static final int USER_MESSAGE_WHAT = 3455;
    private EditText mLoginEditText, mEmailEditText, mPasswordEditText, mRetypePasswordEditText;
    private CheckBox mRulesCheckBox, mPolicyCheckBox;
    private Realm mRealm = Realm.getDefaultInstance();
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == USER_MESSAGE_WHAT)
            {
                Long userId = (Long)msg.obj;
                User user = mRealm.where(User.class).equalTo("id",userId).findFirst();
                User.setLoggedUser(user);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mLoginEditText = (EditText) findViewById(R.id.signupLoginEditText);
        mEmailEditText = (EditText) findViewById(R.id.signupEmailEditText);
        mPasswordEditText = (EditText) findViewById(R.id.signupPasswordEditText);
        mRetypePasswordEditText = (EditText) findViewById(R.id.signupRetypePasswordEditText);
        mRulesCheckBox = (CheckBox) findViewById(R.id.signupCheckboxRules);
        mPolicyCheckBox = (CheckBox) findViewById(R.id.signupCheckboxPolicy);
        Button register = (Button) findViewById(R.id.signupButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginText = mLoginEditText.getText().toString();
                String passwordText = mPasswordEditText.getText().toString();
                String retypeStringText = mRetypePasswordEditText.getText().toString();
                String emailText = mEmailEditText.getText().toString();
                boolean result = true;
                if (loginText.isEmpty()) {
                    mLoginEditText.setError(getString(R.string.register_required_field));
                    result = false;
                }
                if (emailText.isEmpty()) {
                    mEmailEditText.setError(getString(R.string.register_required_field));
                    result = false;
                }
                if (!passwordText.equals(retypeStringText)) {
                    mRetypePasswordEditText.setError(getString(R.string.register_password_different));
                    result = false;
                }
                if (!mRulesCheckBox.isChecked()) {
                    mRulesCheckBox.setError(getString(R.string.register_required_field));
                    result = false;
                }

                if (!mPolicyCheckBox.isChecked()) {
                    mPolicyCheckBox.setError(getString(R.string.register_required_field));
                    result = false;
                }

                if (result) {
                    register(loginText, passwordText, emailText);
                }

            }
        });

    }
    Long userId = null;
    private void register(final String loginText, final String passwordText, final String emailText) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmObject object = realm.where(User.class).equalTo("username", loginText).or().equalTo("mEmailEditText", emailText).findFirst();
                if (object == null) {
                    Number id = realm.where(User.class).max("id");
                    User user = realm.createObject(User.class, (id == null) ? 1 : id.longValue() + 1);
                    user.setUsername(loginText);
                    user.setPassword(Hashing.sha1().hashString(passwordText, Charsets.UTF_8).toString());
                    user.setEmail(emailText);
                    userId = user.getId();

                } else {
                    Snackbar.make(mLoginEditText, R.string.register_login_email_used, Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                if (userId != null) {
                    Message mgs = mHandler.obtainMessage(USER_MESSAGE_WHAT,userId);
                    mgs.sendToTarget();
                    Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                    startActivity(intent);

                    finish();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Snackbar.make(mPasswordEditText, R.string.register_something_goes_wrong, Snackbar.LENGTH_SHORT).show();
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
