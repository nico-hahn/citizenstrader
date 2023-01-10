package de.qweide.citizenstrader;

import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomTrades {

    private Map<Integer, MerchantRecipe> assignedRecipes;
    private Map<String, MerchantRecipe> availableRecipes;

    public CustomTrades() {
        assignedRecipes = new HashMap<Integer, MerchantRecipe>();
        availableRecipes = new HashMap<String, MerchantRecipe>();
    }

    public void addRecipe(String recipeName, MerchantRecipe recipe) {
        availableRecipes.put(recipeName, recipe);
    }

    public void assignRecipe(int npcId, String recipeName) {
        assignedRecipes.put(
                npcId,
                availableRecipes.get(recipeName)
        );
    }

    public MerchantRecipe getRecipe(String name) {
        return availableRecipes.get(name);
    }

    public ArrayList<MerchantRecipe> getAssignedRecipes(int npcId) {
        ArrayList<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
        for(int i : assignedRecipes.keySet()) {
            if (i == npcId) {
                recipes.add(assignedRecipes.get(i));
            }
        }
        return recipes;
    }
}
