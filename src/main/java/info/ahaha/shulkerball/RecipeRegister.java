package info.ahaha.shulkerball;

import org.bukkit.Material;
import org.bukkit.inventory.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecipeRegister {

    public static Recipe register(ItemData data) {

        String recipeType = ShulkerBall.plugin.manager.getRecipe().getString(data.getConfigName() + ".RecipeType");
        int exp = ShulkerBall.plugin.manager.getRecipe().getInt(data.getConfigName() + ".EXP");
        int time = ShulkerBall.plugin.manager.getRecipe().getInt(data.getConfigName() + ".Time") * 20;

        if (recipeType.equalsIgnoreCase("CRAFT")) {

            ShapedRecipe recipe = new ShapedRecipe(data.getKey(), data.getItem());

            String first = ShulkerBall.plugin.manager.getRecipe().getString(data.getConfigName() + ".First");
            String second = ShulkerBall.plugin.manager.getRecipe().getString(data.getConfigName() + ".Second");
            String third = ShulkerBall.plugin.manager.getRecipe().getString(data.getConfigName() + ".Third");

            recipe.shape(first, second, third);
            Set<Character> characters = new HashSet<>();
            for (char c : first.toCharArray()) {
                if (c == ' ') continue;
                characters.add(c);
            }
            for (char c : second.toCharArray()) {
                if (c == ' ') continue;
                characters.add(c);
            }
            for (char c : third.toCharArray()) {
                if (c == ' ') continue;
                characters.add(c);
            }
            String type;
            String item;

            Map<Character, Material> recipes_material = new HashMap<>();
            Map<Character, ItemStack> recipes_itemStack = new HashMap<>();

            for (char c : characters) {
                type = ShulkerBall.plugin.manager.getRecipe().getString(data.getConfigName() + "." + c + ".Type");
                item = ShulkerBall.plugin.manager.getRecipe().getString(data.getConfigName() + "." + c + ".Item");
                if (type.equalsIgnoreCase("Material")) {
                    recipes_material.put(c, Material.valueOf(item));
                } else if (type.equalsIgnoreCase("Item")) {
                    ItemData data1 = null;
                    for (ItemData data2 : ItemData.data) {
                        if (data2.getConfigName().equalsIgnoreCase(item)) {
                            data1 = data2;
                            break;
                        }
                    }
                    if (data1 == null) return null;
                    recipes_itemStack.put(c, data1.getItem());
                }
            }

            if (recipes_material.size() != 0) {
                for (char c : recipes_material.keySet()) {
                    recipe.setIngredient(c, recipes_material.get(c));
                }
            }
            if (recipes_itemStack.size() != 0) {
                for (char c : recipes_itemStack.keySet()) {
                    recipe.setIngredient(c, new RecipeChoice.ExactChoice(recipes_itemStack.get(c)));
                }
            }
            data.setType(recipeType);
            return recipe;
        } else if (recipeType.equalsIgnoreCase("FURNACE")) {
            FurnaceRecipe recipe = null;
            String type = ShulkerBall.plugin.manager.getRecipe().getString(data.getConfigName() + ".Type");
            Material material = null;
            ItemStack item = null;

            if (type.equalsIgnoreCase("Material")) {
                material = Material.valueOf(ShulkerBall.plugin.manager.getRecipe().getString(data.getConfigName() + ".Item"));
                recipe = new FurnaceRecipe(data.getKey(), data.getItem(), material, (float) exp, time);
            } else {
                String configname = ShulkerBall.plugin.manager.getRecipe().getString(data.getConfigName() + ".Item");
                for (ItemData data1 : ItemData.data) {
                    if (data1.getConfigName().equalsIgnoreCase(configname)) {
                        item = data1.getItem();
                        break;
                    }
                }
                if (item == null) return null;
                recipe = new FurnaceRecipe(data.getKey(), data.getItem(), new RecipeChoice.ExactChoice(item), (float) exp, time);
            }
            data.setType(recipeType);
            return recipe;
        }
        return null;
    }
}
