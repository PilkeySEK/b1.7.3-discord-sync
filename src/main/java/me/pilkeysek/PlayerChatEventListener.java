package me.pilkeysek;

import com.eduardomcb.discord.webhook.models.Message;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerChatEventListener extends PlayerListener {
  @Override
  public void onPlayerChat(PlayerChatEvent e) {
    if (!DiscordSyncPlugin.instance.config.getBoolean("enabled", true)) return;
    Message message = new Message().setContent(e.getMessage());
    DiscordSyncPlugin.instance
        .webhookManager
        .setChannelUrl(DiscordSyncPlugin.instance.config.getString("webhookURL"))
        .setMessage(message);
    DiscordSyncPlugin.instance.webhookManager.exec();
  }
}
