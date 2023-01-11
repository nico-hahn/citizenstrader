package de.qweide.citizenstrader;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Merchant;
import org.bukkit.plugin.java.JavaPlugin;

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
        customTrades = new CustomTrades();
        RecipeCommands commands = new RecipeCommands(customTrades);
        this.getCommand("addtrade").setTabCompleter(commands);
        this.getCommand("addtrade").setExecutor(commands);
        this.getCommand("assigntrade").setTabCompleter(commands);
        this.getCommand("assigntrade").setExecutor(commands);
    }
    @Override
    public void onDisable() {
        getLogger().info("Citizens Trader Plugin disabled.");
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
