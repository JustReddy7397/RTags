package ga.justreddy.wiki.rtags.menu.menus;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rtags.RTags;
import ga.justreddy.wiki.rtags.menu.PaginatedSuperMenu;
import ga.justreddy.wiki.rtags.tags.Tag;
import ga.justreddy.wiki.rtags.tags.TagData;
import ga.justreddy.wiki.rtags.tags.TagManager;
import ga.justreddy.wiki.rtags.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerTagMenu extends PaginatedSuperMenu {

    private final Player p;

    public PlayerTagMenu(Player p) {
        super(Utils.format("&aTags of " + p.getName()), 54);
        this.p = p;
    }


    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getCurrentItem().getType() == XMaterial.LIME_DYE.parseMaterial() && e.getCurrentItem().getItemMeta()
                .getDisplayName().equals(Utils.format("&aLeft"))
        ) {
            if (page == 0) {
                e.getWhoClicked().sendMessage(Utils.format("&cYou are already on the first page."));
            } else {
                page -= 1;
                super.open((Player) e.getWhoClicked());
            }
        } else if (e.getCurrentItem().getType() == XMaterial.LIME_DYE.parseMaterial() &&e.getCurrentItem().getItemMeta()
                .getDisplayName().equals(Utils.format("&aRight"))
        ) {
            if ((index + 1) >= TagData.getTagData().getTags().size()) {
                e.getWhoClicked().sendMessage(Utils.format("&cYou are on the last page."));
            } else {
                page += 1;
                super.open((Player) e.getWhoClicked());
            }
        } else if (e.getCurrentItem().getType() == XMaterial.BARRIER.parseMaterial()) {
            e.getWhoClicked().closeInventory();
        }

        if (e.getCurrentItem().getType() == XMaterial.NAME_TAG.parseMaterial() && p.getUniqueId() == e.getWhoClicked().getUniqueId()) {
            String identifier = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            TagData.getTagData().setTag(e.getWhoClicked().getUniqueId().toString(), identifier);
            Utils.sendMessage((Player) e.getWhoClicked(), RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-selected").replaceAll("%identifier%", identifier));
            e.getWhoClicked().closeInventory();
        }

        if (e.getCurrentItem().getType() == XMaterial.IRON_DOOR.parseMaterial() && p.getUniqueId() == e.getWhoClicked().getUniqueId()) {
            TagData.getTagData().setTag(e.getWhoClicked().getUniqueId().toString(), "");
            Utils.sendMessage((Player) e.getWhoClicked(), RTags.getPlugin().getMessagesConfig().getConfig().getString("tag-removed"));
            e.getWhoClicked().closeInventory();
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
                if (p.getUniqueId() == player.getUniqueId()) {
                    lore.add("&7Click to equip this tag");
                }
                lore.add("&4%line%");
                meta.setLore(Utils.formatList(lore));
                nameTag.setItemMeta(meta);
                inventory.addItem(nameTag);
            }
            final ItemStack door = XMaterial.IRON_DOOR.parseItem();
            final ItemMeta meta = door.getItemMeta();
            meta.setDisplayName(Utils.format("&aCurrent tag"));
            List<String> lore = new ArrayList<>();
            lore.add("&4%line%");
            lore.add("%RTags_name%");
            if(p.getUniqueId() == player.getUniqueId()) {
                lore.add("");
                lore.add("&7Click to remove tag");
            }
            lore.add("&4%line%");
            lore = lore.stream().map(a -> PlaceholderAPI.setPlaceholders(this.p, String.valueOf(a))).collect(Collectors.toList());
            meta.setLore(Utils.formatList(lore));
            door.setItemMeta(meta);
            inventory.setItem(47, door);
        }
    }
}
