package info.ahaha.shulkerball;

import jdk.vm.ci.code.site.Site;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public final class ShulkerBall extends JavaPlugin implements Listener {

    public static ShulkerBall plugin;
    public DataManager manager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        manager = new DataManager(this);
        new CreateItemData(this);
        for (ItemData data : ItemData.data){
            Bukkit.addRecipe(data.getRecipe());
        }
        getServer().getPluginManager().registerEvents(this,this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (!hand.hasItemMeta()) return;
        if (!hand.getItemMeta().hasLore()) return;
        if (!hand.getItemMeta().getLore().get(0).equalsIgnoreCase(getShulkerBall().getItemMeta().getLore().get(0)))
            return;
        if (hand.getEnchantments().isEmpty()) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            Location location = player.getLocation().clone();
            location.add(player.getLocation().getDirection());
            location.getWorld().spawn(location, Shulker.class);
            player.sendMessage(ChatColor.GOLD + "[ ShulkerBall ] " + ChatColor.GREEN + "シュルカーを召喚しました！");
            hand.setAmount(hand.getAmount() - 1);
        }else if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (e.getClickedBlock() == null)return;
            Location location = e.getClickedBlock().getRelative(BlockFace.UP).getLocation().clone();
            location.getWorld().spawn(location, Shulker.class);
            player.sendMessage(ChatColor.GOLD + "[ ShulkerBall ] " + ChatColor.GREEN + "シュルカーを召喚しました！");
            hand.setAmount(hand.getAmount() - 1);
        }
    }

    @EventHandler
    public void onGetShulker(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Shulker)) return;
        Player player = e.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();
        ItemStack clone = hand.clone();
        if (!hand.isSimilar(getShulkerBall())) return;
        clone.setAmount(1);
        hand.setAmount(hand.getAmount()-1);
        e.getRightClicked().remove();
        player.sendMessage(ChatColor.GOLD + "[ ShulkerBall ] " + ChatColor.GREEN + "シュルカーを捕獲しました！");
        ItemMeta meta = clone.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        clone.setItemMeta(meta);
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), clone);
        } else {
            player.getInventory().addItem(clone);
        }
    }
    private ItemStack getShulkerBall() {
        ItemStack item = null;
        for (ItemData data : ItemData.data){
            if (data.getConfigName().equalsIgnoreCase("ShulkerBall")){
                item = data.getItem();
            }
        }
        return item;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (ItemData data : ItemData.data){
            data.removeRecipe();
        }
    }
}
