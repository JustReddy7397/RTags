package ga.justreddy.wiki.rtags.utils;

import ga.justreddy.wiki.rtags.RTags;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private Utils() {}

    private static final int CENTER_PX = 154;
    public static final String CHAT_LINE = "&m-----------------------------------------------------";
    public static final String CONSOLE_LINE = "*-----------------------------------------------------*";
    public static final String LORE_LINE = "&m--------------------------";

    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> formatList(List<String> input) {
        List<String> list = new ArrayList<>();
        for (String line : input) list.add(format(line.replace("%line%", LORE_LINE)));
        return list;
    }

    public static void sendMessage(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(format(message.replace("%line%", CHAT_LINE)));
    }

    public static void sendMessage(CommandSender sender, String... message) {
        for (String line : message) {
            sendMessage(sender, line);
        }
    }

    public static void error(Throwable throwable, String description, boolean disable) {
        if (throwable != null) throwable.printStackTrace();

        if (disable) {
            sendConsole(
                    "&4%line%",
                    "&cAn internal error has occurred in " + RTags.getPlugin().getName() + "!",
                    "&cContact the plugin author if you cannot fix this error.",
                    "&cDescription: &6" + description,
                    "&cThe plugin will now disable.",
                    "&4%line%"
            );
        } else {
            sendConsole(
                    "&4%line%",
                    "&cAn internal error has occurred in " + RTags.getPlugin().getDescription().getName() + "!",
                    "&cContact the plugin author if you cannot fix this error.",
                    "&cDescription: &6" + description,
                    "&4%line%"
            );
        }

        if (disable && Bukkit.getPluginManager().isPluginEnabled(RTags.getPlugin())) {
            Bukkit.getPluginManager().disablePlugin(RTags.getPlugin());
        }
    }

    public static void errorCommand(CommandSender sender, String description) {
        sendMessage(sender, "&4%line%", "&cAn error occurred while running this command", "&cDescription: &6" + description, "&4%line%");
    }

    public static void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(format(message.replace("%line%", CONSOLE_LINE)));
    }

    public static void sendConsole(String... message) {
        for (String line : message) {
            sendConsole(line);
        }
    }


}
