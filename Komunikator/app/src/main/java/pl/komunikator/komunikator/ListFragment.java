package pl.komunikator.komunikator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.realm.Realm;
import pl.komunikator.komunikator.entity.User;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class ListFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.conversationsView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView = recyclerView;

        showUserFriends();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ListActivity listActivity = (ListActivity) getActivity();

        final SearchView searchView = (SearchView) listActivity.menuBar.findItem(R.id.action_search).getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPossibleFriends();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                showUserFriends();

                searchView.onActionViewCollapsed();

                return true;
            }
        });
    }

    private void showUserFriends() {
        Realm realm = Realm.getDefaultInstance();
        User user = User.getLoggedUser();
        List<User> userFriends = realm.copyFromRealm(user.friends);
        SearchedUsersAdapter adapter = new SearchedUsersAdapter(userFriends);
        recyclerView.setAdapter(adapter);
    }

    private void showPossibleFriends() {
        Realm realm = Realm.getDefaultInstance();
        User loggedUser = User.getLoggedUser();

        List<User> allUsers = realm.where(User.class).notEqualTo("id", loggedUser.getId()).findAll();
        allUsers = realm.copyFromRealm(allUsers);

        List<User> loggedUserFriends = realm.copyFromRealm(loggedUser.friends);

        differUserLists(loggedUserFriends, allUsers);

        recyclerView.setAdapter(new SearchedUsersAdapter(allUsers));
    }

    private void differUserLists(List<User> loggedUserFriends, List<User> allUsers) {
        for (User loggedUserFriend : loggedUserFriends) {
            for (User user: allUsers) {
                if (user.getId() == loggedUserFriend.getId()) {
                    allUsers.remove(user);
                }
            }
        }
    }

}
