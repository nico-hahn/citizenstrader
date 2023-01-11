package de.qweide.citizenstrader;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

@TraitName("trader")
public class TraderTrait extends Trait {

    private CitizensTraderPlugin plugin = null;

    public TraderTrait() {
        super("trader");
        plugin = JavaPlugin.getPlugin(CitizensTraderPlugin.class);
    }

    @EventHandler
    public void click(net.citizensnpcs.api.event.NPCRightClickEvent event){
        //Handle a click on a NPC. The event has a getNPC() method.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if(event.getNPC() == this.getNPC()) {
            plugin.openTrade(this.getNPC(), event.getClicker());
        }
    }

    @Override
    public void onAttach() {
        plugin.getServer().getLogger().info(npc.getName() + "has been assigned TraderTrait!");
    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {
        plugin.onNpcRemoved(this.getNPC().getId());
    }

}
