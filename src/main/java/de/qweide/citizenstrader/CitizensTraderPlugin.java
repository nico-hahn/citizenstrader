package de.qweide.citizenstrader;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Merchant;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class CitizensTraderPlugin extends JavaPlugin {

    private CustomRecipes customRecipes;
    @Override
    public void onEnable() {
        getLogger().info("Citizens Trader Plugin enabled.");
        net.citizensnpcs.api.CitizensAPI
                .getTraitFactory()
                .registerTrait(
                        net.citizensnpcs.api.trait.TraitInfo.create(TraderTrait.class)
                );
        customRecipes = new CustomRecipes();
        File file = new File(getDataFolder(), "data.json");
        if (file.exists()) {
            try {
                customRecipes = new CustomRecipes().fromJson(
                    Files.readString(file.toPath())
                );
                getLogger().log(
                    Level.INFO,
                    String.format(
                        "Loaded %s recipes and %s recipe assignments",
                        customRecipes.getRecipeCount(),
                        customRecipes.getAssignedCount()
                    )
                );
            } catch (IOException e) {
                getLogger().log(
                    Level.WARNING,
                    "No recipes and assignments have been loaded because of an error:");
                getLogger().log(Level.SEVERE, e.getMessage());
            }

        }
        RecipeCommands commands = new RecipeCommands(customRecipes);
        for(String command : new String[]{
            "addrecipe", "assignrecipe", "listrecipes", "deleterecipe",
            "assignlist", "assigndelete"
        }){
            this.getCommand(command).setTabCompleter(commands);
            this.getCommand(command).setExecutor(commands);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Citizens Trader Plugin disabled.");
        try {
            if(!getDataFolder().isDirectory())
                getDataFolder().mkdir();
            File file = new File(getDataFolder(), "data.json");
            if(!file.exists())
                file.createNewFile();
            Files.writeString(
                file.toPath(),
                customRecipes.toJson().toString(4)
            );
            getLogger().log(
                Level.INFO,
                String.format(
                    "Saved %s recipes and %s recipe assignments",
                    customRecipes.getRecipeCount(),
                    customRecipes.getAssignedCount()
                )
            );
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, e.getMessage());
        }


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    public void openTrade(NPC npc, Player clickPlayer) {
        Merchant merchant = Bukkit.createMerchant(npc.getRawName());
        merchant.setRecipes(customRecipes.getAssignedRecipes(npc.getId()));
        clickPlayer.openMerchant(merchant, true);
    }

    public void onNpcRemoved(int npcId) {
        this.customRecipes.deleteAssignment(npcId);
    }
}
