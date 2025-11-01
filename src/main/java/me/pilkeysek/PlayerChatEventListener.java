package me.pilkeysek;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerChatEventListener extends PlayerListener {
  @Override
  public void onPlayerChat(PlayerChatEvent event) {
    if (!DiscordSyncPlugin.instance.config.getBoolean(ConfigurationUtil.KEY_PLUGIN_ENABLED, true))
      return;
    if (event
        .getMessage()
        .startsWith(
            DiscordSyncPlugin.instance.config.getString(
                ConfigurationUtil.KEY_WEBHOOK_HIDE_MESSAGE_PREFIX))) return;
    MessagingUtil.sendMessageM2D(event.getMessage(), event.getPlayer());
  }
}
