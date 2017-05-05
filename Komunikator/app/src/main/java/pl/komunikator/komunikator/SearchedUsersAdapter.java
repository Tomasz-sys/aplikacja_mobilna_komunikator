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

public class SearchedUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> userList;
    private boolean isSearching;

    public SearchedUsersAdapter(List<User> userList, boolean isSearching) {
        this.userList = userList;
        this.isSearching = isSearching;
    }

    public class PossibleFriendViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public ImageView photo;
        public ImageButton addFriend;

        public PossibleFriendViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.contactName);
            email = (TextView) view.findViewById(R.id.contactEmail);
            photo = (ImageView) view.findViewById(R.id.contactImageView);
            addFriend = (ImageButton) view.findViewById(R.id.addFriendImageButton);
        }
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public ImageView photo;
        public ImageView contactDetails;

        public ContactViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.contactName);
            email = (TextView) view.findViewById(R.id.contactEmail);
            photo = (ImageView) view.findViewById(R.id.contactImageView);
            contactDetails = (ImageView) view.findViewById(R.id.contactDetailsImageView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (isSearching) {
            View v1 = inflater.inflate(R.layout.item_searched_user, parent, false);
            viewHolder = new PossibleFriendViewHolder(v1);
        } else {
            View v1 = inflater.inflate(R.layout.item_contact, parent, false);
            viewHolder = new ContactViewHolder(v1);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final User user = userList.get(position);

        if (isSearching) {
            PossibleFriendViewHolder friendViewHolder = (PossibleFriendViewHolder) holder;
            friendViewHolder.name.setText(user.getUsername());
            friendViewHolder.email.setText(user.getEmail());
            friendViewHolder.addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User loggedUser = User.getLoggedUser();
                    List<User> loggedUserFriends = loggedUser.friends;

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    User friend = realm.where(User.class).equalTo("id", user.getId()).findFirst();
                    loggedUserFriends.add(friend);
                    friend.friends.add(loggedUser);
                    realm.commitTransaction();

                    userList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                }
            });
        } else {
            ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
            contactViewHolder.name.setText(user.getUsername());
            contactViewHolder.email.setText(user.getEmail());
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
