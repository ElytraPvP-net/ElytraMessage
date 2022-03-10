package net.elytrapvp.elytramessage;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class manages all active conversations. This allows /reply to work.
 */
public class MessageManager {
    private final Map<ProxiedPlayer, ProxiedPlayer> conversations = new HashMap<>();
    private final Set<ProxiedPlayer> spying = new HashSet<>();

    /**
     * Get all players with social spy enabled.
     * @return Players using social spy.
     */
    public Set<ProxiedPlayer> getSpying() {
        return spying;
    }

    /**
     * Get if a player is spying on private messages.
     * @param player Player to check.
     * @return Whether they are spying on private messages.
     */
    public boolean isSpying(ProxiedPlayer player) {
        return spying.contains(player);
    }

    /**
     * Makes it so that /r will message between 2 players..
     * @param messenger Messenger.
     * @param receiver Receiver.
     */
    public void setReplyTarget(ProxiedPlayer messenger, ProxiedPlayer receiver){
        conversations.put(messenger, receiver);
        conversations.put(receiver, messenger);
    }

    /**
     * Get who /r should message.
     * @param messenger Player who is replying.
     * @return Who to reply to.
     */
    public ProxiedPlayer getReplyTarget(ProxiedPlayer messenger){
        return conversations.get(messenger);
    }

    /**
     * Remove a player from the conversations maps.
     * This is done when they leave the server.
     * @param player Player to remove.
     */
    public void removePlayer(ProxiedPlayer player) {
        // Removes the player.
        conversations.remove(player);

        // Loops through all the other conversations.
        for(ProxiedPlayer other : conversations.keySet()) {
            // Checks if the player is the value of another player.
            if(conversations.get(other).equals(player)) {
                // If they are, removes that conversation too.
                conversations.remove(other);
            }
        }
    }

    /**
     * Toggle social spy for a player.
     * @param player Player to toggle social spy for.
     */
    public void toggleSocialSpy(ProxiedPlayer player) {
        if(isSpying(player)) {
            spying.remove(player);
        }
        else {
            spying.add(player);
        }
    }
}