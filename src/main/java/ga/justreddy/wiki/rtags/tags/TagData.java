package ga.justreddy.wiki.rtags.tags;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import ga.justreddy.wiki.rtags.RTags;
import ga.justreddy.wiki.rtags.database.DatabaseManager;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TagData {

    private TagData() {
    }

    private final RTags plugin = RTags.getPlugin();
    private final DatabaseManager databaseManager = plugin.getDatabaseManager();

    private static TagData tagData;

    public static TagData getTagData() {
        if (tagData == null) {
            tagData = new TagData();
        }
        return tagData;
    }

    public void createTag(String identifier, String name) {
        if (databaseManager.isMongoConnected()) {
            databaseManager.getCollection("tags").insertOne(new Document("identifier", identifier)
                    .append("name", name)
                    .append("description", ""));
        } else {
            databaseManager.update("INSERT INTO rtags_tags (identifier, name, description) VALUES ('" + identifier + "', '" + name + "', '" + "" + "')");
        }
    }

    public void removeTag(String identifier) {
        if (databaseManager.isMongoConnected()) {
            databaseManager.getCollection("tags").deleteOne(new Document("identifier", identifier));
        } else {
            databaseManager.update("DELETE FROM rtags_tags WHERE identifier='" + identifier + "'");
        }
    }

    public void setTag(String uuid, String id) {
        if (databaseManager.isMongoConnected()) {
            databaseManager.getCollection("playertags").updateOne(Filters.eq("uuid", uuid), Updates.set("identifier", id));
        } else {
            databaseManager.update("UPDATE rtags_playerTags SET identifier='" + id + "' WHERE uuid='" + uuid + "'");
        }
    }

    @SneakyThrows
    public String getTag(String uuid) {
        if (databaseManager.isMongoConnected()) {
            Document doc = databaseManager.getCollection("playertags").find(new Document("uuid", uuid)).first();
            if (doc != null) return doc.getString("identifier");
        } else {
            ResultSet rs = databaseManager.getResult("SELECT * FROM rtags_playerTags WHERE uuid='" + uuid + "'");
            if (rs.next()) return rs.getString("identifier");
        }
        return null;
    }

    @SneakyThrows
    public List<Tag> getPlayerTags(Player player) {
        List<Tag> tags = new ArrayList<>();
        for(Tag tag : TagManager.getTagManager().getTags()) {
            if(player.hasPermission(tag.getPermission())) {
                tags.add(tag);
            }
        }
        return tags;
    }

    @SneakyThrows
    public List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();

        if (databaseManager.isMongoConnected()) {
            for (Document document : databaseManager.getCollection("tags").find()) {
                Tag tag = new Tag(document.getString("name"), document.getString("identifier"));
                tag.setDescription(document.getString("description"));
                tags.add(tag);
            }
        } else {
            ResultSet rs = databaseManager.getResult("SELECT * FROM rtags_tags");
            while (rs.next()) {
                Tag tag = new Tag(rs.getString("name"), rs.getString("identifier"));
                tag.setDescription(rs.getString("description"));
                tags.add(tag);
            }
        }

        return tags;
    }

    public void setTagName(String identifier, String newName) {
        if (databaseManager.isMongoConnected()) {
            databaseManager.getCollection("tags").updateOne(Filters.eq("identifier", identifier), Updates.set("name", newName));
        } else {
            databaseManager.update("UPDATE rtags_tags SET name='" + newName + "' WHERE identifier='" + identifier + "'");
        }
    }

    public void setTagDescription(String identifier, String newDescription) {
        if (databaseManager.isMongoConnected()) {
            databaseManager.getCollection("tags").updateOne(Filters.eq("identifier", identifier), Updates.set("description", newDescription));
        } else {
            databaseManager.update("UPDATE rtags_tags SET description='" + newDescription + "' WHERE identifier='" + identifier + "'");
        }
    }

    public boolean isTagEnabled(String uuid, String identifier) {
        return getTag(uuid).equals(identifier);
    }
}
