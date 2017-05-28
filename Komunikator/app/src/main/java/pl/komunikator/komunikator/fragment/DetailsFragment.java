package pl.komunikator.komunikator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.entity.User;

public class DetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        TextView details_bold_name = (TextView) view.findViewById(R.id.details_bold_name);
        details_bold_name.setText(User.getLoggedUser().getUsername());

        TextView details_email = (TextView) view.findViewById(R.id.details_email);
        details_email.setText(User.getLoggedUser().getEmail());
        
        return view;
    }

    public static DetailsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
