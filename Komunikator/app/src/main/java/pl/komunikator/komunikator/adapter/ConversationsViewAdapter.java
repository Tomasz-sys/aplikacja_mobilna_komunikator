package pl.komunikator.komunikator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.User;
import pl.komunikator.komunikator.viewHolder.ConversationViewHolder;
import pl.komunikator.komunikator.viewHolder.EmptyViewHolder;

/**
 * Created by adrian on 20.05.2017.
 */

public class ConversationsViewAdapter extends RecyclerView.Adapter {

    private static final int EMPTY_LIST = 404;
    private List<Conversation> conversations;

    public ConversationsViewAdapter(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @Override
    public int getItemCount() {
        int size = conversations.size();
        if (size > 0) {
            return size;
        } else {
            size += 1;
            return size;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (conversations.size() == 0) {
            return EMPTY_LIST;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == EMPTY_LIST) {
            view = inflater.inflate(R.layout.item_no_results, parent, false);
            return new EmptyViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_conversation, parent, false);
            return new ConversationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == EMPTY_LIST) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            emptyViewHolder.textView.setText(R.string.empty_conversations);
            return;
        }

        Conversation conversation = conversations.get(position);

        ConversationViewHolder viewHolder = (ConversationViewHolder) holder;

        StringBuilder result = new StringBuilder();
        for(User user : conversation.getUsers()) {
            result.append(user.getUsername());
            result.append(", ");
        }
        String usersText = result.length() > 0 ? result.substring(0, result.length() - 2) : "";

        viewHolder.contactTextView.setText(usersText);

        String lastMessage;
        try {
            lastMessage = conversation.getMessages().last().getContent();
            viewHolder.lastMessageTextView.setText(lastMessage);
        } catch (Exception e) {
            viewHolder.lastMessageTextView.setText("Nie wymieniono jeszcze wiadomości...");
        }

    }

}
