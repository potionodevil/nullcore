package xyz.nullshade.nullcore.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.nullshade.nullcore.annotation.Event;

import java.util.*;
import java.util.function.Consumer;

@Event
public class InventoryBuilder implements Listener {
    private final JavaPlugin plugin;
    private String title;
    private int size;
    private final Map<Integer, ItemStack> items;
    private final Map<Integer, Consumer<InventoryClickEvent>> clickListeners;
    private final Set<UUID> inventoryViewers;
    private Consumer<InventoryCloseEvent> closeListener;
    private boolean cancelClicks = true;

    public InventoryBuilder(JavaPlugin plugin) {
        this.plugin = plugin;
        this.title = "Inventory";
        this.size = 27; // 3 Reihen als Standard
        this.items = new HashMap<>();
        this.clickListeners = new HashMap<>();
        this.inventoryViewers = new HashSet<>();
    }

    public InventoryBuilder title(String title) {
        this.title = title;
        return this;
    }

    public InventoryBuilder size(int size) {
        if (size % 9 != 0) {
            throw new IllegalArgumentException("Inventory size must be a multiple of 9");
        }
        this.size = size;
        return this;
    }

    public InventoryBuilder rows(int rows) {
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("Rows must be between 1 and 6");
        }
        this.size = rows * 9;
        return this;
    }

    public InventoryBuilder item(int slot, ItemStack item) {
        if (slot < 0 || slot >= size) {
            throw new IllegalArgumentException("Slot must be between 0 and " + (size - 1));
        }
        this.items.put(slot, item);
        return this;
    }

    public InventoryBuilder item(int slot, ItemStack item, Consumer<InventoryClickEvent> clickListener) {
        item(slot, item);
        this.clickListeners.put(slot, clickListener);
        return this;
    }


    public ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public InventoryBuilder fillEmptySlots(ItemStack fillItem) {
        for (int i = 0; i < size; i++) {
            if (!items.containsKey(i)) {
                items.put(i, fillItem);
            }
        }
        return this;
    }

    public InventoryBuilder onClose(Consumer<InventoryCloseEvent> closeListener) {
        this.closeListener = closeListener;
        return this;
    }


    public InventoryBuilder cancelClicks(boolean cancelClicks) {
        this.cancelClicks = cancelClicks;
        return this;
    }

    public Inventory build() {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }
        return inventory;
    }


    public Inventory open(Player player) {
        Inventory inventory = build();
        player.openInventory(inventory);
        inventoryViewers.add(player.getUniqueId());
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!inventoryViewers.contains(player.getUniqueId())) {
            return;
        }
        if (cancelClicks) {
            event.setCancelled(true);
        }
        Consumer<InventoryClickEvent> listener = clickListeners.get(event.getSlot());
        if (listener != null) {
            listener.accept(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getPlayer();
        if (inventoryViewers.remove(player.getUniqueId())) {
            // Führe den individuellen CloseListener aus, falls vorhanden
            if (closeListener != null) {
                closeListener.accept(event);
            }
        }
    }
}
