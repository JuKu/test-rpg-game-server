package com.jukusoft.libgdx.rpg.game.client.listener;

import com.jukusoft.libgdx.rpg.game.client.GameClient;
import com.jukusoft.libgdx.rpg.network.channel.ChannelAttributes;

/**
 * Created by Justin on 25.03.2017.
 */
public interface TickListener {

    public void onTick (GameClient client, ChannelAttributes attributes);

}
