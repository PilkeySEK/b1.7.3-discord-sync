package me.pilkeysek.discord;

import me.pilkeysek.DiscordSyncPlugin;
import me.pilkeysek.MessagingUtil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
    MessagingUtil.sendMessageD2M(message, username);
  }
}
