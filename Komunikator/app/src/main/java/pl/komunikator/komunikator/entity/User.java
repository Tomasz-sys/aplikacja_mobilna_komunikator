package pl.komunikator.komunikator.entity;

import android.location.Location;

import java.sql.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Tenek on 11.04.2017.
 */

public class User extends RealmObject {
    @PrimaryKey
    private Long id;
    private String username;
    private String password;
    private Date birthDate;
    private Location location;
    private RealmList<Conversation> conversations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public RealmList<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(RealmList<Conversation> conversations) {
        this.conversations = conversations;
    }
}
