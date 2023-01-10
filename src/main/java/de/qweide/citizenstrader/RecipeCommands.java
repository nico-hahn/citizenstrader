package de.qweide.citizenstrader;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeCommands implements CommandExecutor, TabCompleter {

    private final CustomTrades trades;

    public RecipeCommands(CustomTrades trades) {
        this.trades = trades;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (label.equals("addtrade")) {
            Material mat1 = Material.getMaterial(args[1]);
            Material mat2 = Material.getMaterial(args[3]);
            if(mat1 == null) {
                commandSender.sendMessage(
                        String.format("Input Material %s not found. Using OAK_LOG instead", args[1])
                );
                mat1 = Material.OAK_LOG;
            }
            if(mat2 == null) {
                commandSender.sendMessage(
                        String.format("Output Material %s not found. Using OAK_LOG instead", args[3])
                );
                mat2 = Material.OAK_LOG;
            }
            createTrade(
                    args[0],
                    mat1,
                    Integer.parseInt(args[2]),
                    mat2,
                    Integer.parseInt(args[4]));
            commandSender.sendMessage("Added trade successfully.");
            return true;
        }

        if (label.equals("assigntrade")) {
            assignTrade(
                    args[0],
                    Integer.parseInt(args[1])
            );
            commandSender.sendMessage("Assigned trade successfully.");
            return true;
        }
        commandSender.sendMessage("Something went wrong processing your command.");
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        if(command.getName().equalsIgnoreCase("addtrade")){
            List<String> l = new ArrayList<String>(); //makes a ArrayList
            if (args.length == 2 || args.length == 4){
                for(Material m : Material.values()) {
                    l.add(m.name());
                }
            }
            return l;
        }
        return null;
    }

    private void createTrade(String name, Material in, int inCount, Material out, int outCount) {
        ItemStack result = new ItemStack(out, outCount);
        MerchantRecipe recipe = new MerchantRecipe(result, 0, 64, false);
        recipe.addIngredient(new ItemStack(in, inCount));
        trades.addRecipe(name, recipe);
    }

    private void assignTrade(String recipeName, int npcId) {
        trades.assignRecipe(npcId, recipeName);
    }
}

