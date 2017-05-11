package pl.komunikator.komunikator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 19.04.2017.
 */

public class UsersViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> userList;
    private List<User> filteredUsers;
    private boolean displaysSearchedUsers;
    private boolean isTyping;

    public UsersViewAdapter(List<User> userList, boolean displaysSearchedUsers) {
        this.userList = userList;
        this.displaysSearchedUsers = displaysSearchedUsers;
    }

    public void filterUserList(String text) {
        isTyping = !text.isEmpty();

        if (!isTyping) {
            notifyDataSetChanged();
            return;
        }

        filteredUsers = new ArrayList<>(userList);
        Iterator<User> allUserIterator = filteredUsers.iterator();
        int i = 0;

        while (allUserIterator.hasNext()) {
            User user = allUserIterator.next();
            String userName = user.getUsername();
            String userEmail = user.getEmail();

            if (!userName.contains(text) && !userEmail.contains(text)) {
                allUserIterator.remove();
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, filteredUsers.size() - 1);
            }

            i += 1;
        }
    }

    @Override
    public int getItemCount() {
        return isTyping ? filteredUsers.size() : userList.size();
    }

    public class SearchedUserViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, emailTextView;
        public ImageView avatarImageView;
        public ImageButton inviteImageButton;

        public SearchedUserViewHolder(View view) {
            super(view);

            nameTextView = (TextView) view.findViewById(R.id.contactName);
            emailTextView = (TextView) view.findViewById(R.id.contactEmail);
            avatarImageView = (ImageView) view.findViewById(R.id.contactImageView);
            inviteImageButton = (ImageButton) view.findViewById(R.id.addFriendImageButton);
        }

    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, emailTextView;
        public ImageView avatarImageView;
        public ImageButton detailsImageButton;

        public ContactViewHolder(View view) {
            super(view);

            nameTextView = (TextView) view.findViewById(R.id.contactName);
            emailTextView = (TextView) view.findViewById(R.id.contactEmail);
            avatarImageView = (ImageView) view.findViewById(R.id.contactImageView);
            detailsImageButton = (ImageButton) view.findViewById(R.id.contactDetailsImageButton);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (displaysSearchedUsers) {
            View v1 = inflater.inflate(R.layout.item_searched_user, parent, false);
            viewHolder = new SearchedUserViewHolder(v1);
        } else {
            View v1 = inflater.inflate(R.layout.item_contact, parent, false);
            viewHolder = new ContactViewHolder(v1);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final User user = isTyping ? filteredUsers.get(position) : userList.get(position);

        if (displaysSearchedUsers) {
            SearchedUserViewHolder friendViewHolder = (SearchedUserViewHolder) holder;
            friendViewHolder.nameTextView.setText(user.getUsername());
            friendViewHolder.emailTextView.setText(user.getEmail());
            friendViewHolder.inviteImageButton.setOnClickListener(new View.OnClickListener() {

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


                    if (isTyping) {
                        long removedUserId = filteredUsers.remove(position).getId();

                        Iterator<User> allUserIterator = userList.iterator();

                        while (allUserIterator.hasNext()) {
                            User user = allUserIterator.next();

                            if (user.getId() == removedUserId) {
                                allUserIterator.remove();
                            }
                        }

                    } else {
                        userList.remove(position);
                    }
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, filteredUsers.size() - 1);
                }
            });

        } else {
            ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
            contactViewHolder.nameTextView.setText(user.getUsername());
            contactViewHolder.emailTextView.setText(user.getEmail());
            contactViewHolder.detailsImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    /* TODO show user details */
                }
            });
        }
    }

}
