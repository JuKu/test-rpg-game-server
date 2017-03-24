package com.jukusoft.libgdx.rpg.database.user.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IdGenerator;
import com.jukusoft.libgdx.rpg.database.exception.UserAlreadyExistsException;
import com.jukusoft.libgdx.rpg.database.user.UserEntry;
import com.jukusoft.libgdx.rpg.database.user.UserRepository;
import org.json.JSONObject;

/**
 * Created by Justin on 24.03.2017.
 */
public class UserRepositoryImpl implements UserRepository {

    protected IdGenerator userIDGenerator = null;

    //map userID --> user data
    protected IMap<Long,String> userMap = null;

    //map username --> userID
    protected IMap<String,Long> usernameMap = null;

    public UserRepositoryImpl (HazelcastInstance hazelcastInstance) {
        this.userIDGenerator = hazelcastInstance.getIdGenerator("user-id-generator");
        this.userMap = hazelcastInstance.getMap("user-json-map");
        this.usernameMap = hazelcastInstance.getMap("username-to-userID");
    }

    @Override public void add(long userID, UserEntry user) throws UserAlreadyExistsException {
        if (this.userMap.containsKey(userID)) {
            throw new UserAlreadyExistsException("user '" + user.getUsername() + "' already exists!");
        }

        if (user.getUserID() != userID) {
            throw new IllegalStateException("userID of user object and id parameter has to be equals.");
        }

        //convert user into json string and put to map
        this.userMap.putAsync(userID, user.toJSON().toString());
        this.usernameMap.putAsync(user.getUsername(), userID);
    }

    @Override public void remove(long userID) {
        this.userMap.removeAsync(userID);
    }

    @Override public UserEntry findUserByID(long userID) {
        if (!this.userMap.containsKey(userID)) {
            //user doesnt exists
            return null;
        }

        //get json string
        String str = this.userMap.get(userID);

        if (str == null || str.isEmpty()) {
            //user doesnt exists
            return null;
        }

        //convert string to json object
        JSONObject json = new JSONObject(str);

        //create user entry from json
        return UserEntry.createFromJSON(json);
    }

    @Override public UserEntry findUserByName(String username) {
        //check, if username exists
        if (!this.usernameMap.containsKey(username)) {
            return null;
        }

        //get userID
        long userID = this.usernameMap.get(username);

        return this.findUserByID(userID);
    }

    @Override public long generateUserID() {
        long userID = this.userIDGenerator.newId();

        while (userID <= 0) {
            userID = this.userIDGenerator.newId();
        }

        return userID;
    }

}
