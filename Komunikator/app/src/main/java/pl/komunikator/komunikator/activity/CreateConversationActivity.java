package pl.komunikator.komunikator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.List;

import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.adapter.ConversationCreatorAdapter;
import pl.komunikator.komunikator.entity.User;

public class CreateConversationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ConversationCreatorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversation);

        initViews();
    }

    private void initViews() {
        initRecyclerView();
        initAdapter();
        initButtonBar();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.checkBoxedContactsView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initAdapter() {
        ConversationCreatorAdapter adapter = new ConversationCreatorAdapter(User.getLoggedUser().friends);
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

                Intent intent = new Intent(activity, ContainerActivity.class);
                intent.putExtra("ids", (Serializable) ids);
                activity.setResult(RESULT_OK, intent);
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
