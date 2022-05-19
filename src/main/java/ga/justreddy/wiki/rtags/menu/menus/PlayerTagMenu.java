package ga.justreddy.wiki.rtags.menu.menus;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rtags.menu.PaginatedSuperMenu;
import ga.justreddy.wiki.rtags.tags.Tag;
import ga.justreddy.wiki.rtags.tags.TagData;
import ga.justreddy.wiki.rtags.tags.TagManager;
import ga.justreddy.wiki.rtags.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerTagMenu extends PaginatedSuperMenu {

    private final Player p;

    public PlayerTagMenu(Player p) {
        super(Utils.format("&aYour tags"), 54);
        this.p = p;
    }


    @Override
    public void handleMenu(InventoryClickEvent e) {
        if(p.getUniqueId() != e.getWhoClicked().getUniqueId()) return;
        if (e.getCurrentItem().getType() == XMaterial.NAME_TAG.parseMaterial()) {
            String identifier = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            TagData.getTagData().setTag(e.getWhoClicked().getUniqueId().toString(), identifier);
            Utils.sendMessage((Player) e.getWhoClicked(), "&aSuccessfully selected the tag: " + identifier);
        }
    }

    @Override
    public void setMenuItems(Player player) {
        addMenuBorder();
        for (int i = 0; i < maxItemsPerPage; i++) {
            index = maxItemsPerPage * page + i;
            if (index >= TagData.getTagData().getPlayerTags(p).size()) break;
            if (TagData.getTagData().getPlayerTags(p).get(index) != null) {
                Tag tag = TagData.getTagData().getPlayerTags(p).get(index);
                ItemStack nameTag = XMaterial.NAME_TAG.parseItem();
                ItemMeta meta = nameTag.getItemMeta();
                meta.setDisplayName(Utils.format("&f&o" + tag.getIdentifier()));
                List<String> lore = new ArrayList<>();
                lore.add("&4%line%");
                lore.add(tag.getName());
                lore.add(tag.getDescription());
                lore.add("");
                lore.add("&7Click to equip this tag");
                lore.add("&4%line%");
                meta.setLore(Utils.formatList(lore));
                nameTag.setItemMeta(meta);
                inventory.addItem(nameTag);
            }
        }
    }
}
