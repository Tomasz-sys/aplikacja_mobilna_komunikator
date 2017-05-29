package pl.komunikator.komunikator.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.RealmUtilities;
import pl.komunikator.komunikator.entity.User;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView details_email = (TextView) findViewById(R.id.details_email);
        TextView details_bold_name = (TextView) findViewById(R.id.details_bold_name);

        String name;
        String email;

        RealmUtilities realm = new RealmUtilities();
        long id = getIntent().getLongExtra("userId", 0);
        if (id == 0) {
            name = User.getLoggedUser().getUsername();
            email = User.getLoggedUser().getEmail();
        } else {
            User user = realm.getUser(id);
            name = user.getUsername();
            email = user.getEmail();
        }

        details_bold_name.setText(name);
        details_email.setText(email);

        setTitle("Szczegóły");
    }

    public static void show(Activity startActivity, long userId) {
        Intent intent = new Intent(startActivity, DetailsActivity.class);
        intent.putExtra("userId", userId);
        startActivity.startActivity(intent);
    }

}
