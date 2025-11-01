package me.pilkeysek.event;

import me.pilkeysek.ConfigurationUtil;
import me.pilkeysek.DiscordSyncPlugin;
import me.pilkeysek.MessagingUtil;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerJoinEventListener extends PlayerListener {
  @Override
  public void onPlayerJoin(PlayerJoinEvent event) {
    if (!DiscordSyncPlugin.instance.config.getBoolean(ConfigurationUtil.KEY_PLUGIN_ENABLED, true))
      return;
    if (!DiscordSyncPlugin.instance.config.getBoolean(ConfigurationUtil.KEY_WEBHOOK_ENABLED, true))
      return;
    MessagingUtil.sendPlayerJoinMessage(event.getPlayer());
  }
}
