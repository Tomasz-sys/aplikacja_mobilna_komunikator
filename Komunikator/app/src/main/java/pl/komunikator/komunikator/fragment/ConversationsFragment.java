package pl.komunikator.komunikator.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import pl.komunikator.komunikator.adapter.ConversationsViewAdapter;
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.Message;
import pl.komunikator.komunikator.entity.User;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class ConversationsFragment extends Fragment {

    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.conversationsView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView = recyclerView;

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Conversation> results = realm.where(Conversation.class).findAll();
        List<Conversation> conversations = realm.copyFromRealm(results);

        ConversationsViewAdapter adapter = new ConversationsViewAdapter(conversations);
        mRecyclerView.setAdapter(adapter);
    }

}
