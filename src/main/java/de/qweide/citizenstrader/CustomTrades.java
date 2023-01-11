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

    public boolean deleteRecipe(String recipeName) {
        if(availableRecipes.containsKey(recipeName)) {
            availableRecipes.remove(recipeName);
            assignedRecipes.removeIf(t -> t.getSecondValue().equals(recipeName));
            return true;
        }
        return false;
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

    public boolean deleteAssignment(int npcId, String recipeName) {
        return assignedRecipes.removeIf(t ->
            (t.getFirstValue().equals(npcId)) &&
            (recipeName == null || t.getSecondValue().equals(recipeName))
        );
    }
    public boolean deleteAssignment(int npcId) {
        return deleteAssignment(npcId, null);
    }

    public List<String> getAssignedRecipeNames(int npcId) {
        return assignedRecipes.stream()
            .filter(t -> t.getFirstValue().equals(npcId))
            .map(Tuple::getSecondValue)
            .collect(Collectors.toList());
    }

    public ArrayList<MerchantRecipe> getAssignedRecipes(int npcId) {
        ArrayList<MerchantRecipe> recipes = new ArrayList<>();
        for(String recipeName : getAssignedRecipeNames(npcId)) {
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
