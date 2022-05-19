package ga.justreddy.wiki.rtags.menu;

import com.cryptomorin.xseries.XMaterial;
import ga.justreddy.wiki.rtags.exceptions.MenuSizeException;
import ga.justreddy.wiki.rtags.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class PaginatedSuperMenu extends SuperMenu {

    private final int size;

    public PaginatedSuperMenu(String menuName, int size) {
        super(menuName, size);
        this.size = size;
    }

    protected int page = 0;

    protected int index = 0;

    protected int maxItemsPerPage = 0;

    public void addMenuBorder() {

        final ItemStack left = XMaterial.LIME_DYE.parseItem();
        final ItemMeta meta = left.getItemMeta();
        meta.setDisplayName(Utils.format("&aLeft"));
        left.setItemMeta(meta);
        final ItemStack right = XMaterial.LIME_DYE.parseItem();
        final ItemMeta meta1 = right.getItemMeta();
        meta1.setDisplayName(Utils.format("&aRight"));
        right.setItemMeta(meta1);
        final ItemStack back = XMaterial.BARRIER.parseItem();
        final ItemMeta meta2 = back.getItemMeta();
        meta2.setDisplayName(Utils.format("&cBack"));
        back.setItemMeta(meta2);

        switch (size) {
            case 9:
                Utils.error(new MenuSizeException(), "The paginated menu is too small, the minimum size is 18", false);
                break;
            case 18:
                inventory.setItem(0, FILLER_GLASS);
                inventory.setItem(8, FILLER_GLASS);
                maxItemsPerPage = 7;
                inventory.setItem(12, left);
                inventory.setItem(14, right);
                inventory.setItem(13, back);
                for (int i = 9; i < 18; i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 27:
                // Seven because the row above the middle one will get glassed too!
                maxItemsPerPage = 7;
                inventory.setItem(17, FILLER_GLASS);
                inventory.setItem(9, FILLER_GLASS);
                inventory.setItem(21, left);
                inventory.setItem(22, back);
                inventory.setItem(23, right);
                for (int i = 0; i < 9; i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 18; i < 27; i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 36:
                maxItemsPerPage = 14;
                inventory.setItem(9, FILLER_GLASS);
                inventory.setItem(18, FILLER_GLASS);
                inventory.setItem(17, FILLER_GLASS);
                inventory.setItem(26, FILLER_GLASS);
                inventory.setItem(30, left);
                inventory.setItem(31, back);
                inventory.setItem(32, right);
                for (int i = 0; i < 9; i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 27; i < 35; i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 45:
                maxItemsPerPage = 21;
                inventory.setItem(9, FILLER_GLASS);
                inventory.setItem(18, FILLER_GLASS);
                inventory.setItem(27, FILLER_GLASS);
                inventory.setItem(17, FILLER_GLASS);
                inventory.setItem(26, FILLER_GLASS);
                inventory.setItem(35, FILLER_GLASS);
                inventory.setItem(39, left);
                inventory.setItem(40, back);
                inventory.setItem(41, right);
                for (int i = 0; i < 9; i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 36; i < 45; i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, FILLER_GLASS);
                    }
                }
                break;
            case 54:
                maxItemsPerPage = 28;
                inventory.setItem(17, FILLER_GLASS);
                inventory.setItem(18, FILLER_GLASS);
                inventory.setItem(26, FILLER_GLASS);
                inventory.setItem(27, FILLER_GLASS);
                inventory.setItem(35, FILLER_GLASS);
                inventory.setItem(36, FILLER_GLASS);
                inventory.setItem(48, left);
                inventory.setItem(49, back);
                inventory.setItem(50, right);
                for (int i = 0; i < 10; i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, FILLER_GLASS);
                    }
                }
                for (int i = 44; i < 54; i++) {
                    if (inventory.getItem(i) == null) {
                        inventory.setItem(i, super.FILLER_GLASS);
                    }
                }

        }

    }

}
