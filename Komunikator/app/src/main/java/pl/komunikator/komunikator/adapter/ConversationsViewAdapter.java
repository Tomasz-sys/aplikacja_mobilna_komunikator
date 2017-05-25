package pl.komunikator.komunikator.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmList;
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.activity.ContainerActivity;
import pl.komunikator.komunikator.activity.ConversationActivity;
import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.User;
import pl.komunikator.komunikator.viewHolder.ConversationViewHolder;
import pl.komunikator.komunikator.viewHolder.EmptyViewHolder;

/**
 * Created by adrian on 20.05.2017.
 */

public class ConversationsViewAdapter extends RecyclerView.Adapter {

    private static final int sEMPTY_LIST = 404;
    private RealmList<Conversation> mConversations;

    public ConversationsViewAdapter(RealmList<Conversation> conversations) {
        mConversations = conversations;
    }

    @Override
    public int getItemCount() {
        int size = mConversations.size();
        if (size > 0) {
            return size;
        } else {
            size += 1;
            return size;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mConversations.size() == 0) {
            return sEMPTY_LIST;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == sEMPTY_LIST) {
            view = inflater.inflate(R.layout.item_no_results, parent, false);
            return new EmptyViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_conversation, parent, false);
            return new ConversationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == sEMPTY_LIST) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            emptyViewHolder.textView.setText(R.string.empty_conversations);
            return;
        }

        final Conversation conversation = mConversations.get(position);

        ConversationViewHolder viewHolder = (ConversationViewHolder) holder;
        viewHolder.contactTextView.setText(conversation.getName());

        String lastMessage;
        try {
            lastMessage = conversation.getMessages().last().getContent();
            viewHolder.lastMessageTextView.setText(lastMessage);
        } catch (Exception e) {
            viewHolder.lastMessageTextView.setText("Nie wymieniono jeszcze wiadomości...");
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConversationActivity.show((Activity) view.getContext(), conversation.getId());
            }
        });

    }

}
