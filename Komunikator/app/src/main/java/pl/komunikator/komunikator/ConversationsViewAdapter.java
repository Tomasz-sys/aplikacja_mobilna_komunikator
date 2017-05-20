package pl.komunikator.komunikator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 20.05.2017.
 */

public class ConversationsViewAdapter extends RecyclerView.Adapter {

    private List<Conversation> conversations;

    public ConversationsViewAdapter(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        public TextView contactTextView;
        public TextView lastMessageTextView;

        public ConversationViewHolder(View view) {
            super(view);

            contactTextView = (TextView) view.findViewById(R.id.contactTextView);
            lastMessageTextView = (TextView) view.findViewById(R.id.lastMessageTextView);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ConversationViewHolder(inflater.inflate(R.layout.item_conversation, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);

        ConversationViewHolder viewHolder = (ConversationViewHolder) holder;

        String usersText = "";
        for (User user : conversation.getUsers()) {
            usersText += user.getUsername() + ", ";
        }

        viewHolder.contactTextView.setText(usersText);
        viewHolder.lastMessageTextView.setText(conversation.getMessages().last().getContent());
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }
}
