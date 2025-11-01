package me.pilkeysek.event;

import me.pilkeysek.ConfigurationUtil;
import me.pilkeysek.DiscordSyncPlugin;
import me.pilkeysek.MessagingUtil;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener extends PlayerListener {
  @Override
  public void onPlayerQuit(PlayerQuitEvent event) {
    if (!DiscordSyncPlugin.instance.config.getBoolean(ConfigurationUtil.KEY_PLUGIN_ENABLED, true))
      return;
    if (!DiscordSyncPlugin.instance.config.getBoolean(ConfigurationUtil.KEY_WEBHOOK_ENABLED, true))
      return;
    MessagingUtil.sendPlayerQuitMessage(event.getPlayer());
  }
}
