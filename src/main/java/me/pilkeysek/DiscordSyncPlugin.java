package me.pilkeysek;

import com.eduardomcb.discord.webhook.WebhookClient;
import com.eduardomcb.discord.webhook.WebhookManager;
import java.util.EnumSet;
import java.util.Map;
import me.pilkeysek.discord.MessageEventListener;
import me.pilkeysek.event.PlayerChatEventListener;
import me.pilkeysek.event.PlayerJoinEventListener;
import me.pilkeysek.event.PlayerQuitEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.JDALogger;
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
  public JDA jda = null;

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
    JDALogger.setFallbackLoggerEnabled(false);
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
    getServer()
        .getPluginManager()
        .registerEvent(Type.PLAYER_JOIN, new PlayerJoinEventListener(), Priority.Normal, this);
    getServer()
        .getPluginManager()
        .registerEvent(Type.PLAYER_QUIT, new PlayerQuitEventListener(), Priority.Normal, this);
    this.reload();
  }

  public void reload() {
    this.config.load();
    if (this.jda != null) {
      this.jda.shutdownNow();
    }
    if (this.config.getBoolean(ConfigurationUtil.KEY_BOT_ENABLED, false)) {
      this.jda =
          JDABuilder.createLight(
                  this.config.getString(ConfigurationUtil.KEY_BOT_TOKEN),
                  EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
              .addEventListeners(new MessageEventListener())
              .build();
    }
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
    Map<String, Object> defaultConfig = ConfigurationUtil.getDefaultConfigKV();
    defaultConfig.forEach(
        (key, value) -> {
          Object prop = this.config.getProperty(key);
          if (prop == null) {
            this.config.setProperty(key, value);
          }
        });
    this.config.save();
  }
}
