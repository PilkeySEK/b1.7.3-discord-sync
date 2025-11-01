package me.pilkeysek;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class DiscordSyncCommand implements CommandExecutor {
  private final String MSG_SPECIFY_MORE_ARGUMENTS = ChatColor.RED + "Specify more arguments.";

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

    if (args[0].equals("config")) {
      if (!sender.hasPermission("discordsync.config")
          && !(sender instanceof ConsoleCommandSender)
          && !sender.isOp()) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to configure DiscordSync.");
        return true;
      }
      if (args.length < 2) {
        sender.sendMessage(MSG_SPECIFY_MORE_ARGUMENTS);
        return true;
      }
      String configKey = args[2];
      if (args[1].equals("remove")) {
        if (args.length != 3) {
          sender.sendMessage(MSG_SPECIFY_MORE_ARGUMENTS);
          return true;
        }
        if (DiscordSyncPlugin.instance.config.getProperty(configKey) == null) {
          sender.sendMessage(
              ChatColor.RED + "The configuration does not contain a property '" + configKey + "'.");
          return true;
        }
        DiscordSyncPlugin.instance.config.removeProperty(configKey);
        DiscordSyncPlugin.instance.config.save();
        sender.sendMessage(ChatColor.GREEN + "Removed key '" + configKey + "' from the config");
        return true;
      } else if (args[1].equals("set")) {
        if (args.length < 4) {
          sender.sendMessage(MSG_SPECIFY_MORE_ARGUMENTS);
          return true;
        }
        String prop =
            String.join(" ", (new ArrayList<>(Arrays.asList(args))).subList(3, args.length));
        // this tries to see if there are booleans or integers. if yes, it sets
        // them as their type instead of string
        if (prop.equalsIgnoreCase("true") || prop.equalsIgnoreCase("false")) {
          DiscordSyncPlugin.instance.config.setProperty(configKey, Boolean.parseBoolean(prop));
        } else {
          try {
            int x = Integer.parseInt(prop);
            DiscordSyncPlugin.instance.config.setProperty(configKey, x);
          } catch (NumberFormatException e) {
            DiscordSyncPlugin.instance.config.setProperty(configKey, prop);
          }
        }
        DiscordSyncPlugin.instance.config.save();
        sender.sendMessage(ChatColor.GREEN + "Set property '" + configKey + "'.");
        return true;
      } else if (args[1].equals("get")) {
        if (args.length != 3) {
          sender.sendMessage(MSG_SPECIFY_MORE_ARGUMENTS);
          return true;
        }
        Object prop = DiscordSyncPlugin.instance.config.getProperty(configKey);
        if (prop == null) {
          sender.sendMessage(ChatColor.GREEN + "'" + configKey + "' is not set.");
          return true;
        }
        sender.sendMessage(ChatColor.GREEN + prop.toString());
        return true;
      } else {
        sender.sendMessage(
            ChatColor.RED + "Unrecognized argument: " + ChatColor.DARK_AQUA + args[1]);
        return true;
      }
    } else if (args[0].equals("reload")) {
      DiscordSyncPlugin.instance.reload();
      sender.sendMessage(ChatColor.GREEN + "Reloaded DiscordSync.");
      return true;
    }
    sender.sendMessage(ChatColor.RED + "Unrecognized argument: " + ChatColor.DARK_AQUA + args[0]);
    return true;
  }
}
