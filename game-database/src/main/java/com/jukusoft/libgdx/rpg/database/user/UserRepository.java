package com.jukusoft.libgdx.rpg.database.user;

import com.jukusoft.libgdx.rpg.database.Repository;
import com.jukusoft.libgdx.rpg.database.exception.UserAlreadyExistsException;

/**
 * Created by Justin on 23.03.2017.
 */
public interface UserRepository extends Repository {

    public void add (long userID, UserEntry user) throws UserAlreadyExistsException;

    public void remove (long userID);

    public UserEntry findUserByID (long userID);

    public UserEntry findUserByName (String username);

    public long generateUserID ();

}
