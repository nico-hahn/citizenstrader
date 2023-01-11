package de.qweide.citizenstrader;

import de.qweide.citizenstrader.data.Tuple;
import org.bukkit.inventory.MerchantRecipe;

import java.util.*;
import java.util.stream.Collectors;

public class CustomTrades {

    private ArrayList<Tuple<Integer, String>> assignedRecipes;
    private Map<String, MerchantRecipe> availableRecipes;

    public CustomTrades() {
        assignedRecipes = new ArrayList<>();
        availableRecipes = new HashMap<>();
    }

    public void addRecipe(String recipeName, MerchantRecipe recipe) {
        availableRecipes.put(recipeName, recipe);
    }

    public boolean assignRecipe(int npcId, String recipeName) {
        if (availableRecipes.get(recipeName) != null) {
            assignedRecipes.add(new Tuple<>(
                npcId,
                recipeName
            ));
            return true;
        }
        return false;
    }

    public ArrayList<MerchantRecipe> getAssignedRecipes(int npcId) {
        ArrayList<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
        for(String recipeName : assignedRecipes.stream()
            .filter(t -> t.getFirstValue() == npcId)
            .map(Tuple::getSecondValue)
            .collect(Collectors.toList())
        ) {
            if(availableRecipes.get(recipeName) != null) {
                recipes.add(availableRecipes.get(recipeName));
            }
        }
        return recipes;
    }

    public Set<String> getTradeNames() {
        return availableRecipes.keySet();
    }

    public MerchantRecipe getRecipeByName(String tradeName){
        return availableRecipes.get(tradeName);
    }
}
