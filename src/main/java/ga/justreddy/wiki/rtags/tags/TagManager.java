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
            Bukkit.getServer().getPluginManager().addPermission(new Permission(tag.getPermission()));
            Utils.sendConsole("[RTags] &aLoaded custom tag: &l" + tag.getIdentifier());
        }
    }

    public void deleteTags() {
        tags.clear();
        tagById.clear();
    }


    public void createTag(Player player, String identifier, String name) {
        if (tagExists(identifier)) {
            Utils.errorCommand(player, "Tag with that identifier already exists");
            return;
        }
        Tag tag = new Tag(identifier, name);
        tags.add(tag);
        tagById.put(identifier, tag);
        TagData.getTagData().createTag(identifier, name);
        Utils.sendMessage(player, "&aSuccessfully created a tag with the identifier: &l" + identifier);
        new EditMenu(identifier).open(player);
    }

    public void removeTag(Player player, String identifier) {
        if (!tagExists(identifier)) {
            Utils.errorCommand(player, "Tag with that identifier doesn't exist");
            return;
        }

        Tag tag = getTag(identifier);
        tags.remove(tag);
        tagById.remove(tag.getIdentifier());
        TagData.getTagData().removeTag(identifier);
        Utils.sendMessage(player, "&aSuccessfully removed the tag with the identifier: &l" + identifier);
    }

    public void setTagName(Player player, String identifier, String newName) {
        if (!tagExists(identifier)) {
            Utils.errorCommand(player, "Tag with that identifier doesn't exist");
            return;
        }

        Tag tag = getTag(identifier);
        tag.setName(newName);
        TagData.getTagData().setTagName(identifier, newName);
        Utils.sendMessage(player, "&aSuccessfully changed the name of the tag with the identifier: " + identifier + " to: " + newName);
    }

    public void setTagDescription(Player player, String identifier, String newDescription) {
        if (!tagExists(identifier)) {
            Utils.errorCommand(player, "Tag with that identifier doesn't exist");
            return;
        }

        Tag tag = getTag(identifier);
        tag.setDescription(newDescription);
        TagData.getTagData().setTagDescription(identifier, newDescription);
        Utils.sendMessage(player, "&aSuccessfully changed the description of the tag with the identifier: " + identifier);
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
