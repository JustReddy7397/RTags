package ga.justreddy.wiki.rtags.menu.menus;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rtags.menu.PaginatedSuperMenu;
import ga.justreddy.wiki.rtags.tags.Tag;
import ga.justreddy.wiki.rtags.tags.TagData;
import ga.justreddy.wiki.rtags.tags.TagManager;
import ga.justreddy.wiki.rtags.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GlobalTagMenu extends PaginatedSuperMenu {

    public GlobalTagMenu() {
        super(Utils.format("&aAll tags"), 54);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType() == XMaterial.LIME_DYE.parseMaterial() && e.getCurrentItem().getItemMeta()
                .getDisplayName().equals(Utils.format("&aLeft"))
        ) {
            if (page == 0) {
                p.sendMessage(Utils.format("&cYou are already on the first page."));
            } else {
                page -= 1;
                super.open(p);
            }
        } else if (e.getCurrentItem().getType() == XMaterial.LIME_DYE.parseMaterial() &&e.getCurrentItem().getItemMeta()
                .getDisplayName().equals(Utils.format("&aRight"))
        ) {
            if ((index + 1) >= TagData.getTagData().getTags().size()) {
                p.sendMessage(Utils.format("&cYou are on the last page."));
            } else {
                page += 1;
                super.open(p);
            }
        } else if (e.getCurrentItem().getType() == XMaterial.BARRIER.parseMaterial()) {
            p.closeInventory();
        }
    }

    @Override
    public void setMenuItems(Player player) {
        addMenuBorder();
        for (int i = 0; i < maxItemsPerPage; i++) {
            index = maxItemsPerPage * page + i;
            if (index >= TagData.getTagData().getTags().size()) break;
            if (TagData.getTagData().getTags().get(index) != null) {
                Tag tag = TagData.getTagData().getTags().get(index);
                ItemStack nameTag = XMaterial.NAME_TAG.parseItem();
                ItemMeta meta = nameTag.getItemMeta();
                meta.setDisplayName(Utils.format("&f&o" + tag.getIdentifier()));
                List<String> lore = new ArrayList<>();
                lore.add("&4%line%");
                lore.add(tag.getName());
                lore.add(tag.getDescription());
                lore.add("&4%line%");
                meta.setLore(Utils.formatList(lore));
                nameTag.setItemMeta(meta);
                inventory.addItem(nameTag);
            }
        }
    }
}
