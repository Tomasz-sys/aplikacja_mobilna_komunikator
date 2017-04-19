package pl.komunikator.komunikator;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 19.04.2017.
 */

public class SearchedUsersAdapter extends RecyclerView.Adapter<SearchedUsersAdapter.MyViewHolder> {

    private List<User> userList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email;
        public ImageView photo;

        public MyViewHolder(View view) {
            super(view);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
