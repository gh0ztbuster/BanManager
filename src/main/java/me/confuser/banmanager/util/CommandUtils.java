package me.confuser.banmanager.util;

import me.confuser.banmanager.BanManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandUtils {

  public static void dispatchCommand(CommandSender sender, String command) {
    Bukkit.dispatchCommand(sender, command);
  }

  public static void dispatchCommands(CommandSender sender, List<String> commands) {
    for (String command : commands) {
      dispatchCommand(sender, command);
    }
  }

  public static void handleMultipleNames(CommandSender sender, String commandName, String[] args) {
    String[] names = splitNameDelimiter(args[0]);
    String argsStr = StringUtils.join(args, " ", 1, args.length);
    ArrayList<String> commands = new ArrayList<>(names.length);

    for (String name : names) {
      if (name.length() == 0) continue;
      commands.add(commandName + " " + name + " " + argsStr);
    }

    dispatchCommands(sender, commands);
  }

  public static boolean isValidNameDelimiter(String names) {
    return names.contains("|") || names.contains(",");
  }

  public static String[] splitNameDelimiter(String str) {
    String delimiter;

    if (str.contains("|")) {
      delimiter = "\\|";
    } else {
      delimiter = "\\,";
    }

    return str.split(delimiter);
  }

  public static void broadcast(String message, String permission) {
    Set<Permissible> permissibles = Bukkit.getPluginManager().getPermissionSubscriptions("bukkit.broadcast.user");
    for (Permissible permissible : permissibles) {
      if (((permissible instanceof CommandSender)) && (permissible.hasPermission(permission))) {
        CommandSender user = (CommandSender) permissible;
        user.sendMessage(message);
      }
    }
  }

  public static String getReason(int start, String[] args) {
    String reason = StringUtils.join(args, " ", start, args.length);

    if (!args[start].startsWith("#")) return reason;

    String key = args[start].replace("#", "");
    String replace = BanManager.getPlugin().getReasonsConfig().getReason(key);

    if (replace == null) return reason;

    return reason.replace("#" + key, replace);
  }
}
