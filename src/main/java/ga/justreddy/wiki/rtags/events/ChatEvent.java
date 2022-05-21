package ga.justreddy.wiki.rtags.events;

import ga.justreddy.wiki.rtags.RTags;
import ga.justreddy.wiki.rtags.tags.TagManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.ResultSet;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (TagManager.getTagManager().getNamingMap().containsKey(player.getUniqueId())) {
            TagManager.getTagManager().setTagName(player, TagManager.getTagManager().getNamingMap().get(player.getUniqueId()), e.getMessage());
            TagManager.getTagManager().getNamingMap().remove(player.getUniqueId());
            e.setCancelled(true);
        }
        if (TagManager.getTagManager().getDescriptionMap().containsKey(player.getUniqueId())) {
            TagManager.getTagManager().setTagDescription(player, TagManager.getTagManager().getDescriptionMap().get(player.getUniqueId()), e.getMessage());
            TagManager.getTagManager().getDescriptionMap().remove(player.getUniqueId());
            e.setCancelled(true);
        }
    }




}
