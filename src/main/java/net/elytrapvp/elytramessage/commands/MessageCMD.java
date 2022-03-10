package net.elytrapvp.elytramessage.commands;

import io.netty.util.internal.StringUtil;
import net.elytrapvp.elytramessage.ElytraMessage;
import net.elytrapvp.elytramessage.utils.ChatUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * This class runs the message command, which is used to send a private message to another player.
 */
public class MessageCMD extends Command {
    private final ElytraMessage plugin;

    /**
     * Creates the /message command with no permissions and the following aliases:
     * - /msg
     * - /m
     * - /tell
     * - /t
     * - /whipser
     * - /w
     * @param plugin Instance of the plugin.
     */
    public MessageCMD(ElytraMessage plugin) {
        super("message", "", "msg","m","tell","t","whisper","w");
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

        // Make sure they're using the command properly.
        if(args.length < 2) {
            ChatUtils.chat(player, "&c&lUsage &8» &c/msg [player] [message]");
            return;
        }

        // Gets the target player.
        ProxiedPlayer target = plugin.getProxy().getPlayer(args[0]);

        // Make sure they're online.
        if(target == null) {
            ChatUtils.chat(player, "&c&lError &8» &cThat player isn't online!");
            return;
        }

        // Make sure the target isn't the player.
        if(target.equals(player)) {
            ChatUtils.chat(player, "&c&lError &8» &cYou cannot message yourself!");
            return;
        }

        // Gets the message from the arguments by creating a new array ignoring the username and turning it into a list.
        String message = StringUtil.join(" ", Arrays.asList(Arrays.copyOfRange(args, 1, args.length))).toString();

        // Gets the message format from the config and sends it to the sender.
        String toMessage = plugin.getSettingsManager().getConfig().getString("toMessage")
                .replace("%sender%", player.getName())
                .replace("%receiver%", target.getName())
                .replace("%message%", message);
        ChatUtils.chat(player, toMessage);

        // Gets the message format from the config and sends it to the receiver.
        String fromMessage = plugin.getSettingsManager().getConfig().getString("fromMessage")
                .replace("%sender%", player.getName())
                .replace("%receiver%", target.getName())
                .replace("%message%", message);
        ChatUtils.chat(target, fromMessage);

        // Sends message to all staff online using social spy.
        String spyMessage = plugin.getSettingsManager().getConfig().getString("spyMessage")
                .replace("%sender%", player.getName())
                .replace("%receiver%", target.getName())
                .replace("%message%", message);

        for(ProxiedPlayer stalker : plugin.getMessageManager().getSpying()) {
            ChatUtils.chat(stalker, spyMessage);
        }

        // Creates a conversation between the two players so /reply works.
        plugin.getMessageManager().setReplyTarget(player, target);

        // Logs the message to the MySQL database.
        String name = player.getName();
        String uuid = player.getUniqueId().toString();
        String server = player.getServer().getInfo().getName();
        String channel = "private";

        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement("INSERT INTO chat_logs (server,channel,uuid,username,message) VALUES (?,?,?,?,?)");
                statement.setString(1, server);
                statement.setString(2, channel);
                statement.setString(3, uuid);
                statement.setString(4, name);
                statement.setString(5, "to " + target.getName() + ": " + message);
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }
}