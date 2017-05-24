package pl.komunikator.komunikator.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.adapter.ConversationCreatorAdapter;
import pl.komunikator.komunikator.entity.User;

public class CreateConversationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_conversation);

        mRecyclerView = (RecyclerView) findViewById(R.id.checkBoxedContactsView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ConversationCreatorAdapter adapter = new ConversationCreatorAdapter(User.getLoggedUser().friends);
        mRecyclerView.setAdapter(adapter);
    }
}
