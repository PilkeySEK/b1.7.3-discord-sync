package me.pilkeysek;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class DiscordSyncCommand implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(
          ChatColor.AQUA
              + "DiscordSync version "
              + ChatColor.DARK_AQUA
              + DiscordSyncPlugin.VERSION);
      return true;
    }

    if (args.length == 1) {
      return false;
    }

    if (args.length >= 2) {
      if (args[0].equals("config")) {
        if (!sender.hasPermission("discordsync.config")
            && !(sender instanceof ConsoleCommandSender)) {
          sender.sendMessage(
              ChatColor.RED + "You do not have permission to configure DiscordSync.");
          return true;
        }
        if (args[1].equals("enable")) {
          DiscordSyncPlugin.instance.config.setProperty("enabled", true);
          sender.sendMessage(
              ChatColor.GREEN
                  + "Enabled DiscordSync. You may need to also set the Webhook URL with "
                  + ChatColor.DARK_AQUA
                  + "/discordsync config webhookURL <url>"
                  + ChatColor.GREEN
                  + ".");
          DiscordSyncPlugin.instance.config.save();
          return true;
        }
        if (args[1].equals("disable")) {
          DiscordSyncPlugin.instance.config.setProperty("enabled", false);
          sender.sendMessage(ChatColor.GREEN + "Disabled DiscordSync.");
          DiscordSyncPlugin.instance.config.save();
          return true;
        }
        if (args[1].equals("enableBot")) {
          DiscordSyncPlugin.instance.config.setProperty("enableBot", true);
          sender.sendMessage(ChatColor.GREEN + "Enabled Discord Bot.");
          DiscordSyncPlugin.instance.config.save();
          return true;
        }
        if (args[1].equals("disableBot")) {
          DiscordSyncPlugin.instance.config.setProperty("enableBot", false);
          sender.sendMessage(ChatColor.GREEN + "Disabled Discord Bot.");
          DiscordSyncPlugin.instance.config.save();
          return true;
        }
        if (args[1].equals("enableColorCodes")) {
          DiscordSyncPlugin.instance.config.setProperty("enabeColorCodes", true);
          sender.sendMessage(ChatColor.GREEN + "Enabled color codes.");
          DiscordSyncPlugin.instance.config.save();
          return true;
        }
        if (args[1].equals("disableColorCodes")) {
          DiscordSyncPlugin.instance.config.setProperty("enabeColorCodes", false);
          sender.sendMessage(ChatColor.GREEN + "Disabled color codes.");
          DiscordSyncPlugin.instance.config.save();
          return true;
        }
        if (args[1].equals("reload")) {
          DiscordSyncPlugin.instance.config.load();
          DiscordSyncPlugin.instance.reload();
          sender.sendMessage(ChatColor.GREEN + "Reloaded config and plugin.");
          return true;
        }
        if (args[1].equals("webhookURL")) {
          if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Specify more arguments.");
            return false;
          }
          String url = args[2];
          DiscordSyncPlugin.instance.config.setProperty("webhookURL", url);
          sender.sendMessage(
              ChatColor.GREEN + "Set the Webhook URL to: " + ChatColor.DARK_AQUA + url);
          DiscordSyncPlugin.instance.config.save();
          return true;
        }
        if (args[1].equals("botToken")) {
          if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Specify more arguments.");
            return false;
          }
          String token = args[2];
          DiscordSyncPlugin.instance.config.setProperty("botToken", token);
          sender.sendMessage(ChatColor.GREEN + "Set the bot token.");
          DiscordSyncPlugin.instance.config.save();
          return true;
        }
        if (args[1].equals("botMessageChannel")) {
          if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Specify more arguments.");
            return false;
          }
          String channel = args[2];
          DiscordSyncPlugin.instance.config.setProperty("botMessageChannel", channel);
          sender.sendMessage(
              ChatColor.GREEN + "Set the bot channel to: " + ChatColor.DARK_AQUA + channel);
          DiscordSyncPlugin.instance.config.save();
          return true;
        }
        sender.sendMessage(
            ChatColor.RED + "Unrecognized argument: " + ChatColor.DARK_AQUA + args[1]);
        return false;
      }
      sender.sendMessage(ChatColor.RED + "Unrecognized argument: " + ChatColor.DARK_AQUA + args[0]);
      return false;
    }
    return true;
  }
}
