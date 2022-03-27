package info.ahaha.shulkerball;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataManager {
    public static ShulkerBall plugin;
    public static FileConfiguration items, recipe = null;
    public static File itemsfile, recipefile = null;

    public DataManager(ShulkerBall plugins) {
        plugin = plugins;
        saveDefaultConfig();
    }

    public static void reloadConfig() {
        if (itemsfile == null)
            itemsfile = new File(plugin.getDataFolder(), "items.yml");
        if (recipefile == null)
            recipefile = new File(plugin.getDataFolder(), "recipe.yml");

        items = YamlConfiguration.loadConfiguration(itemsfile);
        recipe = YamlConfiguration.loadConfiguration(recipefile);

        InputStream itemsStream = plugin.getResource("items.yml");
        InputStream recipeStream = plugin.getResource("recipe.yml");

        if (itemsStream != null) {
            YamlConfiguration item = YamlConfiguration.loadConfiguration(new InputStreamReader(itemsStream));
            items.setDefaults(item);
        }
        if (recipeStream != null) {
            YamlConfiguration recipes = YamlConfiguration.loadConfiguration(new InputStreamReader(recipeStream));
            recipe.setDefaults(recipes);
        }

    }


    public FileConfiguration getItems() {
        if (items == null)
            reloadConfig();
        return items;
    }

    public FileConfiguration getRecipe() {
        if (recipe == null)
            reloadConfig();
        return recipe;
    }


    public void saveDefaultConfig() {

        if (itemsfile == null)
            itemsfile = new File(plugin.getDataFolder(), "items.yml");
        if (recipefile == null)
            recipefile = new File(plugin.getDataFolder(), "recipe.yml");

        if (!itemsfile.exists())
            plugin.saveResource("items.yml", false);
        if (!recipefile.exists())
            plugin.saveResource("recipe.yml", false);

    }
}
