package pl.komunikator.komunikator;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.User;

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
        bindObjects(conversation, users);
        setConversationName(conversation);

        mRealm.commitTransaction();

        return conversation;
    }

    private Number getConversationMaxId() {
        return mRealm.where(Conversation.class).max("id");
    }

    public void bindObjects(Conversation conversation, List<Long> ids) {
        RealmList<User> users = new RealmList<>();
        for (Long id : ids) {
            users.add(getUser(id));
        }

        mRealm.beginTransaction();

        bindObjects(conversation, users);
        assignUsersTo(conversation, users);
        setConversationName(conversation);

        mRealm.commitTransaction();
    }

    private void assignUsersTo(Conversation conversation, RealmList<User> users) {
        conversation.getUsers().addAll(users);
    }

    private void bindObjects(Conversation conversation, RealmList<User> users) {
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

    public RealmList<User> getFilteredUserContacts(RealmList<User> usersToDrop) {
        RealmQuery<User> friendQuery = RealmQuery.createQueryFromList(User.getLoggedUser().friends);
        for (User user : usersToDrop) {
            long userId = user.getId();

            friendQuery.notEqualTo("id", userId);
        }

        RealmResults<User> filteredContactsResults = friendQuery.findAll();
        RealmList<User> filteredContacts = new RealmList<>();
        filteredContacts.addAll(filteredContactsResults.subList(0, filteredContactsResults.size()));
        return filteredContacts;
    }

}
