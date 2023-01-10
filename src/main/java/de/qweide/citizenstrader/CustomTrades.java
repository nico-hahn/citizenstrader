package de.qweide.citizenstrader;

import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public boolean assignRecipe(int npcId, String recipeName) {
        if (availableRecipes.get(recipeName) != null) {
            assignedRecipes.put(
                npcId,
                availableRecipes.get(recipeName)
            );
            return true;
        }
        return false;
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

    public Set<String> getTradeNames() {
        return availableRecipes.keySet();
    }
}
