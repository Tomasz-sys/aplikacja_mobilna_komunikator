package pl.komunikator.komunikator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.komunikator.komunikator.entity.User;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class ListFragment extends Fragment {

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

        Realm realm = Realm.getDefaultInstance();
        RealmResults<User> allUsers = realm.where(User.class).findAll();
        List<User> users = realm.copyFromRealm(allUsers);

        SearchedUsersAdapter adapter = new SearchedUsersAdapter(users);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
