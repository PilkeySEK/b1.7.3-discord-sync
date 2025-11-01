package me.pilkeysek.discord;

import me.pilkeysek.ConfigurationUtil;
import me.pilkeysek.DiscordSyncPlugin;
import me.pilkeysek.MessagingUtil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEventListener extends ListenerAdapter {
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (!DiscordSyncPlugin.instance.config.getBoolean(ConfigurationUtil.KEY_PLUGIN_ENABLED, true))
      return;
    User author = event.getAuthor();
    if (author.isBot()) return;
    MessageChannelUnion channel = event.getChannel();
    String message = event.getMessage().getContentDisplay();
    String botMessageChannel =
        DiscordSyncPlugin.instance.config.getString(ConfigurationUtil.KEY_BOT_CHANNEL);
    if (botMessageChannel == null) {
      DiscordSyncPlugin.instance.logInfo("The channel to check messages for is not defined.");
      return;
    }
    if (!channel.getId().equals(botMessageChannel)) return;
    String username;
    if (DiscordSyncPlugin.instance
        .config
        .getString(ConfigurationUtil.KEY_BOT_NAME_DISPLAY_TYPE)
        .equals("display_name")) {
      username = author.getGlobalName();
      if (username == null) {
        username = author.getName();
      } else {
        username = author.getEffectiveName();
      }
    } else {
      username = author.getName();
    }
    MessagingUtil.sendMessageD2M(message, username);
  }
}
