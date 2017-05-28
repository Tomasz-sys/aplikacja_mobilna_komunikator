package pl.komunikator.komunikator.entity;


import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Tenek on 11.04.2017.
 */

public class Conversation extends RealmObject {
    @PrimaryKey
    private Long id;
    private String name;
    private Date createDate;
    private RealmList<User> users;
    private RealmList<Message> messages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public RealmList<User> getUsers() {
        if (users == null) return new RealmList<>();
        return users;
    }

    public void setUsers(RealmList<User> users) {
        this.users = users;
    }

    public RealmList<Message> getMessages() {

        if (messages == null) return new RealmList<>();
        return messages;
    }

    public void setMessages(RealmList<Message> messages) {
        this.messages = messages;
    }
}
