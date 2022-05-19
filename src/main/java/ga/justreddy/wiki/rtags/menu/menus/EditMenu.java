package ga.justreddy.wiki.rtags.menu.menus;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.messages.Titles;
import ga.justreddy.wiki.rtags.RTags;
import ga.justreddy.wiki.rtags.menu.SuperMenu;
import ga.justreddy.wiki.rtags.tags.TagData;
import ga.justreddy.wiki.rtags.tags.TagManager;
import ga.justreddy.wiki.rtags.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditMenu extends SuperMenu {

    private final String identifier;

    public EditMenu(String identifier) {
        super(Utils.format("&aEdit tag: " + identifier), 27);
        this.identifier = identifier;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getRawSlot() == 11) {
            e.getWhoClicked().closeInventory();
            TagManager.getTagManager().getNamingMap().put(e.getWhoClicked().getUniqueId(), identifier);
            Titles.sendTitle((Player) e.getWhoClicked(), Utils.format("&aSet the tags name"), Utils.format("&aType it in chat! ( 30s )"));
            Bukkit.getScheduler().runTaskLater(RTags.getPlugin(), () -> TagManager.getTagManager().getNamingMap().remove(e.getWhoClicked().getUniqueId()), 30 * 20L);
        }
        if (e.getRawSlot() == 15) {
            e.getWhoClicked().closeInventory();
            TagManager.getTagManager().getDescriptionMap().put(e.getWhoClicked().getUniqueId(), identifier);
            Titles.sendTitle((Player) e.getWhoClicked(), Utils.format("&aSet the tags description"), "&aType it in chat! ( 30s )");
            Bukkit.getScheduler().runTaskLater(RTags.getPlugin(), () -> TagManager.getTagManager().getDescriptionMap().remove(e.getWhoClicked().getUniqueId()), 30 * 20L);
        }
    }

    @Override
    public void setMenuItems(Player player) {

        ItemStack nameTag = XMaterial.NAME_TAG.parseItem();
        ItemMeta nameTagMeta = nameTag.getItemMeta();
        nameTagMeta.setDisplayName(Utils.format("&9Tag Information"));
        List<String> lore = new ArrayList<>();
        lore.add(
                "&4%line%"
        );
        lore.add(
                "&7- &aIdentifier: &6" + identifier
        );
        lore.add(
                "&7- &aName: " + TagManager.getTagManager().getTag(identifier).getName()
        );
        lore.add("&7- &aDescription: " + TagManager.getTagManager().getTag(identifier).getDescription());
        lore.add(
                "&4%line%"
        );
        nameTagMeta.setLore(Utils.formatList(lore));
        nameTag.setItemMeta(nameTagMeta);
        inventory.setItem(13, nameTag);
        ItemStack name = XMaterial.WRITABLE_BOOK.parseItem();
        ItemMeta nameMeta = name.getItemMeta();
        nameMeta.setDisplayName(Utils.format("&aClick to change tag name"));
        name.setItemMeta(nameMeta);
        inventory.setItem(11, name);
        ItemStack description = XMaterial.PAPER.parseItem();
        ItemMeta descriptionMeta = description.getItemMeta();
        descriptionMeta.setDisplayName(Utils.format("&aClick to change tag description"));
        description.setItemMeta(descriptionMeta);
        inventory.setItem(15, description);
        setFillerGlass();
    }
}
