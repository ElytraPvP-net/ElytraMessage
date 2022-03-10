package net.elytrapvp.elytramessage.listeners;

import net.elytrapvp.elytramessage.ElytraMessage;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * This listens to the PlayerDisconnectEvent event, which is called every time a player leaves the server.
 * We use this to remove conversations when a player leaves.
 */
public class PlayerDisconnectListener implements Listener {
    private final ElytraMessage plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * This is known as Dependency Injection.
     * @param plugin Instance of the plugin.
     */
    public PlayerDisconnectListener(ElytraMessage plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event PlayerDisconnectEvent.
     */
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        // Removes the player from conversations.
        plugin.getMessageManager().removePlayer(event.getPlayer());
    }
}
