package ga.justreddy.wiki.rtags.tags;

import ga.justreddy.wiki.rtags.RTags;
import ga.justreddy.wiki.rtags.menu.menus.EditMenu;
import ga.justreddy.wiki.rtags.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.*;
import java.util.logging.Level;

public class TagManager {

    private TagManager() {
    }


    private static TagManager tagManager;

    @Getter private final List<Tag> tags = new ArrayList<>();
    @Getter private final Map<String, Tag> tagById = new HashMap<>();
    @Getter private final Map<UUID, String> namingMap = new HashMap<>();
    @Getter private final Map<UUID, String> descriptionMap = new HashMap<>();

    public void loadTags() {
        for(Tag tag : TagData.getTagData().getTags()) {
            tags.add(tag);
            tagById.put(tag.getIdentifier(), tag);
            if(Bukkit.getPluginManager().getPermission(tag.getPermission()) == null) {
                Bukkit.getServer().getPluginManager().addPermission(new Permission(tag.getPermission()));
            }
            Utils.sendConsole("[RTags] &aLoaded custom tag: &l" + tag.getIdentifier());

        }
    }

    public void deleteTags() {
        tags.clear();
        tagById.clear();
    }


    public void createTag(Player player, String identifier, String name) {
        if (tagExists(identifier)) {
            Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-exists"));
            return;
        }
        Tag tag = new Tag(identifier, name);
        tags.add(tag);
        tagById.put(identifier, tag);
        TagData.getTagData().createTag(identifier, name);
        Utils.sendMessage(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-create").replaceAll("%identifier%", identifier));
        if(Bukkit.getPluginManager().getPermission(tag.getPermission()) == null) {
            Bukkit.getServer().getPluginManager().addPermission(new Permission(tag.getPermission()));
        }
        new EditMenu(identifier).open(player);
    }

    public void removeTag(Player player, String identifier) {
        if (!tagExists(identifier)) {
            Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-no-exist"));
            return;
        }

        Tag tag = getTag(identifier);
        tags.remove(tag);
        tagById.remove(tag.getIdentifier());
        TagData.getTagData().removeTag(identifier);
        Utils.sendMessage(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-delete").replaceAll("%identifier%", identifier));
    }

    public void setTagName(Player player, String identifier, String newName) {
        if (!tagExists(identifier)) {
            Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-no-exist"));
            return;
        }

        Tag tag = getTag(identifier);
        tag.setName(newName);
        TagData.getTagData().setTagName(identifier, newName);
        Utils.sendMessage(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-name-change").replaceAll("%tag%", identifier).replaceAll("%name%", newName));
    }

    public void setTagDescription(Player player, String identifier, String newDescription) {
        if (!tagExists(identifier)) {
            Utils.errorCommand(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-no-exist"));
            return;
        }

        Tag tag = getTag(identifier);
        tag.setDescription(newDescription);
        TagData.getTagData().setTagDescription(identifier, newDescription);
        Utils.sendMessage(player, RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-description-change").replaceAll("%tag%", identifier));
    }

    public boolean tagExists(Tag tag) {
        return getTags().contains(tag);
    }

    public boolean tagExists(String identifier) {
        return tagById.containsKey(identifier);
    }

    public Tag getTag(String identifier) {
        return tagById.get(identifier);
    }

    public static TagManager getTagManager() {
        if (tagManager == null) {
            tagManager = new TagManager();
        }
        return tagManager;
    }

}
