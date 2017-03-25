package com.jukusoft.libgdx.rpg.game.server.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Justin on 25.03.2017.
 */
public class CharacterManager {

    protected List<Character> characterList = new ArrayList<>();
    protected Map<Long,Character> characterMap = new ConcurrentHashMap<>();

    public void updatePos (long userID, String username, float x, float y, float speedX, float speedY) {
        //get character
        Character character = this.characterMap.get(userID);

        if (character == null) {
            //create new character
            character = new Character(userID, username);

            this.characterMap.put(userID, character);
        }

        //update player position
        character.updatePos(x, y, speedX, speedY);
    }

    public List<Character> listCharacters () {
        return this.characterList;
    }

}
