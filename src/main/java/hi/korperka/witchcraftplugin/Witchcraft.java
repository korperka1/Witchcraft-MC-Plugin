package hi.korperka.witchcraftplugin;

import hi.korperka.witchcraftplugin.commands.WandCommand;
import hi.korperka.witchcraftplugin.listeners.MagicWandListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Witchcraft extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Witchcraft plugin has started");
        getServer().getPluginManager().registerEvents(new MagicWandListener(), this);
        getCommand("wand").setExecutor(new WandCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Witchcraft plugin has stopped");
    }
}
