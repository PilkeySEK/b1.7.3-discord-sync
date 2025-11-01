package me.pilkeysek.event;

import me.pilkeysek.DiscordSyncPlugin;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class CommandPreprocessEventListener extends PlayerListener {
  @Override
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    String command = event.getMessage();
    if (command.toLowerCase().startsWith("/reload")) {
      try {
        char c = command.charAt("/reload".length());
        if (c != ' ') return;
      } catch (IndexOutOfBoundsException e) {
        /* this exception is to be expected */
      }
      DiscordSyncPlugin.instance.jda.shutdownNow();
      DiscordSyncPlugin.instance.logInfo("Shutting down JDA due to /reload.");
    }
  }
}
