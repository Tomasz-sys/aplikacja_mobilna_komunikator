package pl.komunikator.komunikator.interfaces;

import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 24.05.2017.
 */
public interface OnConversationCreatedListener {

    void onContactSelected(User contact);
    void onContactDetailsClicked(User contact);

}
