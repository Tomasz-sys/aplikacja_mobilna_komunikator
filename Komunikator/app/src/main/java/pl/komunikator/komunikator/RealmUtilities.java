package pl.komunikator.komunikator;

import io.realm.Realm;
import io.realm.RealmList;
import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.User;

/**
 * Created by adrian on 24.05.2017.
 */

public class RealmUtilities {

    private Realm mRealm = Realm.getDefaultInstance();

    public User getUser(long id) {
        return mRealm.where(User.class).equalTo("id", id).findFirst();
    }

    public void createConversation(RealmList<User> users) {
        Number id = getConversationMaxId();

        mRealm.beginTransaction();
        Conversation conversation = mRealm.createObject(Conversation.class, (id == null) ? 1 : id.longValue() + 1);
        conversation.setUsers(users);
        assignConversation(conversation, users);
        mRealm.commitTransaction();
    }

    private Number getConversationMaxId() {
        return mRealm.where(Conversation.class).max("id");
    }

    private void assignConversation(Conversation conversation, RealmList<User> users) {
        for (User u : users) {
            try {
                RealmList<Conversation> userConversations = u.getConversations();
                userConversations.add(conversation);

            } catch (NullPointerException e) {
                u.setConversations(new RealmList<>(conversation));
            }
        }
    }
}
