package pl.komunikator.komunikator;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.User;

import static android.R.attr.id;

/**
 * Created by adrian on 24.05.2017.
 */

public class RealmUtilities {

    private Realm mRealm = Realm.getDefaultInstance();

    public User getUser(long id) {
        return (User) getObject(id, User.class);
    }

    public Conversation getConversation(long id) {
        return (Conversation) getObject(id, Conversation.class);
    }

    private RealmModel getObject(long id, Class type) {
        return mRealm.where(type).equalTo("id", id).findFirst();
    }

    public Conversation createConversation(RealmList<User> users) {
        Number id = getConversationMaxId();

        mRealm.beginTransaction();

        Conversation conversation = mRealm.createObject(Conversation.class, (id == null) ? 1 : id.longValue() + 1);
        conversation.setUsers(users);
        assignConversation(conversation, users);
        setConversationName(conversation);

        mRealm.commitTransaction();

        return conversation;
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

    private void setConversationName(Conversation conversation) {
        StringBuilder result = new StringBuilder();
        for(User user : conversation.getUsers()) {
            result.append(user.getUsername());
            result.append(", ");
        }
        String usersText = result.length() > 0 ? result.substring(0, result.length() - 2) : "";
        conversation.setName(usersText);
    }

    public Conversation createConversation(List<Long> usersIds) {
        RealmList<User> users = new RealmList<>();

        for (Long id : usersIds) {
            users.add(getUser(id));
        }

        return createConversation(users);
    }

}
