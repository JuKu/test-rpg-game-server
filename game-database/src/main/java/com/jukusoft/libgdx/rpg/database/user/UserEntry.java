package com.jukusoft.libgdx.rpg.database.user;

import com.jukusoft.libgdx.rpg.database.json.JSONSerializable;
import org.json.JSONObject;

/**
 * Created by Justin on 23.03.2017.
 */
public class UserEntry implements JSONSerializable {

    public static final int OBJ_VERSION = 1;

    protected long userID = 0;
    protected String username = "";

    //password hash
    protected String password = "";

    public UserEntry (long userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    protected UserEntry () {
        //
    }

    public long getUserID () {
        return this.userID;
    }

    public String getUsername () {
        return this.username;
    }

    public boolean checkPassword (String password) {
        return this.password.equals(password);
    }

    @Override public JSONObject toJSON() {
        JSONObject json = new JSONObject();

        json.put("obj_version", OBJ_VERSION);
        json.put("userID", this.userID);
        json.put("username", this.username);
        json.put("password", this.password);

        return json;
    }

    @Override public void loadFromJSON(JSONObject json) {
        this.userID = json.getLong("userID");
        this.username = json.getString("username");
        this.password = json.getString("password");
    }

    public static UserEntry createFromJSON (JSONObject json) {
        UserEntry user = new UserEntry();
        user.loadFromJSON(json);

        return user;
    }

}
