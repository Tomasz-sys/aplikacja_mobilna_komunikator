package pl.komunikator.komunikator;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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

        User user1 = new User();
        user1.setUsername("Adrian");
        user1.setEmail("asdas@o2.pl");

        User user2 = new User();
        user2.setUsername("Marek");
        user2.setEmail("marek@o2.pl");

        User user3 = new User();
        user3.setUsername("Przemek");
        user3.setEmail("przemek@o2.pl");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        SearchedUsersAdapter adapter = new SearchedUsersAdapter(users);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.conversationsView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
