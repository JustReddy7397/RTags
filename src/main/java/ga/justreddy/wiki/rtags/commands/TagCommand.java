package ga.justreddy.wiki.rtags.commands;

import ga.justreddy.wiki.rtags.RTags;
import ga.justreddy.wiki.rtags.menu.menus.EditMenu;
import ga.justreddy.wiki.rtags.menu.menus.GlobalTagMenu;
import ga.justreddy.wiki.rtags.menu.menus.PlayerTagMenu;
import ga.justreddy.wiki.rtags.tags.Tag;
import ga.justreddy.wiki.rtags.tags.TagData;
import ga.justreddy.wiki.rtags.tags.TagManager;
import ga.justreddy.wiki.rtags.utils.Utils;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;

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
        permissions.add(new Permission("clear", "Clear your current tag", "rtags.command.clear"));
        permissions.add(new Permission("reload", "Reload the plugin", "rtags.command.reload"));
        permissions.add(new Permission("select", "Select a tag you currently have", "rtags.command.select"));
        permissions.add(new Permission("add", "Add a tag to a player", "rtags.command.add"));
        permissions.add(new Permission("remove", "Remove a tag from someone", "rtags.command.remove"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try{
            switch (args[0]) {
                case "create": runCreateTagCommand(sender, args); break;
                case "delete": runDeleteTagCommand(sender, args); break;
                case "settag": runSetTagCommand(sender, args); break;
                case "edit": runEditCommand(sender, args); break;
                case "help": runHelpCommand(sender, args); break;
                case "list": runListCommand(sender, args); break;
                case "info": runInfoCommand(sender, args); break;
                case "clear": runClearTagCommand(sender, args); break;
                case "reload": runReloadCommand(sender, args); break;
                case "select": runSelectTagCommand(sender, args); break;
                case "add": addCommand(sender, args); break;
                case "remove": removeCommand(sender, args); break;
                default: Utils.errorCommand(sender, "Command doesn't exist! Try /tag help"); break;
            }
        }catch (IndexOutOfBoundsException ex) {
            Utils.errorCommand(sender, "No arguments given! Try /tag help");
        }

        return true;
    }

    private void runCreateTagCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return;
        }
        Player player = (Player) sender;
        try{
            if(!player.hasPermission("rtags.command.create")) {
                Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("no-perms").replaceAll("%permission%", "rtags.command.create"));
                return;
            }
            TagManager.getTagManager().createTag(player, args[1], args[1]);
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "Invalid arguments! /tag create <identifier>");
        }
    }

    private void runDeleteTagCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return;
        }
        Player player = (Player) sender;
        try{
            if(!player.hasPermission("rtags.command.delete")) {
                Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("no-perms").replaceAll("%permission%", "rtags.command.delete"));
                return;
            }
            TagManager.getTagManager().removeTag(player, args[1]);
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "Invalid arguments! /tag delete <identifier>");
        }
    }

    private void runSetTagCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return;
        }
        Player player = (Player) sender;
        try{
            if(!player.hasPermission("rtags.command.settag")) {
                Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("no-perms").replaceAll("%permission%", "rtags.command.settag"));
                return;
            }
            Player player1 = Bukkit.getPlayer(args[1]);
            if (player1 == null) {
                Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("not-found"));
                return;
            }
            TagData.getTagData().setTag(player1.getUniqueId().toString(), args[2]);
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "Invalid arguments! /tag settag <player> <identifier>");
        }
    }

    private void runEditCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return;
        }
        Player player = (Player) sender;
        try{
            if(!player.hasPermission("rtags.command.edit")) {
                Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("no-perms").replaceAll("%permission%", "rtags.command.edit"));
                return;
            }
            if (!TagManager.getTagManager().tagExists(args[1])) {
                Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-no-exist"));
                return;
            }
            new EditMenu(args[1]).open(player);
        }catch (IndexOutOfBoundsException ex){
            Utils.errorCommand(player, "Invalid arguments! /tag delete <identifier>");
        }
    }

    private void runHelpCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return;
        }
        Player player = (Player) sender;
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

    private void runClearTagCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("rtags.command.edit")) {
            Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("no-perms").replaceAll("%permission%", "rtags.command.clear"));
            return;
        }

        TagData.getTagData().setTag(player.getUniqueId().toString(), "");
        Utils.sendMessage(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-removed"));

    }

    private void runInfoCommand(CommandSender sender, String[] args) {
        Utils.sendMessage(sender, "&e%line%", "&bThis server is running", "&bRTags version &l" + RTags.getPlugin().getDescription().getVersion(), "&bBy JustReddy", "&e%line%");
    }

    private void runListCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return;
        }
        Player player = (Player) sender;
        try{
            if (args[1].equalsIgnoreCase("all") && player.hasPermission("rtags.view.all")) {
                new GlobalTagMenu().open(player);
                return;
            }
            if (player.hasPermission("rtags.view.other")) {
                Player player1 = Bukkit.getPlayer(args[1]);
                if (player1 == null) return;
                new PlayerTagMenu(player1).open(player);
            }else{
                new PlayerTagMenu(player).open(player);
            }
        }catch (IndexOutOfBoundsException ex) {
            new PlayerTagMenu(player).open(player);
        }
    }

    @SneakyThrows
    private void runReloadCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("rtags.command.reload")) {
            Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("no-perms").replaceAll("%permission%", "rtags.command.reload"));
            return;
        }

        RTags.getPlugin().getMessagesConfig().reload();
        Utils.sendMessage(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("reload"));
    }


    private void runSelectTagCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            Utils.sendConsole("&cYou need to be a player to execute this command");
            return;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("rtags.command.select")) {
            Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("no-perms").replaceAll("%permission%", "rtags.command.select"));
            return;
        }

        try {
            String tag = args[1];
            Tag tag1 = TagManager.getTagManager().getTag(tag);
            if (tag1 == null) {
                Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-no-exist"));
                return;
            }
            if (!player.hasPermission(tag1.getPermission())) {
                Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("not-unlocked"));
                return;
            }
            TagData.getTagData().setTag(player.getUniqueId().toString(), tag);
            Utils.sendMessage(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-selected").replaceAll("%identifier%", tag));
        }catch (IndexOutOfBoundsException ex) {
            Utils.errorCommand(player, "Invalid arguments! /tag select <identifier>");
        }

    }

    private void addCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("rtags.command.add")) {
            Utils.errorCommand(sender, RTags.getPlugin().getMessagesConfig().getConfig().getString("no-perms").replaceAll("%permission%", "rtags.command.add"));
            return;
        }
        try{
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            Tag tag = TagManager.getTagManager().getTag(args[2]);

            if (tag == null) {
                Utils.errorCommand(sender, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-no-exist"));
                return;
            }

            if (RTags.getPlugin().getPermission().playerHas(null, offlinePlayer, tag.getPermission())) {
                Utils.errorCommand(sender, RTags.getPlugin().getMessagesConfig().getConfig().getString("player-tag-already-unlocked").replaceAll("%player%", offlinePlayer.getName()));
                return;
            }

            RTags.getPlugin().getPermission().playerAdd(null, offlinePlayer, tag.getPermission());

            Utils.sendMessage(sender, RTags.getPlugin().getMessagesConfig().getConfig().getString("player-tag-added")
                    .replaceAll("%identifier%", tag.getIdentifier())
                    .replaceAll("%player%", offlinePlayer.getName()));;
        }catch (IndexOutOfBoundsException ex) {
            Utils.errorCommand(sender, "Invalid Arguments! /tag add <player> <identifier>");
        }
    }

    private void removeCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("rtags.command.remove")) {
            Utils.errorCommand(sender, RTags.getPlugin().getMessagesConfig().getConfig().getString("no-perms").replaceAll("%permission%", "rtags.command.remove"));
            return;
        }
        try{
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
            Tag tag = TagManager.getTagManager().getTag(args[2]);

            if (tag == null) {
                Utils.errorCommand(sender, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-no-exist"));
                return;
            }

            if (!RTags.getPlugin().getPermission().playerHas(null, offlinePlayer, tag.getPermission())) {
                Utils.errorCommand(sender, RTags.getPlugin().getMessagesConfig().getConfig().getString("player-tag-not-unlocked").replaceAll("%player%", offlinePlayer.getName()));
                return;
            }

            RTags.getPlugin().getPermission().playerAdd(null, offlinePlayer, tag.getPermission());
            if(TagData.getTagData().isTagEnabled(offlinePlayer.getUniqueId().toString(), tag.getIdentifier())) {
                TagData.getTagData().setTag(offlinePlayer.getUniqueId().toString(), "");
            }

            Utils.sendMessage(sender, RTags.getPlugin().getMessagesConfig().getConfig().getString("player-tag-removed")
                    .replaceAll("%identifier%", tag.getIdentifier())
                    .replaceAll("%player%", offlinePlayer.getName()));;
        }catch (IndexOutOfBoundsException ex) {
            Utils.errorCommand(sender, "Invalid Arguments! /tag remove <player> <identifier>");
        }
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