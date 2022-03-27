package info.ahaha.shulkerball;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.List;

public class ItemData {

    private final String configName;
    private ItemStack item;
    private String type;
    private Recipe recipe;
    private final NamespacedKey key;

    public static List<ItemData> data = new ArrayList<>();

    public ItemData(String configName, ItemStack item, String type) {
        this.configName = configName;
        this.item = item;
        this.key = new NamespacedKey(ShulkerBall.plugin, configName);
        this.type = type;
    }

    public ItemData(String configName, ItemStack item) {
        this.configName = configName;
        this.item = item;
        this.key = new NamespacedKey(ShulkerBall.plugin, configName);
    }

    public String getConfigName() {
        return configName;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void removeRecipe() {
        Bukkit.removeRecipe(getKey());
    }
}
