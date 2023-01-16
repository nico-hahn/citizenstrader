# CitizensTrader

A plugin for Spigot Minecraft servers that provides a
trait for Citizens NPCs that allows them to trade like
vanilla villagers. Having the Citizens-Plugin instaled
is required.

## Features
* Create custom MerchantRecipes
* Connect (any and any amount of) custom MerchantRecipes to Citizens NPCs
* *trader* trait for Citizens NPCs
* Citizens NPCs will be able to be traded with (exactly like vanilla villagers)

## Usage
### Process
* Assign *trader* trait to NPC.
* Create custom MerchantRecipe using the *addtrade* command.
* Assign custom trade to NPC using the *assigntrade* command.

### Commands

#### addtrade
```
/addtrade <recipename> <item> <amount> <item> <amount>
```
Creates a custom MerchantRecipe with the given name. First item + amount
pair is the ingredient; second item + amount pair is the result.
As of now, recipes containing
multiple ingredeients are not supported.

#### deletetrade
```
/deletetrade <recipename>
```
Deletes the custom MerchantRecipe with the given name, if it
exists. If the recipe is assigned to a NPC, the link will be
deleted automatically.

#### listtrades
```
/listtrades [<recipename>]
```
Shows a list of all custom MerchantRecipes that are available.
If the optional parameter *recipename* is given, the ingredients
and result of the specified recipe is
shown, instead.

#### assigntrade
```
/assigntrade <recipename> <npcid>
```
Assigns the recipe with the given *recipename* to the NPC that
has the given *npcid*. The NPC will then offer a trade that matches
the given recipe.

#### assignlist
```
/assignlist <npcid>
```
Shows a list of all the recipes that are currently assigned
to the NPC with the given *npcid*.

#### assigndelete
```
/assigndelete <npcid> [<recipename>]
```
Deletes all the links between the NPC with the given *npcid*
and any recipe. If an optional *recipename* is given, only
the link between the NPC and the given recipe is deleted.

## Contribute

This plugin is still a work in progress. Please refer to the
issues page if you'd like to contribute to the project.
