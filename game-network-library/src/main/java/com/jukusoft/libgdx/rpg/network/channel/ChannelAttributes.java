package com.jukusoft.libgdx.rpg.network.channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Justin on 23.03.2017.
 */
public class ChannelAttributes {

    protected final long connID;

    //map with attributes
    protected Map<String,Object> attrMap = new ConcurrentHashMap<>();

    protected AtomicBoolean isAuth = new AtomicBoolean(false);

    public ChannelAttributes (long connID) {
        this.connID = connID;
    }

    public long getChannelID () {
        return this.connID;
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

    public void setAuth () {
        this.isAuth.set(true);
    }

    public boolean isAuth () {
        return this.isAuth.get();
    }

    public void logout () {
        this.isAuth.set(false);
    }

}
