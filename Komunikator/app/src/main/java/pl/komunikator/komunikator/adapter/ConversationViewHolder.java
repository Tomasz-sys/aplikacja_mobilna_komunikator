package pl.komunikator.komunikator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pl.komunikator.komunikator.R;

/**
 * Created by adrian on 22.05.2017.
 */
public class ConversationViewHolder extends RecyclerView.ViewHolder {

    public TextView contactTextView;
    public TextView lastMessageTextView;

    public ConversationViewHolder(View view) {
        super(view);

        contactTextView = (TextView) view.findViewById(R.id.contactTextView);
        lastMessageTextView = (TextView) view.findViewById(R.id.lastMessageTextView);
    }

}
