package net.elytrapvp.elytramessage;

import net.elytrapvp.elytramessage.commands.MessageCMD;
import net.elytrapvp.elytramessage.commands.ReplyCMD;
import net.elytrapvp.elytramessage.commands.SocialSpyCMD;
import net.elytrapvp.elytramessage.listeners.PlayerDisconnectListener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * This plugin allows players to privately message each other across a BungeeCord network.
 * It also allows staff to listen in to private messages to look for rule breakers
 * and logs all messages to MySQL.
 */
public final class ElytraMessage extends Plugin {
    private SettingsManager settingsManager;
    private MessageManager messageManager;
    private MySQL mySQL;

    /**
     * This is called when BungeeCord first loads the plugin.
     */
    @Override
    public void onEnable() {
        // Creates or Loads the configuration file.
        settingsManager = new SettingsManager(this);

        // Connects to the mysql database.
        mySQL = new MySQL(this);
        // Connection is opened async.
        getProxy().getScheduler().runAsync(this, () -> mySQL.openConnection());

        // Creates the message manager.
        messageManager = new MessageManager();

        // We need to tell BungeeCord that our listeners exist for them to work.
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener(this));

        // We must also tell BungeeCord that our commands exist.
        getProxy().getPluginManager().registerCommand(this, new MessageCMD(this));
        getProxy().getPluginManager().registerCommand(this, new ReplyCMD(this));
        getProxy().getPluginManager().registerCommand(this, new SocialSpyCMD(this));
    }

    /**
     * Gets the Message Manager, which manages conversations between players.
     * @return Message Manager.
     */
    public MessageManager getMessageManager() {
        return messageManager;
    }

    /**
     * Be able to connect to MySQL.
     * @return MySQL.
     */
    public MySQL getMySQL() {
        return mySQL;
    }

    /**
     * Get the Settings Manager, which gives us access to the plugin Configuration.
     * @return Settings Manager.
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}