package de.qweide.citizenstrader;

import com.google.common.base.MoreObjects;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeCommands implements CommandExecutor, TabCompleter {

    private final CustomTrades trades;

    public RecipeCommands(CustomTrades trades) {
        this.trades = trades;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        switch (label) {
            case "addtrade":
                return executeAddTrade(
                    commandSender, args[0], args[1].toUpperCase(), Integer.parseInt(args[2]), args[3].toUpperCase(), Integer.parseInt(args[4])
                );
            case "assigntrade":
                return executeAssignTrade(
                    commandSender, args[0], Integer.parseInt(args[1])
                );
            case "listtrades":
                return executeListTrades(
                    commandSender, args.length > 0 ? args[0] : null
                );
            default:
                commandSender.sendMessage("Something went wrong processing your command.");
                break;
        }
        return false;
    }

    public List<String> onTabComplete(
        CommandSender sender,
        Command command,
        String alias,
        String[] args
    ){
        if(command.getName().equalsIgnoreCase("addtrade")){
            List<String> l = new ArrayList<String>();
            if(args.length == 1) {
                return trades
                    .getTradeNames()
                    .stream().filter(p -> p.contains(args[0]))
                    .collect(Collectors.toList());
            }
            else if (args.length == 2 || args.length == 4){
                return Arrays
                    .stream(Material.values())
                    .map(m -> m.name())
                    .filter(m -> m.contains(args[args.length - 1]))
                    .collect(Collectors.toList());
            }
        }
        return null;
    }

    private boolean executeAddTrade(
        CommandSender sender,
        String recipeName,
        String inputMaterialName,
        int inputMaterialCount,
        String outputMaterialName,
        int outputMaterialCount
    ) {
        Material materialIn = Material.getMaterial(inputMaterialName);
        Material materialOut = Material.getMaterial(outputMaterialName);
        if(materialIn == null || materialOut == null) {
            sender.sendMessage(
                String.format(
                    "Material(s) %s %s not found.",
                    (materialIn == null) ? inputMaterialName : "",
                    (materialOut == null) ? outputMaterialName : ""
                )
            );
            return false;
        }
        ItemStack result = new ItemStack(materialOut, outputMaterialCount);
        MerchantRecipe recipe = new MerchantRecipe(result, 0, 64, false);
        recipe.addIngredient(new ItemStack(materialIn, inputMaterialCount));
        trades.addRecipe(recipeName, recipe);
        sender.sendMessage("Added trade successfully.");
        return true;
    }

    private boolean executeAssignTrade(
        CommandSender sender,
        String recipeName,
        int npcId
    ) {
        boolean result = trades.assignRecipe(npcId, recipeName);
        if (result)
            sender.sendMessage("Assigned trade successfully.");
        return result;
    }

    private boolean executeListTrades(
        CommandSender sender,
        String tradeName
    ) {
        if (tradeName == null){
            sender.sendMessage(
                "Names of all custom trades:",
                String.join(", ", new ArrayList<>(trades.getTradeNames()))
            );
        }
        else {
            if (trades.getTradeNames().contains(tradeName)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Takes: ");
                MerchantRecipe recipe = trades.getRecipeByName(tradeName);
                for(ItemStack i : recipe.getIngredients()) {
                    sb.append(i.getAmount());
                    sb.append("x");
                    sb.append(i.getType().name());
                    sb.append(", ");
                }
                sb.append("Gives: ");
                sb.append(recipe.getResult().getAmount());
                sb.append("x");
                sb.append(recipe.getResult().getType().name());
                sender.sendMessage(sb.toString());
            }
            else {
                sender.sendMessage("Specified trade does not exist.");
            }
        }
        return true;
    }
}

