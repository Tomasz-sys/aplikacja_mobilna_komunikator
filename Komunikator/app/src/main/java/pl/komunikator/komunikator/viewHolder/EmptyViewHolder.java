package pl.komunikator.komunikator.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pl.komunikator.komunikator.R;

/**
 * Created by adrian on 22.05.2017.
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public EmptyViewHolder(View view) {
        super(view);

        textView = (TextView) view.findViewById(R.id.textView);
    }

}
