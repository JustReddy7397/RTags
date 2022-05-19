package ga.justreddy.wiki.rtags;

import ga.justreddy.wiki.rtags.tags.Tag;
import ga.justreddy.wiki.rtags.tags.TagData;
import ga.justreddy.wiki.rtags.tags.TagManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Placeholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "RTags";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JustReddy";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        if (params.equals("name")) {
            Tag tag = TagManager.getTagManager().getTag(TagData.getTagData().getTag(player.getUniqueId().toString()));
            if (tag == null) return "";
            return tag.getName();
        }
        if (params.equals("identifier")) {
            Tag tag = TagManager.getTagManager().getTag(TagData.getTagData().getTag(player.getUniqueId().toString()));
            if (tag == null) return "";
            return tag.getIdentifier();
        }
        if (params.equals("description")) {
            Tag tag = TagManager.getTagManager().getTag(TagData.getTagData().getTag(player.getUniqueId().toString()));
            if (tag == null) return "";
            return tag.getDescription();
        }

        return "";
    }
}
