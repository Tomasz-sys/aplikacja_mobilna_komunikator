package pl.komunikator.komunikator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import io.realm.RealmList;
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.RealmUtilities;
import pl.komunikator.komunikator.adapter.ConversationCreatorAdapter;
import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.User;

public class CreateConversationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ConversationCreatorAdapter mAdapter;
    private Conversation mConversationToEdit;

    private RealmUtilities realm = new RealmUtilities();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversation);

        initConversationToEditIfExists();
        initViews();
    }

    private void initConversationToEditIfExists() {
        long id = getIntent().getLongExtra("editId", 0);
        mConversationToEdit = realm.getConversation(id);
    }

    private void initViews() {
        initRecyclerView();
        initAdapter();
        initButtonBar();

        setTitle("Utwórz konwersację");
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.checkBoxedContactsView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initAdapter() {
        ConversationCreatorAdapter adapter;
        try {
            RealmList<User> filteredUserContacts = realm.getFilteredUserContacts(mConversationToEdit.getUsers());
            adapter = new ConversationCreatorAdapter(filteredUserContacts);
        } catch (NullPointerException e) {
            adapter = new ConversationCreatorAdapter(User.getLoggedUser().friends);
        }
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
    }

    private void initButtonBar() {
        final Activity activity = this;

        final View buttonBar = findViewById(R.id.button_bar_contacts);
        Button createButton = (Button) buttonBar.findViewById(R.id.createBarButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Long> ids = mAdapter.getIds();

                if (ids.size() == 0) {
                    Toast.makeText(activity, "Nie wybrano żadnego kontaktu...", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(activity, ConversationActivity.class);

                if (mConversationToEdit != null) {
                    realm.bindObjects(mConversationToEdit, ids);
                    intent.putExtra("id", mConversationToEdit.getId());
                } else {
                    ids.add(0, User.getLoggedUser().getId());
                    Conversation conversation = realm.createConversation(ids);
                    intent.putExtra("id", conversation.getId());
                }

                activity.startActivity(intent);
                activity.finish();
            }
        });

        Button cancelButton = (Button) buttonBar.findViewById(R.id.cancelBarButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                activity.setResult(Activity.RESULT_CANCELED, returnIntent);
                activity.finish();
            }
        });
    }

}
