package ga.justreddy.wiki.rtags.commands;

import ga.justreddy.wiki.rtags.menu.menus.EditMenu;
import ga.justreddy.wiki.rtags.menu.menus.GlobalTagMenu;
import ga.justreddy.wiki.rtags.tags.TagData;
import ga.justreddy.wiki.rtags.tags.TagManager;
import ga.justreddy.wiki.rtags.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

public class TagCommand implements CommandExecutor {

    private final List<Permission> permissions = new ArrayList<>();

    public TagCommand() {
        permissions.add(new Permission("create <identifier>", "Creates a new tag", "rtags.command.create"));
        permissions.add(new Permission("delete <identifier>", "Deletes an existing tag", "rtags.command.delete"));
        permissions.add(new Permission("settag <player> <identifier>", "Forcefully sets a tag for the player", "rtags.command.settag"));
        permissions.add(new Permission("edit <identifier>", "Edit a tag", "rtags.command.edit"));
        permissions.add(new Permission("list <all/player>", "View all/your tags", "rtags.command.view"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return true;
        }
        final Player player = (Player) sender;
        try{
            switch (args[0]) {
                case "create": runCreateTagCommand(player, args); break;
                case "delete": runDeleteTagCommand(player, args); break;
                case "settag": runSetTagCommand(player, args); break;
                case "edit": runEditCommand(player, args); break;
                case "help": runHelpCommand(player, args); break;
                case "list": runListCommand(player, args); break;
                case "info": runInfoCommand(player, args);
                default: Utils.errorCommand(player, "Command doesn't exist! Try /tag help"); break;
            }
        }catch (IndexOutOfBoundsException ex) {
            Utils.errorCommand(player, "No arguments given! Try /tag help");
        }

        return true;
    }

    private void runCreateTagCommand(Player player, String[] args) {
        try{
            TagManager.getTagManager().createTag(player, args[1], args[1]);
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "Invalid arguments! /tag create <identifier>");
        }
    }

    private void runDeleteTagCommand(Player player, String[] args) {
        try{
            TagManager.getTagManager().removeTag(player, args[1]);
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "Invalid arguments! /tag delete <identifier>");
        }
    }

    private void runSetTagCommand(Player player, String[] args) {
        try{
            Player player1 = Bukkit.getPlayer(args[1]);
            if (player == null) return;
            TagData.getTagData().setTag(player1.getUniqueId().toString(), args[2]);
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "Invalid arguments! /tag settag <player> <identifier>");
        }
    }

    private void runEditCommand(Player player, String[] args) {
        try{
            if (!TagManager.getTagManager().tagExists(args[1])) {
                Utils.errorCommand(player, "Tag with that identifier doesn't exist");
                return;
            }
            new EditMenu(args[1]).open(player);
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "Invalid arguments! /tag delete <identifier>");
        }
    }

    private void runHelpCommand(Player player, String[] args) {
        if (!player.hasPermission("rtags.command.help")) {
            Utils.sendMessage(player, "&b/tags info &7- &eShows info about this plugin");
            return;
        }

        StringBuilder desc = new StringBuilder();
        for (Permission permission : permissions) {
            if (!player.hasPermission(permission.getPermission())) continue;
            desc.append("&b/tags ").append(permission.getName()).append(" &7- &e").append(permission.getDescription()).append("\n");
        }
        Utils.sendMessage(player, "&b%line%" ,desc.toString(), "&b/tags info &7- &eShows info about this plugin", "&b%line%");
    }

    private void runInfoCommand(Player player, String[] args) {

    }

    private void runListCommand(Player player, String[] args) {
        new GlobalTagMenu().open(player);
    }

}

class Permission {

    @Getter private final String name;
    @Getter private final String description;
    @Getter private final String permission;

    public Permission(String name, String description, String permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
    }

}