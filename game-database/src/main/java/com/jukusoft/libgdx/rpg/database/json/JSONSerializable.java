package com.jukusoft.libgdx.rpg.database.json;

import org.json.JSONObject;

/**
 * Created by Justin on 24.03.2017.
 */
public interface JSONSerializable {

    public JSONObject toJSON ();

    public void loadFromJSON (JSONObject json);

}
