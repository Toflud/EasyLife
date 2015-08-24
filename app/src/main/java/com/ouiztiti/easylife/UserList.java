package com.ouiztiti.easylife;

import java.util.List;

/**
 * Created by cpe on 24/06/2015.
 */
public class UserList {

    private String userId;
    private String listeName;
    private List<UserIntent> liste;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getListeName() {
        return listeName;
    }

    public void setListeName(String listeName) {
        this.listeName = listeName;
    }

    public List<UserIntent> getListe() {
        return liste;
    }

    public void setListe(List<UserIntent> liste) {
        this.liste = liste;
    }
}
