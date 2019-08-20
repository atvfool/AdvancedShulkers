// 
// Decompiled by Procyon v0.5.36
// 

package cn.mcrain.advancedshulkerboxes;

import java.util.Iterator;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.block.BlockState;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedShulkerboxes extends JavaPlugin implements Listener
{
    private BukkitTask timeTask;
    
    public AdvancedShulkerboxes() {
        this.timeTask = null;
    }
    
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.timeTask = this.getServer().getScheduler().runTaskTimer((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                AdvancedShulkerboxes.this.CheckHandShulker();
            }
        }, 2L, 2L);
    }
    
    public void onDisable() {
        this.timeTask.cancel();
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void GameClick(final PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || (!e.isCancelled() && e.getHand() == EquipmentSlot.HAND && e.getAction() == Action.RIGHT_CLICK_BLOCK)) && !e.getPlayer().isSneaking() && e.getPlayer().getEquipment().getItemInMainHand() != null && this.isShulkerBox(e.getPlayer().getEquipment().getItemInMainHand().getTypeId()) && e.getPlayer().hasPermission("advancedshulkerboxes.use")) {
            final BlockStateMeta im = (BlockStateMeta)e.getPlayer().getEquipment().getItemInMainHand().getItemMeta();
            final ShulkerBox shulker = (ShulkerBox)im.getBlockState();
            final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, "§rShulker Box");
            inv.setContents(shulker.getInventory().getContents());
            e.getPlayer().openInventory(inv);
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void InvClick(final InventoryClickEvent e) {
        if (e.getInventory().getName().equals("§rShulker Box")) {
            if (!this.isShulkerBox(e.getWhoClicked().getEquipment().getItemInMainHand().getTypeId())) {
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
                ((Player)e.getWhoClicked()).updateInventory();
                return;
            }
            if (e.getClickedInventory() != null && ((e.getClickedInventory().getType() == InventoryType.PLAYER && ((e.getSlot() != -999 && e.getSlot() == e.getWhoClicked().getInventory().getHeldItemSlot()) || (e.isShiftClick() && e.getWhoClicked().getInventory().getItem(e.getSlot()) != null && this.isShulkerBox(e.getWhoClicked().getInventory().getItem(e.getSlot()).getTypeId())))) || (e.getClickedInventory().getType() == InventoryType.CHEST && e.getCursor() != null && this.isShulkerBox(e.getCursor().getTypeId())) || (e.getHotbarButton() != -1 && e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) != null && (e.getHotbarButton() == e.getWhoClicked().getInventory().getHeldItemSlot() || (e.getClickedInventory().getType() == InventoryType.CHEST && this.isShulkerBox(e.getWhoClicked().getInventory().getItem(e.getHotbarButton()).getTypeId())))))) {
                e.setCancelled(true);
                return;
            }
            final BlockStateMeta im = (BlockStateMeta)e.getWhoClicked().getEquipment().getItemInMainHand().getItemMeta();
            final ShulkerBox shulker = (ShulkerBox)im.getBlockState();
            shulker.getInventory().setContents(e.getInventory().getContents());
            im.setBlockState((BlockState)shulker);
            e.getWhoClicked().getEquipment().getItemInMainHand().setItemMeta((ItemMeta)im);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void InvClose(final InventoryCloseEvent e) {
        if (e.getInventory().getName().equals("§rShulker Box") && e.getPlayer().getEquipment().getItemInMainHand() != null && this.isShulkerBox(e.getPlayer().getEquipment().getItemInMainHand().getTypeId())) {
            final BlockStateMeta im = (BlockStateMeta)e.getPlayer().getEquipment().getItemInMainHand().getItemMeta();
            final ShulkerBox shulker = (ShulkerBox)im.getBlockState();
            shulker.getInventory().setContents(e.getInventory().getContents());
            im.setBlockState((BlockState)shulker);
            e.getPlayer().getEquipment().getItemInMainHand().setItemMeta((ItemMeta)im);
        }
    }
    
    public boolean isShulkerBox(final Integer id) {
        return id > 218 && id < 235;
    }
    
    public void CheckHandShulker() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory() != null && player.getOpenInventory().getType() == InventoryType.CHEST && player.getOpenInventory().getTitle().equals("§rShulker Box")) {
                if (!this.isShulkerBox(player.getEquipment().getItemInMainHand().getTypeId())) {
                    player.closeInventory();
                }
                else {
                    final BlockStateMeta im = (BlockStateMeta)player.getEquipment().getItemInMainHand().getItemMeta();
                    final ShulkerBox shulker = (ShulkerBox)im.getBlockState();
                    shulker.getInventory().setContents(player.getOpenInventory().getTopInventory().getContents());
                    im.setBlockState((BlockState)shulker);
                    player.getEquipment().getItemInMainHand().setItemMeta((ItemMeta)im);
                }
            }
        }
    }
}
