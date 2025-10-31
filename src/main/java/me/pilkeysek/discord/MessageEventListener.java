package me.pilkeysek.discord;

import me.pilkeysek.DiscordSyncPlugin;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.ChatColor;

public class MessageEventListener extends ListenerAdapter {
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    User author = event.getAuthor();
    if (author.isBot()) return;
    MessageChannelUnion channel = event.getChannel();
    String message = event.getMessage().getContentDisplay();
    String botMessageChannel = DiscordSyncPlugin.instance.config.getString("botMessageChannel");
    if (botMessageChannel == null) {
      DiscordSyncPlugin.instance.logInfo("The channel to check messages for is not defined.");
      return;
    }
    if (!channel.getId().equals(botMessageChannel)) return;
    String username = author.getName();
    DiscordSyncPlugin.instance
        .getServer()
        .broadcastMessage(
            ChatColor.GRAY
                + "["
                + ChatColor.AQUA
                + "Discord"
                + ChatColor.GRAY
                + "] "
                + ChatColor.GOLD
                + username
                + ChatColor.GRAY
                + ": "
                + ChatColor.WHITE
                + message);
  }
}
