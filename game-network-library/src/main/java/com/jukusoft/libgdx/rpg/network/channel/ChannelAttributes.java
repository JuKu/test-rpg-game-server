package com.jukusoft.libgdx.rpg.network.channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Justin on 23.03.2017.
 */
public class ChannelAttributes {

    protected final long connID;

    //map with attributes
    protected Map<String,Object> attrMap = new ConcurrentHashMap<>();

    public ChannelAttributes (long connID) {
        this.connID = connID;
    }

    public void put (String key, Object obj) {
        this.attrMap.put(key, obj);
    }

    public Object get (String key) {
        return this.attrMap.get(key);
    }

    public <T> T get (String key, Class<T> cls) {
        Object obj = this.attrMap.get(key);

        if (obj == null) {
            return null;
        }

        if (cls.isInstance(obj)) {
            return cls.cast(this.attrMap.get(key));
        } else {
            throw new IllegalStateException("attribute key " + key + " isnt an instance of " + cls.getName());
        }
    }

    public boolean contains (String key) {
        return this.attrMap.get(key) != null;
    }

}
