package pl.komunikator.komunikator.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmList;
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.activity.ContainerActivity;
import pl.komunikator.komunikator.activity.CreateConversationActivity;
import pl.komunikator.komunikator.adapter.ConversationsViewAdapter;
import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.User;
import pl.komunikator.komunikator.interfaces.OnConversationCreatedListener;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class ConversationsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Context mContext;
    private OnConversationCreatedListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.conversationsView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final ContainerActivity containerActivity = (ContainerActivity) getActivity();
        final SearchView searchView = (SearchView) containerActivity.getMenu().findItem(R.id.action_search).getActionView();

        mRecyclerView = recyclerView;

        final MenuItem createConversationMenuItem = containerActivity.getMenu().findItem(R.id.action_create_conversation);
        createConversationMenuItem.setVisible(true);
        createConversationMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showConversationCreator();
                searchView.setIconified(true);
                return false;
            }
        });
        final MenuItem addFriendsMenuItem = containerActivity.getMenu().findItem(R.id.action_add_friends);
        addFriendsMenuItem.setVisible(false);
        searchView.setVisibility(View.GONE);
        searchView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility != View.GONE)
                {
                    searchView.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        User loggedUser = User.getLoggedUser();
        RealmList<Conversation> conversations = loggedUser.getConversations();

        ConversationsViewAdapter adapter = new ConversationsViewAdapter(conversations);
        mRecyclerView.setAdapter(adapter);
    }

    private void showConversationCreator() {
        Intent intent = new Intent(mContext, CreateConversationActivity.class);
        mContext.startActivity(intent);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        try {
            mCallback = (OnConversationCreatedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnConversationCreatedListener");
        }
    }

}
