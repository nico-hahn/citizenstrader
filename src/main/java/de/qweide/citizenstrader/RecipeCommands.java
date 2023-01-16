package de.qweide.citizenstrader;

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
                    commandSender,
                    args[0],
                    args[1].toUpperCase(),
                    Integer.parseInt(args[2]),
                    args[3].toUpperCase(),
                    Integer.parseInt(args[4]),
                    args.length >= 6 ? args[5].toUpperCase() : null,
                    args.length >= 7 ? Integer.parseInt(args[6]) : null
                );
            case "assigntrade":
                return executeAssignTrade(
                    commandSender, args[0], Integer.parseInt(args[1])
                );
            case "listtrades":
                return executeListTrades(
                    commandSender, args.length >= 1 ? args[0] : null
                );
            case "deletetrade":
                return executeDeleteTrade(
                    commandSender,
                    args[0]
                );
            case "assignlist":
                return executeListAssignments(
                    commandSender,
                    Integer.parseInt(args[0])
                );
            case "assigndelete":
                return executeDeleteAssignment(
                    commandSender,
                    Integer.parseInt(args[0]),
                    args.length >= 2 ? args[1] : null
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
            /*if(args.length == 1) {
                return trades
                    .getTradeNames()
                    .stream().filter(p -> p.contains(args[0]))
                    .collect(Collectors.toList());
            }*/
            if (args.length == 2 || args.length == 4){
                return Arrays
                    .stream(Material.values())
                    .map(Enum::name)
                    .filter(m -> m.contains(args[args.length - 1]))
                    .collect(Collectors.toList());
            }
        }
        return null;
    }

    private boolean executeAddTrade(
        CommandSender sender,
        String recipeName,
        String resultMaterialName,
        int resultMaterialCount,
        String ingredient1MaterialName,
        int ingredient1MaterialCount,
        String ingredient2MaterialName,
        Integer ingredient2MaterialCount
    ) {
        Material material1In = Material.getMaterial(ingredient1MaterialName);
        Material material2In = null;
        if (ingredient2MaterialName != null) {
            material2In = Material.getMaterial(ingredient2MaterialName);
        }
        Material materialOut = Material.getMaterial(resultMaterialName);
        if(material1In == null || materialOut == null) {
            sender.sendMessage(
                String.format(
                    "Material(s) %s %s %s not found.",
                    (material1In == null) ? ingredient1MaterialName : "",
                    (material2In == null && ingredient2MaterialName != null) ?
                        ingredient2MaterialName : "",
                    (materialOut == null) ? resultMaterialName : ""
                )
            );
            return false;
        }
        ItemStack result = new ItemStack(materialOut, resultMaterialCount);
        MerchantRecipe recipe = new MerchantRecipe(result, 0, Integer.MAX_VALUE, false);
        recipe.addIngredient(new ItemStack(material1In, ingredient1MaterialCount));
        if(material2In != null && ingredient2MaterialCount != null) {
            recipe.addIngredient(new ItemStack(material2In, ingredient2MaterialCount));
        }
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

    private boolean executeDeleteTrade(
        CommandSender sender,
        String tradeName
    ) {
        sender.sendMessage(String.format(
            trades.deleteRecipe(tradeName) ?
                "Trade %s has been deleted." :
                "Trade %s has not been found.",
            tradeName
        ));
        return true;
    }

    private boolean executeListAssignments(
        CommandSender sender,
        int npcId
    ) {
        sender.sendMessage(
            String.format("Trades assigned to npc %s", npcId),
            String.join(", ", trades.getAssignedRecipeNames(npcId))
        );
        return true;
    }

    private boolean executeDeleteAssignment(
        CommandSender sender,
        int npcId,
        String tradeName
    ) {
        sender.sendMessage(
            trades.deleteAssignment(npcId, tradeName) ?
                String.format(
                    "Assignment(s)%s to NPC %s deleted successfully.",
                    (tradeName != null) ? String.format(" of trade %s", tradeName) : "",
                    npcId
                ) :
                "Assignment(s) not found. Nothing has been deleted."
        );
        return true;
    }
}

