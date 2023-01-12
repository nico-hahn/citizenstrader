package de.qweide.citizenstrader.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class MerchantRecipeSerializable extends MerchantRecipe implements JsonSerializable {

    public MerchantRecipeSerializable(MerchantRecipe recipe) {
        super(
            recipe.getResult(),
            recipe.getUses(),
            recipe.getMaxUses(),
            recipe.hasExperienceReward(),
            recipe.getVillagerExperience(),
            recipe.getPriceMultiplier(),
            recipe.getDemand(),
            recipe.getSpecialPrice()
        );
        this.setIngredients(recipe.getIngredients());
    }

    public MerchantRecipeSerializable(ItemStack result){
        super(
            result,
            0,
            Integer.MAX_VALUE,
            false
        );
    }

    public MerchantRecipeSerializable(){
        this(new ItemStack(Material.OAK_LOG, 1));
    }

    @Override
    public JSONObject toJson() {
        JSONArray ings = new JSONArray();
        for(ItemStack itemStack : this.getIngredients())
            ings.put(new JSONObject()
                .put("material", itemStack.getType().name())
                .put("amount", itemStack.getAmount())
            );
        return new JSONObject()
            .put(
                "result",
                new JSONObject()
                    .put("material", this.getResult().getType().name())
                    .put("amount", this.getResult().getAmount())
            )
            .put(
                "ingredients",
                ings
            );
    }

    @Override
    public MerchantRecipeSerializable fromJson(String json) throws IOException {
        JSONObject data = new JSONObject(json);
        JSONObject result = data.getJSONObject("result");
        MerchantRecipeSerializable recipe = new MerchantRecipeSerializable(
            new ItemStack(
                Material.getMaterial(result.getString("material")),
                result.getInt("amount")
            )
        );
        for(Object ingredient : data.getJSONArray("ingredients")) {
            recipe.addIngredient(
                new ItemStack(
                    Material.getMaterial(((JSONObject)ingredient).getString("material")),
                    ((JSONObject)ingredient).getInt("amount")
                )
            );
        }
        return recipe;
    }
}
