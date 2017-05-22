package pl.komunikator.komunikator.fragment;

import android.content.Context;
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

import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.activity.ContainerActivity;
import pl.komunikator.komunikator.adapter.ConversationCreatorAdapter;
import pl.komunikator.komunikator.adapter.UsersViewAdapter;
import pl.komunikator.komunikator.entity.User;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class ContactsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    OnUserSelectedListener mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.conversationsView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView = recyclerView;

        showUserFriends();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ContainerActivity containerActivity = (ContainerActivity) getActivity();

        final SearchView searchView = (SearchView) containerActivity.getMenu().findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                UsersViewAdapter adapter = (UsersViewAdapter) mRecyclerView.getAdapter();
                adapter.filterUserList(newText);
                return false;
            }
        });

        final MenuItem createConversationMenuItem = containerActivity.getMenu().findItem(R.id.action_create_conversation);
        createConversationMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showConversationCreator();
                return false;
            }
        });

        final MenuItem addFriendsMenuItem = containerActivity.getMenu().findItem(R.id.action_add_friends);
        addFriendsMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showPossibleFriends();
                searchView.setIconified(false);
                addFriendsMenuItem.setEnabled(false);
                createConversationMenuItem.setEnabled(false);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                showUserFriends();

                searchView.onActionViewCollapsed();
                addFriendsMenuItem.setEnabled(true);
                createConversationMenuItem.setEnabled(true);

                return true;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnUserSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    private void showUserFriends() {
        List<User> userFriends = getCopyOfUserFriends();
        UsersViewAdapter adapter = new UsersViewAdapter(userFriends, true);
        mRecyclerView.setAdapter(adapter);
    }

    private void showPossibleFriends() {
        Realm realm = Realm.getDefaultInstance();
        User loggedUser = User.getLoggedUser();

        List<User> allUsers = realm.where(User.class).notEqualTo("id", loggedUser.getId()).findAll();
        allUsers = realm.copyFromRealm(allUsers);

        List<User> loggedUserFriends = realm.copyFromRealm(loggedUser.friends);

        differUserLists(loggedUserFriends, allUsers);

        mRecyclerView.setAdapter(new UsersViewAdapter(allUsers, false));
    }

    private void differUserLists(List<User> loggedUserFriends, List<User> allUsers) {
        for (User loggedUserFriend : loggedUserFriends) {
            Iterator<User> allUserIterator = allUsers.iterator();

            while (allUserIterator.hasNext()) {
                User user = allUserIterator.next();

                if (user.getId() == loggedUserFriend.getId())
                    allUserIterator.remove();
            }
        }
    }

    private void showConversationCreator() {
        List<User> userFriends = getCopyOfUserFriends();
        ConversationCreatorAdapter adapter = new ConversationCreatorAdapter(userFriends);
        mRecyclerView.setAdapter(adapter);
    }

    private List<User> getCopyOfUserFriends() {
        Realm realm = Realm.getDefaultInstance();
        User user = User.getLoggedUser();
        return realm.copyFromRealm(user.friends);
    }

    public interface OnUserSelectedListener {
        void onContactSelected(User contact);
    }

}
