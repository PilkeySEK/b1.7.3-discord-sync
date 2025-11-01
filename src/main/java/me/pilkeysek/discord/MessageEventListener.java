package me.pilkeysek.discord;

import java.util.ArrayList;
import java.util.List;
import me.pilkeysek.ConfigurationUtil;
import me.pilkeysek.DiscordSyncPlugin;
import me.pilkeysek.MessagingUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.bukkit.entity.Player;

public class MessageEventListener extends ListenerAdapter {
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    System.out.println("Message");
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
    List<Object> prefixes =
        DiscordSyncPlugin.instance.config.getList(ConfigurationUtil.KEY_BOT_COMMAND_PREFIXES);
    String rawMessage = event.getMessage().getContentRaw();
    for (Object prefix : prefixes) {
      if (!rawMessage.startsWith(prefix.toString())) continue;
      String command = rawMessage.substring(prefix.toString().length());
      if (command.equals("list")) {
        List<String> players = new ArrayList<>();
        for (Player player : DiscordSyncPlugin.instance.getServer().getOnlinePlayers()) {
          players.add(player.getName());
        }
        if (players.isEmpty()) {
          event
              .getMessage()
              .reply(
                  MessageCreateData.fromEmbeds(
                      new EmbedBuilder()
                          .setDescription("There are no players online.")
                          .setColor(0xFFFFFF)
                          .build()))
              .mentionRepliedUser(false)
              .submit();
          return;
        }
        String playersStr = "* **" + String.join("**\n* **", players) + "**";
        event
            .getMessage()
            .reply(
                MessageCreateData.fromEmbeds(
                    new EmbedBuilder()
                        .setDescription(
                            (players.size() == 1
                                    ? "There is 1 player online:"
                                    : "There are " + players.size() + " players online:")
                                + "\n"
                                + playersStr)
                        .setColor(0xFFFFFF)
                        .build()))
            .mentionRepliedUser(false)
            .submit();
        return;
      }
    }
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
