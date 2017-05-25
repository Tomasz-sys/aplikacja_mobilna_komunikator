package pl.komunikator.komunikator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.entity.User;
import pl.komunikator.komunikator.viewHolder.EmptyViewHolder;

/**
 * Created by adrian on 22.05.2017.
 */

public class ConversationCreatorAdapter extends RecyclerView.Adapter {

    private static final int sNO_CONTACTS = 404;
    private List<User> mContacts;
    private List<Long> mSelectedItemsIds;

    public ConversationCreatorAdapter(List<User> contacts) {
        mContacts = contacts;
        mSelectedItemsIds = new ArrayList<>(Collections.<Long>nCopies(mContacts.size(), null));
    }

    @Override
    public int getItemCount() {
        int size = mContacts.size();
        if (size > 0) {
            return size;
        } else {
            size += 1;
            return size;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mContacts.size() == 0) {
            return sNO_CONTACTS;
        } else {
            return super.getItemViewType(position);
        }
    }

    public class CheckBoxedContactViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, emailTextView;
        public ImageView avatarImageView;
        public CheckBox checkBox;

        public CheckBoxedContactViewHolder(View view) {
            super(view);

            nameTextView = (TextView) view.findViewById(R.id.contactName);
            emailTextView = (TextView) view.findViewById(R.id.contactEmail);
            avatarImageView = (ImageView) view.findViewById(R.id.contactImageView);
            checkBox = (CheckBox) view.findViewById(R.id.contactCheckBox);
        }

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == sNO_CONTACTS) {
            view = inflater.inflate(R.layout.item_all_empty_list, parent, false);
            return new EmptyViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_activity_create_conversation, parent, false);
            return new CheckBoxedContactViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (getItemViewType(position) == sNO_CONTACTS) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            emptyViewHolder.textView.setText(R.string.empty_contacts);
            return;
        }

        final User contact = mContacts.get(position);

        CheckBoxedContactViewHolder viewHolder = (CheckBoxedContactViewHolder) holder;
        viewHolder.nameTextView.setText(contact.getUsername());
        viewHolder.emailTextView.setText(contact.getEmail());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mSelectedItemsIds.add(position, contact.getId());
                } else {
                    mSelectedItemsIds.remove(position);
                }
            }
        });
    }

    public List<Long> getIds() {
        List<Long> ids = new ArrayList<>(mSelectedItemsIds);
        ids.removeAll(Collections.singleton(null));

        return ids;
    }

}
