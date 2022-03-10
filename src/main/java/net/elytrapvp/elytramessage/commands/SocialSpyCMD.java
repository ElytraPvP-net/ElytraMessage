package net.elytrapvp.elytramessage.commands;

import net.elytrapvp.elytramessage.ElytraMessage;
import net.elytrapvp.elytramessage.utils.ChatUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * This command runs the social spy command, which allows staff to listen in on private messages to catch rule breakers.
 */
public class SocialSpyCMD extends Command {
    private final ElytraMessage plugin;

    /**
     * Creates the command /socialspy with the permission staff.spy and aliases /spy and /ss.
     * @param plugin Instance of the plugin.
     */
    public SocialSpyCMD(ElytraMessage plugin) {
        super("socialspy", "staff.spy", "spy", "ss");
        this.plugin = plugin;
    }

    /**
     * This is the code that runs when the command is sent.
     * @param sender The player (or console) that sent the command.
     * @param args The arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        // Only players should be able to message each other.
        if(!(sender instanceof ProxiedPlayer player)) {
            return;
        }

        // Toggles social spy for the player.
        plugin.getMessageManager().toggleSocialSpy(player);

        // Sends a message to the player telling them what they did.
        if(plugin.getMessageManager().isSpying(player)) {
            ChatUtils.chat(player, "&a&lChat &8» &aYou have enabled social spy.");
        }
        else {
            ChatUtils.chat(player, "&a&lChat &8» &aYou have disabled social spy.");
        }
    }
}
