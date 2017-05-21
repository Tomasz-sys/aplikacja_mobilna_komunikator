package pl.komunikator.komunikator.entity;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Tenek on 11.04.2017.
 */

public class User extends RealmObject implements Comparable {
    @Ignore
    private static User instance = null;
    @PrimaryKey
    private Long id;
    private String username;
    private String password;
    private Date birthDate;
    private Long latitude;
    private Long Longitude;
    private RealmList<Conversation> conversations;
    private String email;

    public RealmList<User> friends;

    public static User getLoggedUser() {
        return instance;
    }

    public static void setLoggedUser(User user) {
        instance = user;
    }

    public static void logout() {
        instance = null;
    }

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

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return Longitude;
    }

    public void setLongitude(Long longitude) {
        Longitude = longitude;
    }

    public RealmList<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(RealmList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        try {
            User user = (User) o;
            if (id < user.id) {
                return -1;
            } else if (id > user.id) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw e;
        }
    }

}