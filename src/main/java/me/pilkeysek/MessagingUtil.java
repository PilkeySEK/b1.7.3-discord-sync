package me.pilkeysek;

import com.eduardomcb.discord.webhook.WebhookClient;
import com.eduardomcb.discord.webhook.WebhookManager;
import com.eduardomcb.discord.webhook.models.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessagingUtil {
  public static String mhfSteveUUID = "c06f89064c8a49119c29ea1dbd1aab82";

  public static void sendMessageM2D(String message, Player sender) {
    String avatarURL;
    if (DiscordSyncPlugin.instance.config.getBoolean("enableChatHeads", true)) {
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
            discordMessage, DiscordSyncPlugin.instance.config.getString("webhookURL"))
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

  private static String filterMinecraftMessage(String minecraftMessage) {
    String filteredMessage = minecraftMessage;
    // yes i know this is a bit aggressive but i don't want to implement a better system rn
    filteredMessage = filteredMessage.replace("@", "[at]");
    return filteredMessage;
  }

  private static String filterDiscordMessage(String discordMessage) {
    String filteredMessage = discordMessage;
    boolean colorCodesEnabled =
        DiscordSyncPlugin.instance.config.getBoolean("colorCodesenabled", true);
    if (!colorCodesEnabled) {
      filteredMessage = filteredMessage.replace("§", "");
    }
    return filteredMessage;
  }

  private static class MessageSenderThread extends Thread {
    private Message message;
    private String webhookURL;

    public MessageSenderThread(Message message, String webhookURL) {
      this.message = message;
      this.webhookURL = webhookURL;
    }

    @Override
    public void run() {
      WebhookManager manager = new WebhookManager().setChannelUrl(webhookURL).setMessage(message);
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
