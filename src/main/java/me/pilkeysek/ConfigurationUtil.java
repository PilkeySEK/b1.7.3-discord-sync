package me.pilkeysek;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationUtil {
  public static String KEY_WEBHOOK_URL = "webhook.url";
  public static String KEY_WEBHOOK_ENABLED = "webhook.enabled";
  public static String KEY_WEBHOOK_HIDE_MESSAGE_PREFIX = "webhook.hide_message_prefix";
  public static String KEY_PLUGIN_ENABLED = "plugin_enabled";
  public static String KEY_CHAT_HEADS_ENABLED = "chat_heads.enabled";
  public static String KEY_BOT_ENABLED = "bot.enabled";
  public static String KEY_BOT_TOKEN = "bot.token";
  public static String KEY_BOT_CHANNEL = "bot.channel";
  public static String KEY_BOT_COLOR_CODES_ENABLED = "bot.color_codes";
  public static String KEY_BOT_NAME_DISPLAY_TYPE = "bot.name_display_type";
  public static String KEY_BOT_COMMAND_PREFIXES = "bot.command_prefixes";

  public static Map<String, Object> getDefaultConfigKV() {
    Map<String, Object> map = new HashMap<>();
    map.put(KEY_WEBHOOK_URL, DiscordSyncPlugin.WEBHOOK_DEFAULT_VALUE);
    map.put(KEY_WEBHOOK_ENABLED, true);
    map.put(KEY_WEBHOOK_HIDE_MESSAGE_PREFIX, "::");
    map.put(KEY_PLUGIN_ENABLED, true);
    map.put(KEY_CHAT_HEADS_ENABLED, true);
    map.put(KEY_BOT_ENABLED, false);
    map.put(KEY_BOT_TOKEN, "");
    map.put(KEY_BOT_CHANNEL, "");
    map.put(KEY_BOT_COLOR_CODES_ENABLED, true);
    map.put(KEY_BOT_NAME_DISPLAY_TYPE, "username");
    map.put(KEY_BOT_COMMAND_PREFIXES, Arrays.asList("mc!", "mc:"));
    return map;
  }
}
