package ga.justreddy.wiki.rtags.events;

import ga.justreddy.wiki.rtags.RTags;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;

public class JoinEvent implements Listener {

    @SneakyThrows
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if(RTags.getPlugin().getDatabaseManager().isMongoConnected()) {
            Document document = RTags.getPlugin().getDatabaseManager().getCollection("playertags").find(new Document("uuid", e.getPlayer().getUniqueId().toString())).first();
            if (document == null) RTags.getPlugin().getDatabaseManager().getCollection("playertags").insertOne(new Document("uuid", e.getPlayer().getUniqueId().toString())
                    .append("identifier", ""));
        }else{
            ResultSet rs = RTags.getPlugin().getDatabaseManager().getResult("SELECT * FROM playerTags WHERE uuid='"+e.getPlayer().getUniqueId()+"'");
            if (rs.next()) return;
            RTags.getPlugin().getDatabaseManager().update("INSERT INTO playerTags (uuid, identifier) VALUES ('"+e.getPlayer().getUniqueId()+"', '"+""+"')");
        }
    }

}
