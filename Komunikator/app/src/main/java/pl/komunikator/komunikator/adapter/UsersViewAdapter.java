package pl.komunikator.komunikator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.activity.ContainerActivity;
import pl.komunikator.komunikator.entity.User;
import pl.komunikator.komunikator.viewHolder.ContactViewHolder;
import pl.komunikator.komunikator.viewHolder.EmptyViewHolder;
import pl.komunikator.komunikator.viewHolder.SearchedUserViewHolder;

/**
 * Created by adrian on 19.04.2017.
 */

public class UsersViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int sNO_RESULTS_CODE = 404;
    private List<User> mUsers;
    private List<User> mFilteredUsers;
    private boolean mDisplaysContacts;

    public UsersViewAdapter(List<User> users, boolean displaysContacts) {
        mUsers = users;
        mFilteredUsers = new ArrayList<>(users);

        mDisplaysContacts = displaysContacts;
    }

    @Override
    public int getItemCount() {
        int size = mFilteredUsers.size();
        if (size > 0) {
            return size;
        } else {
            size += 1;
            return size;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFilteredUsers.size() == 0) {
            return sNO_RESULTS_CODE;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == sNO_RESULTS_CODE) {
            view = inflater.inflate(R.layout.item_all_empty_list, parent, false);
            return new EmptyViewHolder(view);
        }

        if (mDisplaysContacts) {
            view = inflater.inflate(R.layout.item_fragment_contacts, parent, false);
            return new ContactViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_fragment_contacts_possible_friend, parent, false);
            return new SearchedUserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (getItemViewType(position) == sNO_RESULTS_CODE) {
            return;
        }

        final User user = mFilteredUsers.get(position);

        if (mDisplaysContacts) {
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
                    container.onContactSelected(mUsers.get(position));
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

                    long removedUserId = mFilteredUsers.remove(position).getId();

                    Iterator<User> allUserIterator = mUsers.iterator();

                    while (allUserIterator.hasNext()) {
                        User user = allUserIterator.next();

                        if (user.getId() == removedUserId) {
                            allUserIterator.remove();
                        }
                    }

                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mFilteredUsers.size());
                }
            });

        }
    }

    public void filterUserList(String text) {
        mFilteredUsers = new ArrayList<>(mUsers);
        Iterator<User> allUserIterator = mFilteredUsers.iterator();

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
