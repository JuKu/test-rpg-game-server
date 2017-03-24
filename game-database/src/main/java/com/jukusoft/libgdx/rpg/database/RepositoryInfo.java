package com.jukusoft.libgdx.rpg.database;

/**
 * Created by Justin on 10.01.2017.
 */
public @interface RepositoryInfo {

    enum DB {
        HAZELCAST, CASSANDRA
    }

    DB[] db();

}
