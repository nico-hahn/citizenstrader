package de.qweide.citizenstrader;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Merchant;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

public class CitizensTraderPlugin extends JavaPlugin {

    private CustomTrades customTrades;
    @Override
    public void onEnable() {
        getLogger().info("Citizens Trader Plugin enabled.");
        net.citizensnpcs.api.CitizensAPI
                .getTraitFactory()
                .registerTrait(
                        net.citizensnpcs.api.trait.TraitInfo.create(TraderTrait.class)
                );
    }

    @Override
    public void onLoad() {
        super.onLoad();
        customTrades = new CustomTrades();
        File file = new File(getDataFolder(), "data.json");
        if (file.exists()) {
            try {
                customTrades = new CustomTrades().fromJson(
                    Files.readString(file.toPath())
                );
                getLogger().log(
                    Level.INFO,
                    String.format(
                        "Loaded %s trades and %s trade assignments",
                        customTrades.getTradeCount(),
                        customTrades.getAssignedCount()
                    )
                );
            } catch (IOException e) {
                getLogger().log(
                    Level.WARNING,
                    "No trades and assignments have been loaded because of an error:");
                getLogger().log(Level.SEVERE, e.getMessage());
            }

        }
        RecipeCommands commands = new RecipeCommands(customTrades);
        for(String command : new String[]{
            "addtrade", "assigntrade", "listtrades", "deletetrade",
            "assignlist", "assigndelete"
        }){
            this.getCommand(command).setTabCompleter(commands);
            this.getCommand(command).setExecutor(commands);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Citizens Trader Plugin disabled.");
        File file = new File(getDataFolder(), "data.json");
        try {
            if(!file.exists())
                file.createNewFile();
            Files.writeString(
                file.toPath(),
                customTrades.toJson().toString(4)
            );
            getLogger().log(
                Level.INFO,
                String.format(
                    "Saved %s trades and %s trade assignments",
                    customTrades.getTradeCount(),
                    customTrades.getAssignedCount()
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
        merchant.setRecipes(customTrades.getAssignedRecipes(npc.getId()));
        clickPlayer.openMerchant(merchant, true);
    }

    public void onNpcRemoved(int npcId) {
        this.customTrades.deleteAssignment(npcId);
    }
}
