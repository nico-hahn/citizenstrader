package de.qweide.citizenstrader;

import de.qweide.citizenstrader.data.JsonSerializable;
import de.qweide.citizenstrader.data.MerchantRecipeSerializable;
import de.qweide.citizenstrader.data.Tuple;
import org.bukkit.inventory.MerchantRecipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CustomTrades implements JsonSerializable {

    private ArrayList<Tuple<Integer, String>> assignedRecipes;
    private Map<String, MerchantRecipeSerializable> availableRecipes;

    public CustomTrades() {
        assignedRecipes = new ArrayList<>();
        availableRecipes = new HashMap<>();
    }

    public int getTradeCount() {
        return availableRecipes.size();
    }

    public int getAssignedCount() {
        return assignedRecipes.size();
    }

    public void addRecipe(String recipeName, MerchantRecipe recipe) {
        availableRecipes.put(recipeName, new MerchantRecipeSerializable(recipe));
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

    @Override
    public JSONObject toJson() {
        JSONArray assigned = new JSONArray();
        for (Tuple t : assignedRecipes)
            assigned.put(t.toJson());
        JSONObject trades = new JSONObject();
        for (String key : availableRecipes.keySet())
            trades.put(key, availableRecipes.get(key).toJson());
        return new JSONObject()
            .put("trades", trades)
            .put("assigned", assigned);
    }

    @Override
    public CustomTrades fromJson(String json) throws IOException {
        CustomTrades customTrades = new CustomTrades();
        JSONObject data = new JSONObject(json);
        for (Object jsonTuple : data.getJSONArray("assigned")){
            Tuple t = new Tuple().fromJson((String) jsonTuple);
            customTrades.assignedRecipes.add(t);
        }
        for (String key : data.getJSONObject("trades").keySet()) {
            customTrades.availableRecipes.put(
                key,
                new MerchantRecipeSerializable().fromJson(
                    data.getJSONObject("trades").getString(key)
                )
            );
        }
        return customTrades;
    }
}
