package info.ahaha.shulkerball;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CreateItemData {

    public CreateItemData(ShulkerBall plugin) {
        create(plugin.manager);
        setDataRecipes();
    }

    public void create(DataManager manager) {
        for (String config : manager.getItems().getStringList("Items")) {
            ItemStack item = new ItemStack(Material.valueOf(manager.getItems().getString(config + ".Material")));
            ItemMeta meta = item.getItemMeta();


            meta.setDisplayName(manager.getItems().getString(config + ".Name"));
            List<String> lore = manager.getItems().getStringList(config + ".Lore");
            if (manager.getItems().getConfigurationSection(config + ".Enchant") != null) {
                for (String ench : manager.getItems().getStringList(config + ".Enchant.List")) {
                    meta.addEnchant(Enchantment.getByName(ench), manager.getItems().getInt(config + ".Enchant." + ench + ".Level"), true);
                }
            } else {
                if (manager.getItems().getBoolean(config + ".Glow")) {
                    meta.addEnchant(Enchantment.MENDING, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            ItemData data = new ItemData(config, item);
            ItemData.data.add(data);

        }
    }

    public void setDataRecipes() {
        for (ItemData data : ItemData.data) {
            data.setRecipe(RecipeRegister.register(data));
        }
    }
}
