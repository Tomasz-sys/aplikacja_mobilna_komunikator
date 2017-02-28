package pl.tenek.realmtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    ListView listView;
    String REALM_URL = "";
    ArrayAdapter<String> adapter;
    private SyncUser user;
    private Realm realm;
    private Integer lastId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        REALM_URL = "realm://" + getString(R.string.object_server_address) + ":9080/~/default";

        if (SyncUser.currentUser() == null) {
            goToLoginActivity();
        }

        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.list_view);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listView.setAdapter(adapter);
        listView.setSelection(adapter.getCount()-1);


    }

    @Override
    protected void onStart() {
        super.onStart();
        user = SyncUser.currentUser();
        if (user != null) {
            // Create a RealmConfiguration for our user
            SyncConfiguration config = new SyncConfiguration.Builder(user, REALM_URL)
                    .build();

            // This will automatically sync all changes in the background for as long as the Realm is open
            realm = Realm.getInstance(config);
            RealmResults<Message> messageList = realm.where(Message.class).findAllAsync();
            messageList.addChangeListener(new RealmChangeListener<RealmResults<Message>>() {
                @Override
                public void onChange(RealmResults<Message> element) {
                    updateList(element);
                }
            });
            updateList(messageList);

        }

    }

    private void updateList(RealmResults<Message> element) {
        if (element == null) return;
        List<String> messages = new ArrayList<>();
        for (int i = 0; i < element.size(); i++) {
            Message m = element.get(i);
            if (m != null && lastId < m.getId() && m.getContent() != null && !m.getContent().isEmpty()) {
                lastId = m.getId();
                messages.add(m.getContent());
                adapter.add(m.getContent());
                listView.setSelection(adapter.getCount()-1);
            }
        }


    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void sendMessage() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Message msg = realm.createObject(Message.class, realm.where(Message.class).max("id").intValue() + 1);
                msg.setContent(editText.getText().toString());
                msg.setCreateDate(new Date());

            }
        });
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
