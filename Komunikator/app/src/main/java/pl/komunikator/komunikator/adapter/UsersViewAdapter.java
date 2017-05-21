package pl.komunikator.komunikator.adapter;

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
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.activity.ContainerActivity;
import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 19.04.2017.
 */

public class UsersViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NO_RESULTS_CODE = 404;
    private List<User> userList;
    private List<User> filteredUsers;
    private boolean displaysContacts;

    public UsersViewAdapter(List<User> userList, boolean displaysContacts) {
        this.userList = userList;
        filteredUsers = new ArrayList<>(userList);

        this.displaysContacts = displaysContacts;
    }

    @Override
    public int getItemCount() {
        int size = filteredUsers.size();
        if (size > 0) {
            return size;
        } else {
            size += 1;
            return size;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (filteredUsers.size() == 0) {
            return NO_RESULTS_CODE;
        } else {
            return super.getItemViewType(position);
        }
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

    public class NoResultsViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public NoResultsViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.textView);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == NO_RESULTS_CODE) {
            view = inflater.inflate(R.layout.item_no_results, parent, false);
            return new NoResultsViewHolder(view);
        }

        if (displaysContacts) {
            view = inflater.inflate(R.layout.item_contact, parent, false);
            return new ContactViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_searched_user, parent, false);
            return new SearchedUserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (getItemViewType(position) == NO_RESULTS_CODE) {
            return;
        }

        final User user = filteredUsers.get(position);

        if (displaysContacts) {
            ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
            contactViewHolder.nameTextView.setText(user.getUsername());
            contactViewHolder.emailTextView.setText(user.getEmail());
            contactViewHolder.detailsImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    /* TODO show user details */
                }
            });
            contactViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContainerActivity container = (ContainerActivity) view.getContext();
                    container.onContactSelected(userList.get(position));
                }
            });
        } else {
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

                    long removedUserId = filteredUsers.remove(position).getId();

                    Iterator<User> allUserIterator = userList.iterator();

                    while (allUserIterator.hasNext()) {
                        User user = allUserIterator.next();

                        if (user.getId() == removedUserId) {
                            allUserIterator.remove();
                        }
                    }

                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, filteredUsers.size());
                }
            });

        }
    }

    public void filterUserList(String text) {
        filteredUsers = new ArrayList<>(userList);
        Iterator<User> allUserIterator = filteredUsers.iterator();

        while (allUserIterator.hasNext()) {
            User user = allUserIterator.next();
            String userName = user.getUsername();
            String userEmail = user.getEmail();

            if (!userName.contains(text) && !userEmail.contains(text)) {
                allUserIterator.remove();
            }
        }

        notifyDataSetChanged();
    }

}
