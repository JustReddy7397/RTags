package ga.justreddy.wiki.rtags.menu;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class SuperMenu implements InventoryHolder {

    protected Inventory inventory;

    protected ItemStack FILLER_GLASS = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();

    private final String menuName;
    private final int size;

    public SuperMenu(String menuName, int size) {
        this.menuName = menuName;
        this.size = size;
    }

    public abstract void handleMenu(InventoryClickEvent e);

    public abstract void setMenuItems(Player player);

    public void open(Player player) {

        inventory = Bukkit.createInventory(this, size, menuName);

        setMenuItems(player);

        player.openInventory(inventory);

    }

    public void setFillerGlass() {
        for (int i = 0; i < size; i++) {
            if(inventory.getItem(i) == null) inventory.setItem(i, FILLER_GLASS);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
