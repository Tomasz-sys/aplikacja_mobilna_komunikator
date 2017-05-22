package pl.komunikator.komunikator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import pl.komunikator.komunikator.R;

/**
 * Created by adrian on 22.05.2017.
 */
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
