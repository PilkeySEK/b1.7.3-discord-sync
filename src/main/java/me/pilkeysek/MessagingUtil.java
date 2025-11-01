package me.pilkeysek;

import com.eduardomcb.discord.webhook.WebhookClient;
import com.eduardomcb.discord.webhook.WebhookManager;
import com.eduardomcb.discord.webhook.models.Embed;
import com.eduardomcb.discord.webhook.models.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessagingUtil {
  public static String mhfSteveUUID = "c06f89064c8a49119c29ea1dbd1aab82";

  public static void sendMessageM2D(String message, Player sender) {
    String avatarURL;
    if (DiscordSyncPlugin.instance.config.getBoolean(
        ConfigurationUtil.KEY_CHAT_HEADS_ENABLED, true)) {
      avatarURL = "https://mc-heads.net/avatar/" + sender.getName().toLowerCase();
    } else {
      avatarURL = "https://mc-heads.net/avatar/" + mhfSteveUUID;
    }
    Message discordMessage =
        new Message()
            .setContent(filterMinecraftMessage(message))
            .setAvatarUrl(avatarURL)
            .setUsername(sender.getName());
    // using a thread to send the request to avoid blocking the entire server each time
    new MessageSenderThread(
            discordMessage,
            DiscordSyncPlugin.instance.config.getString(ConfigurationUtil.KEY_WEBHOOK_URL))
        .start();
  }

  public static void sendMessageD2M(String message, String sender) {
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
                + sender
                + ChatColor.GRAY
                + ": "
                + ChatColor.WHITE
                + filterDiscordMessage(message));
  }

  public static void sendPlayerJoinMessage(Player player) {
    String avatarURL;
    if (DiscordSyncPlugin.instance.config.getBoolean(
        ConfigurationUtil.KEY_CHAT_HEADS_ENABLED, true)) {
      avatarURL = "https://mc-heads.net/avatar/" + player.getName().toLowerCase();
    } else {
      avatarURL = "https://mc-heads.net/avatar/" + mhfSteveUUID;
    }
    Embed joinMessage =
        new Embed()
            .setDescription("**" + player.getName() + "** joined the game.")
            .setColor(rgbToDecimal(0x21, 0xCE, 0x00));
    // using a thread to send the request to avoid blocking the entire server each time
    new MessageSenderThread(
            joinMessage,
            new Message().setAvatarUrl(avatarURL).setUsername(player.getName()),
            DiscordSyncPlugin.instance.config.getString(ConfigurationUtil.KEY_WEBHOOK_URL))
        .start();
  }

  public static void sendPlayerQuitMessage(Player player) {
    String avatarURL;
    if (DiscordSyncPlugin.instance.config.getBoolean(
        ConfigurationUtil.KEY_CHAT_HEADS_ENABLED, true)) {
      avatarURL = "https://mc-heads.net/avatar/" + player.getName().toLowerCase();
    } else {
      avatarURL = "https://mc-heads.net/avatar/" + mhfSteveUUID;
    }
    Embed joinMessage =
        new Embed()
            .setDescription("**" + player.getName() + "** left the game.")
            .setColor(rgbToDecimal(0xAC, 0x00, 0x00));
    // using a thread to send the request to avoid blocking the entire server each time
    new MessageSenderThread(
            joinMessage,
            new Message().setAvatarUrl(avatarURL).setUsername(player.getName()),
            DiscordSyncPlugin.instance.config.getString(ConfigurationUtil.KEY_WEBHOOK_URL))
        .start();
  }

  private static int rgbToDecimal(int r, int g, int b) {
    return (r << 16) + (g << 8) + b;
  }

  private static String filterMinecraftMessage(String minecraftMessage) {
    String filteredMessage = minecraftMessage;
    // yes i know this is a bit aggressive but i don't want to implement a better system rn
    filteredMessage = filteredMessage.replace("@", "[at]");
    return filteredMessage;
  }

  private static String filterDiscordMessage(String discordMessage) {
    String filteredMessage = discordMessage;
    boolean colorCodesEnabled =
        DiscordSyncPlugin.instance.config.getBoolean(
            ConfigurationUtil.KEY_BOT_COLOR_CODES_ENABLED, true);
    if (!colorCodesEnabled) {
      filteredMessage = filteredMessage.replace("§", "");
    }
    return filteredMessage;
  }

  private static class MessageSenderThread extends Thread {
    private Message message = null;
    private Embed embed = null;
    private String webhookURL;

    public MessageSenderThread(Message message, String webhookURL) {
      this.message = message;
      this.webhookURL = webhookURL;
    }

    public MessageSenderThread(Embed embed, Message message, String webhookURL) {
      this.embed = embed;
      this.message = message;
      this.webhookURL = webhookURL;
    }

    @Override
    public void run() {
      WebhookManager manager = new WebhookManager().setChannelUrl(webhookURL);
      if (this.embed != null) {
        Embed[] embeds = {this.embed};
        manager.setEmbeds(embeds);
      }
      manager.setMessage(message);
      manager.setListener(
          new WebhookClient.Callback() {
            @Override
            public void onSuccess(String response) {}

            @Override
            public void onFailure(int statusCode, String errorMessage) {
              DiscordSyncPlugin.instance.logInfo(
                  "Failed to send Webhook message: status="
                      + statusCode
                      + ", error: "
                      + errorMessage);
            }
          });
      manager.exec();
    }
  }
}
