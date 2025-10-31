package me.pilkeysek;

import com.eduardomcb.discord.webhook.WebhookClient;
import com.eduardomcb.discord.webhook.WebhookManager;
import java.util.List;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class DiscordSyncPlugin extends JavaPlugin {
  public static DiscordSyncPlugin instance;
  public Configuration config;
  public static final String VERSION = "1.0";
  public WebhookManager webhookManager;
  public static final String WEBHOOK_DEFAULT_VALUE = "https://discord.com/api/webhooks/...";

  public DiscordSyncPlugin() {
    try {
      // just to make sure
      Class.forName("com.eduardomcb.discord.webhook.WebhookManager");
      Class.forName("com.eduardomcb.discord.webhook.models.Message");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    DiscordSyncPlugin.instance = this;
  }

  @Override
  public void onEnable() {
    this.config = getConfiguration();
    this.config.load();
    setDefaultConfigValues();
    getCommand("discordsync").setExecutor(new DiscordSyncCommand());
    this.webhookManager = new WebhookManager();
    this.webhookManager.setListener(
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
    getServer()
        .getPluginManager()
        .registerEvent(Type.PLAYER_CHAT, new PlayerChatEventListener(), Priority.Normal, this);
  }

  @Override
  public void onDisable() {
    this.config.save();
    logInfo("Bye!");
  }

  // Log with prefix
  public void logInfo(String s) {
    getServer().getLogger().info("[DiscordSync] " + s);
  }

  public void setDefaultConfigValues() {
    List<String> configKeys = this.config.getKeys();
    if (!configKeys.contains("webhookURL")) {
      this.config.setProperty("webhookURL", WEBHOOK_DEFAULT_VALUE);
    }
    if (!configKeys.contains("enabled")) {
      this.config.setProperty("enabled", true);
    }
    this.config.save();
  }
}
