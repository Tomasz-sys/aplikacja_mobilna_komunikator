package pl.komunikator.komunikator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 19.04.2017.
 */

public class SearchedUsersAdapter extends RecyclerView.Adapter<SearchedUsersAdapter.MyViewHolder> {

    private List<User> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public ImageView photo;
        public ImageButton addFriend;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.contactName);
            email = (TextView) view.findViewById(R.id.contactEmail);
            photo = (ImageView) view.findViewById(R.id.contactImageView);
            addFriend = (ImageButton) view.findViewById(R.id.addFriendImageButton);
        }
    }

    public SearchedUsersAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_searched_user, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final User user = userList.get(position);
        holder.name.setText(user.getUsername());
        holder.email.setText(user.getEmail());
        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User loggedUser = User.getLoggedUser();
                List<User> loggedUserFriends = loggedUser.friends;

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                loggedUserFriends.add(user);
                user.friends.add(loggedUser);
                realm.commitTransaction();

                userList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
